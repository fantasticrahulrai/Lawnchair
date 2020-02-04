/*
 *     Copyright (C) 2019 Lawnchair Team.
 *
 *     This file is part of Lawnchair Launcher.
 *
 *     Lawnchair Launcher is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Lawnchair Launcher is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Lawnchair Launcher.  If not, see <https://www.gnu.org/licenses/>.
 */

package wallunix.uhd.wallpaper.wallpapers4k.Classes;


import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.android.launcher3.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FileDownloadTask.TaskSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import wallunix.uhd.wallpaper.wallpapers4k.MainActivity;

public class AutoWallpaperAsyncTask extends AsyncTask<String, String, String> {


    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;

    private EditorsChoiceSqlite sqdb;
    Wallpaper mWallpaper;

    File localFile;

    SharedPreferences mUserDetails, sharedPrefs;
    SharedPreferences.Editor mEditor;
    String autoWallString = "no wallpaper";
    int screenHeight, screenWidth;
    boolean tableCreated;


    private String resp, screenPref;
    private int screenPrefValue;
    private WeakReference<Context> contextRef;

    public AutoWallpaperAsyncTask(Context context) {

        contextRef = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... params) {

        Context mContext = contextRef.get();

        if (!tableCreated) {
            parseToSqlite(mContext);
        }


        if (tableCreated) {
            autoWallpaperChange(mContext);
        }



        return resp;
    }


    @Override
    protected void onPostExecute(String result) {
        // execution of result of Long time consuming operation
        Context mContext = contextRef.get();
        //Toast.makeText(mContext, "Wallunix Changed", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onPreExecute() {

        Context mContext = contextRef.get();
        mUserDetails = mContext.getSharedPreferences("UserDetails", 0); // 0 - for private mode
        screenHeight = mUserDetails.getInt("screenheight", 1000);
        screenWidth = mUserDetails.getInt("screenwidth", 700);
        tableCreated = mUserDetails.getBoolean("editortablecreated", false);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        screenPref = sharedPrefs.getString("screenPref", "Both (Home and Lockscreen)");


        sqdb = new EditorsChoiceSqlite(mContext);
        //Toast.makeText(mContext, "Wallunix is changing the wallpaper", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onProgressUpdate(String... text) {


    }

    //**************************auto wall methods*****************
    private void autoWallpaperChange(final Context context) {


        try {

            mWallpaper = sqdb.getRandomWallpaper();
            storageRef = storage.getReferenceFromUrl(mWallpaper.getWallpaper());
            localFile = File.createTempFile("images", "jpg");

            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                    WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context);


                    try {

                        myWallpaperManager.suggestDesiredDimensions(screenWidth, screenHeight);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            myWallpaperManager.setBitmap(bitmap);
                        }
                        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                            if (screenPref.equalsIgnoreCase("Both (Home and Lockscreen)"))
                                myWallpaperManager.setBitmap(bitmap, null,true,
                                        WallpaperManager.FLAG_SYSTEM| WallpaperManager.FLAG_LOCK);
                            else if (screenPref.equalsIgnoreCase("Homescreen"))
                                myWallpaperManager.setBitmap(bitmap, null,true, WallpaperManager.FLAG_SYSTEM);
                            else if (screenPref.equalsIgnoreCase("Lockscreen"))
                                myWallpaperManager.setBitmap(bitmap, null,true, WallpaperManager.FLAG_LOCK);
                        }



                        //**************
                        Date currentTime = Calendar.getInstance().getTime();
                        autoWallString = mWallpaper.getThumbnail();

                        mEditor = mUserDetails.edit();
                        mEditor.putString("todayspick", screenPref);
                        mEditor.putString("changedAt",currentTime+"");
                        mEditor.apply();

                        sendNotification(context);


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void parseToSqlite(Context context){

        final List<Wallpaper> mWallpaperListParse = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("editorschoice");
        query.addDescendingOrder("name");
        query.setLimit(1000);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> wallpaperlist, ParseException e) {
                if (e == null) {
                    for (ParseObject w : wallpaperlist) {

                        Wallpaper mWallpaper = new Wallpaper(w.getString("name"), w.getString("thumbnail"), w.getString("wallpaper"),
                                w.getInt("downloads"), w.getString("category"), w.getString("firebaseid"));

                        mWallpaperListParse.add(mWallpaper);
                    }

                    sqdb.batchInsterstion(mWallpaperListParse);

                    //************ populating grid methods ****************
                    autoWallpaperChange(context);

                    //**************** marking table as created**************
                    mEditor = mUserDetails.edit();
                    mEditor.putBoolean("editortablecreated", true);
                    mEditor.apply();


                } else {

                }
            }
        });

    }

    public void sendNotification(Context context) {

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 001, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "James Bond")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Wallpaper Changed. Didn't like the current wallpaper?")
                .setContentText("Tap to select another Wallpaper, swipe to dismiss")
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(01, builder.build());
    }

}