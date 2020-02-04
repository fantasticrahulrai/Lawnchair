/*
 *     Copyright (C) 2020 Lawnchair Team.
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;
import com.android.launcher3.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.util.ArrayList;
import wallunix.uhd.wallpaper.wallpapers4k.MainActivity;


public class QuoteAlamarManagerBootReciver extends BroadcastReceiver {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;


    private static ArrayList<Wallpaper> mWallpaperList;
    int totalImages=0, randNo=1;
    File localFile;

    SharedPreferences mUserDetails, sharedPrefs;
    SharedPreferences.Editor mEditor;
    String autoWallString= "no wallpaper";
    int screenHeight,screenWidth;
    boolean previewQualityLow;


    @Override
    public void onReceive(final Context context, Intent intent) {

       // if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) { }

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            Intent serviceIntent = new Intent(context, QuoteAlarmService.class);
            context.startService(serviceIntent);
        }
        else {
            Toast.makeText(context.getApplicationContext(), "Alarm Manager just ran", Toast.LENGTH_LONG).show();
            sendNotificationQuote(context);
        }



    }


    private void sendNotificationQuote(Context context) {

        String thought, author;

        QuotesSqlite sqdb = new QuotesSqlite(context);
        Quotes q=sqdb.getRandomQuote();

        if(q.getAuthor().isEmpty())
            author = "Anonymous";
        else
            author = q.getAuthor();

        thought = q.getQuote()+"\n\t\t\t\t\t\t\t ~"+author;


        //*******************

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 001, intent, 0);

        //*************************
        Intent sendIntent = new Intent(context,ShareBroadcast.class);
        sendIntent.putExtra("Thought", thought);
        PendingIntent piAction1 = PendingIntent.getBroadcast(context, 001, sendIntent, 0);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "James Bond")
                .setSmallIcon(R.drawable.lightbulb)
                .setContentTitle("Thought of the Day")
                .setContentText(thought)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(thought))
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.whatsapp,"Whatsapp", piAction1);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(01, builder.build());
    }


}