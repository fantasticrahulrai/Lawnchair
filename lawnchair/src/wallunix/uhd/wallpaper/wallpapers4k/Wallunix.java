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

package wallunix.uhd.wallpaper.wallpapers4k;

import android.app.Application;
import android.content.Context;
import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.parse.Parse;
import com.parse.ParseInstallation;

public class Wallunix extends Application {



    private static Context context;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // OneSignal Initialization
       // OneSignal.startInit(this)
         //       .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
         //       .unsubscribeWhenNotificationsAreDisabled(true)
        //        .init();


        // Daddy Parse is back

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("5ZSBrcMuwOZbJcOqkPEnHhd7PbKEl4psH574lr1g")
                // if defined
                .clientKey("uSFMY6ANnqoOoJprbAZAbA8bbETNzPcM6ylDYaOt")
                .server("https://parseapi.back4app.com/")
                .enableLocalDataStore()
                .build()
        );
        ParseInstallation.getCurrentInstallation().saveInBackground();


        //Flurry analytics
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "X35S8HRC74PZTP9BZ65T");


    }

    public static Context getContext(){
        return context;
    }


}
