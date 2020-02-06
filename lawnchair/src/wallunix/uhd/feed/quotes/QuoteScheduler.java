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

package wallunix.uhd.feed.quotes;

import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.android.launcher3.R;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.ShareBroadcast;
import wallunix.uhd.wallpaper.wallpapers4k.MainActivity;

public class QuoteScheduler extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {

        sendNotificationQuote(this);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {



        return false;
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
        Intent sendIntent = new Intent(context, ShareBroadcast.class);
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
