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


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Utils {

    public static final long ONE_DAY = 24 * 60 * 60 * 1000L; // 1 Day
    public static final long SIX_HOUR = 6 * 60 * 60 * 1000L; // 6 Hour
    public static final long THREE_HOUR = 3*60 * 60 * 1000L; // 1 hour
    public static final long ONE_HOUR = 60 * 60 * 1000L; // 1 hour
    public static final long FIFTEEN_MINUTE = 15 * 60 * 1000L; // 15 minutes

    public static String ABSTRACT_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com" +
            "/o/categories_thumbnails%2Fabstractbg.jpg?alt=media&token=f36c4d20-8127-46fb-a16c-71a640415b93";

    public static String AMOLED_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com" +
            "/o/categories_thumbnails%2Famoledbg.jpg?alt=media&token=792e3b10-36e4-4355-99c5-646e8103c7fe";

    public static String ANIME_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fanimebg.jpg?alt=media&token=6ff73738-58f0-4338-8f4e-f0fb8ee2d4c5";

    public static String ANIMAL_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fanimalbg.jpg?alt=media&token=efc3b0e0-9722-4780-921d-37a9b45e78ff";

    public static String ART_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/o/" +
            "categories_thumbnails%2Fartbg.jpeg?alt=media&token=25d3512c-592b-4762-90d1-d0562b2019c7";

    public static String BUILDINGS_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fbuildingsbg.jpg?alt=media&token=976a96cc-5135-4f41-8a45-5d24023da179";

    public static String CARS_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fcarsbg.jpg?alt=media&token=f804bbc1-5185-414b-80d3-1d677f8bf231";

    public static String CITYSCAPE_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fcityscapebg.jpg?alt=media&token=0ca40546-be1d-4eaf-8992-2006dd4a8763";

    public static String FANTASY_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Ffantasybg.jpg?alt=media&token=22afe2a1-6df1-4662-aa7b-e547211af65d";

    public static String FLOWER_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fflowerbg.jpg?alt=media&token=600fbab5-f0c4-4845-a1ec-d62bd1460ba8";

    public static String GAMES_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fgamesbg.jpg?alt=media&token=54556488-2322-49fc-8a18-e92f02ebade7";

    public static String LIGHT_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Flightbg.jpg?alt=media&token=84cd7159-d77d-4adb-be5f-0ad093c89db0";

    public static String MINIMAL_URL= "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fminimalbg.jpg?alt=media&token=e88ce4a7-926e-4d1a-88ca-a96ea04cac96";

    public static String MOVIES_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fmoviesbg.jpg?alt=media&token=89117b1f-86c3-4dfe-86bb-b308b9098870";

    public static String NATURE_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fnaturebg.jpg?alt=media&token=b2d4983b-18f5-4bbe-bc9c-4d5e08768cef";

    public static String OTHERS_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fothersbg.jpg?alt=media&token=3daa242d-f2c3-4520-94bc-302e819a8962";

    public static String PEOPLE_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fpeoplebg.jpg?alt=media&token=3f68ab7c-fa25-426f-ae58-79a7b89af771";

    public static String SPACE_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fspacebg.jpg?alt=media&token=ffcb65e7-ee50-455b-a4d6-42a8198e66f6";

    public static String SUPERHEROES_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fsuperheroesbg.jpg?alt=media&token=107a787b-4db9-4374-98dc-7f8db02d6dfe";

    public static String VEHICLE_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fvehiclebg.jpg?alt=media&token=d80d3cc0-3137-481e-9eb2-8cc1c2d72dcd";

    public static String MOTERCYCLES_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fmotercyclesbg.jpg?alt=media&token=5b0ef5b9-8fb6-401f-8258-510487bf2293";

    public static String LOVE_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Flovebg.jpg?alt=media&token=7fc4f5a1-810a-4b6d-af8a-60bf6e78e31c";

    public static String TECHNOLOGY_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Ftechnologybg.jpg?alt=media&token=5a6e1173-461f-4e82-b2b1-770e2fa8412d";

    public static String SPORTS_URL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/categories_thumbnails%2Fsportsbg.jpg?alt=media&token=71f49827-6de4-4d0a-af0b-cd270d875db5";



    //***************spotlight*********
    public static String SPOTLIGHT_THUMBNAIL = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/spotlight%2Ft.jpg?alt=media&token=943f2661-e7c1-428d-a98c-feb3f5110111";


    public static String SPOTLIGHT_WALLPAPER = "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/" +
            "o/spotlight%2Fw.jpg?alt=media&token=f161cdfb-78b5-457c-a213-179c3506292a";

    public static String DEFAULT_PIC = "https://firebasestorage.googleapis.com/v0/" +
            "b/raiwallpaper.appspot.com/o/artists_dp%2Fdp.jpeg?alt=media&token=562ea210-0590-4d3e-ab02-2ae6e1930368";








    public static String RATE_ON_GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=hd.uhd.wallpaper.wallpapers4k";

    public static ColorDrawable[] vibrantLightColorList =
            {
                    new ColorDrawable(Color.parseColor("#9ACCCD")), new ColorDrawable(Color.parseColor("#8FD8A0")),
                    new ColorDrawable(Color.parseColor("#CBD890")), new ColorDrawable(Color.parseColor("#DACC8F")),
                    new ColorDrawable(Color.parseColor("#D9A790")), new ColorDrawable(Color.parseColor("#D18FD9")),
                    new ColorDrawable(Color.parseColor("#FF6772")), new ColorDrawable(Color.parseColor("#DDFB5C"))
            };


    public static ColorDrawable getRandomDrawbleColor() {
        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }


    //********************************************************************

    public static void setAlarmQuote(Context context) {


        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmMgr= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver

        Intent intent = new Intent(context, QuoteAlamarManagerBootReciver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 007, intent, 0);

        // Set the alarm to start at approximately 5:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 5);

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);

        Toast.makeText(context, "Thought of the day is on", Toast.LENGTH_SHORT).show();
    }


    public static void cancelAlarmQuote(Context context) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmMgr= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver

        Intent intent = new Intent(context, QuoteAlamarManagerBootReciver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 007, intent, 0);


        alarmMgr.cancel(pi);
        Toast.makeText(context, "Thought of the day is off", Toast.LENGTH_SHORT).show();
    }


    public static boolean checkAlarmQuote(Context context){
        boolean alarmRunning;

        Intent intentCheck = new Intent(context, QuoteAlamarManagerBootReciver.class);
        alarmRunning = (PendingIntent.getBroadcast(context, 007, intentCheck, PendingIntent.FLAG_NO_CREATE) != null);

        return alarmRunning;
    }

    //********************************************************************

    public static void turnOnJobScheduler(Context context) {


        long interval = ONE_DAY;
        //*********getting prefrence and converting to long *******************
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String intervalPref = sharedPrefs.getString("frequencyPref", "Daily (Default)");

        if(intervalPref.equalsIgnoreCase("Daily (Deafult)"))
            interval=ONE_DAY;
        else if(intervalPref.equalsIgnoreCase("6 Hours"))
            interval=SIX_HOUR;
        else if(intervalPref.equalsIgnoreCase("3 Hours"))
            interval=THREE_HOUR;
        else if(intervalPref.equalsIgnoreCase("1 Hour"))
            interval=ONE_HOUR;
        else if(intervalPref.equalsIgnoreCase("15 Minutes"))
            interval=FIFTEEN_MINUTE;

        //********************************************************************

        //******************job schedular**************

        ComponentName componentName = new ComponentName(context, AutoWallpaperScheduler.class);
        JobInfo jobInfo = new JobInfo.Builder(12, componentName)
                //.setRequiresCharging(true)
                .setPersisted(true)
                .setPeriodic(interval)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();


        JobScheduler mJobScheduler = (JobScheduler)context.getSystemService(context.JOB_SCHEDULER_SERVICE);
        int resultCode = mJobScheduler.schedule(jobInfo);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Toast.makeText(context, "Auto Wallpaper set on "+ intervalPref, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Some error while scheduling Auto Wallpaper", Toast.LENGTH_SHORT).show();
        }
    }

    public static void turnOffJobScheduler(Context context) {

       // AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
       // AlarmManager alarmMgr= (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver

        //Intent intent = new Intent(context, QuoteAlamarManagerBootReciver.class);
        //PendingIntent pi = PendingIntent.getBroadcast(context, 007, intent, 0);
        // alarmMgr.cancel(pi);

        JobScheduler mJobScheduler = (JobScheduler)context.getSystemService(context.JOB_SCHEDULER_SERVICE);
        mJobScheduler.cancel(12);



    }



    public static boolean isJobServiceOn( Context context ) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;

        boolean hasBeenScheduled = false ;

        for ( JobInfo jobInfo : scheduler.getAllPendingJobs() ) {
            if ( jobInfo.getId() == 12 ) {
                hasBeenScheduled = true ;
                break ;
            }
        }

        return hasBeenScheduled ;
    }


    //**************************job Sheduler for thought of the day

    public static void turnOnJobSchedulerQuote(Context context) {


        long interval = ONE_DAY;

        //******************job schedular**************

        ComponentName componentName = new ComponentName(context, QuoteScheduler.class);
        JobInfo jobInfo = new JobInfo.Builder(13, componentName)
                //.setRequiresCharging(true)
                .setPersisted(true)
                .setPeriodic(interval)
                .build();


        JobScheduler mJobScheduler = (JobScheduler)context.getSystemService(context.JOB_SCHEDULER_SERVICE);
        int resultCode = mJobScheduler.schedule(jobInfo);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Toast.makeText(context, "Thought of the day is on", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error turning thought of the day", Toast.LENGTH_SHORT).show();
        }
    }

    public static void turnOffJobSchedulerQuote(Context context) {

        JobScheduler mJobScheduler = (JobScheduler)context.getSystemService(context.JOB_SCHEDULER_SERVICE);
        mJobScheduler.cancel(13);

        Toast.makeText(context, "Thought of the day is off", Toast.LENGTH_SHORT).show();

    }


    public static boolean isJobServiceOnQuote( Context context ) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;

        boolean hasBeenScheduled = false ;

        for ( JobInfo jobInfo : scheduler.getAllPendingJobs() ) {
            if ( jobInfo.getId() == 13 ) {
                hasBeenScheduled = true ;
                break ;
            }
        }

        return hasBeenScheduled ;
    }

    //**************************

    //for showing the user settings toast
    public void showUserSettings(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        StringBuilder builder = new StringBuilder();

        builder.append("\n Username: "
                + sharedPrefs.getBoolean("notificationPref", false));

        builder.append("\n Send report:"
                + sharedPrefs.getBoolean("previewPref", false));

        builder.append("\n Sync Frequency: "
                + sharedPrefs.getString("frequencyPref", "NULL"));

        Toast.makeText(context, "Prefrence check "+builder.toString(), Toast.LENGTH_SHORT).show();

    }


    public void shareUs(Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=hd.best.wallpapers.amoledwallpaper");
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int dpToPx(int dp, Context context) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    public static void openUrl(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static String catergotyThumbnail(String category){

        String url = "";

        if (category.equalsIgnoreCase("Abstract"))
            url = ABSTRACT_URL;

        if (category.equalsIgnoreCase("Art"))
            url = ART_URL;

        if (category.equalsIgnoreCase("Anime"))
            url = ANIME_URL;

        if (category.equalsIgnoreCase("Animal"))
            url = ANIMAL_URL;

        if (category.equalsIgnoreCase("Cars"))
            url = CARS_URL;

        if (category.equalsIgnoreCase("Fantasy"))
            url = FANTASY_URL;

        if (category.equalsIgnoreCase("Flower"))
            url = FLOWER_URL;

        if (category.equalsIgnoreCase("Games"))
            url = GAMES_URL;

        if (category.equalsIgnoreCase("People"))
            url = PEOPLE_URL;

        if (category.equalsIgnoreCase("Space"))
            url = SPACE_URL;

        if (category.equalsIgnoreCase("Superheroes"))
            url = SUPERHEROES_URL;

        if (category.equalsIgnoreCase("Movies"))
            url = MOVIES_URL;

        return url;
    }


    public static String loadJSONFromAsset(Context context, String json) {

        try {
            InputStream is = context.getAssets().open(json);

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


    public static void likeCounter(Wallpaper mWallpaper){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("wallpapers");
        query.whereEqualTo("firebaseid",mWallpaper.getId());
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> wallpaperlist, ParseException e) {
                if (e == null) {
                    for (ParseObject w : wallpaperlist) {

                        w.increment("downloads");
                        w.saveInBackground();
                    }


                } else {

                }
            }
        });
    }

    public static Boolean isAuth (Context context){

        Boolean b = true;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        SharedPreferences mUserDetails = context.getSharedPreferences("UserDetails", 0); // 0 - for private mode
        boolean signedIn = mUserDetails.getBoolean("signedin", false);


        if (user==null || !signedIn)
            b=false;



        return b;
    }


    public static void share (Context context, String msg){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public static void shareWhatsapp (Context context, String msg){

        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, msg);


        try {
           context.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }

}
