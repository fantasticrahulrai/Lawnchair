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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import com.android.launcher3.R;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughActivity;
import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughCard;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends FancyWalkthroughActivity {
    SharedPreferences mUserDetails;
    SharedPreferences.Editor mEditor;
    Boolean verified;
    Boolean introPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //setContentView(R.layout.activity_splash);

        mUserDetails = getApplicationContext().getSharedPreferences("UserDetails", 0); // 0 - for private mode
        verified=mUserDetails.getBoolean("v", false);
        introPressed=getIntent().getBooleanExtra("pressed",false);




        if (verified==true && introPressed==false) {
            Intent veri = new Intent(this, MainActivity.class);// for testing purpose
            SplashActivity.this.startActivity(veri);

            finish();
        }


        else if (verified==false||introPressed==true){

            //startActivity(new Intent(SplashActivity.this, IntroActivity.class));

            String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
            String display = android.os.Build.DISPLAY;

            FancyWalkthroughCard fancywalkthroughCard1 = new FancyWalkthroughCard(deviceName.toUpperCase(), "All wallpapers are adapted to your device ",R.drawable.ic_launcher);
            FancyWalkthroughCard fancywalkthroughCard2 = new FancyWalkthroughCard("Great artworks made by artists", "We recommend you checking WallUnix daily for new artworks.",R.drawable.intro_art);
            //FancyWalkthroughCard fancywalkthroughCard3 = new FancyWalkthroughCard("BETA Release", "We are adding new wallpapers everyday. Stay tuned.",R.drawable.intro_update);
            fancywalkthroughCard1.setDescriptionColor(R.color.black);
            fancywalkthroughCard2.setDescriptionColor(R.color.black);

            List<FancyWalkthroughCard> pages = new ArrayList<>();

            pages.add(fancywalkthroughCard1);
            pages.add(fancywalkthroughCard2);
            //pages.add(fancywalkthroughCard3);

            setImageBackground(R.drawable.intro_bg);
            setFinishButtonTitle("Get Started");
            showNavigationControls(true);
            setColorBackground(R.color.white);
            setInactiveIndicatorColor(R.color.grey_600);
            setActiveIndicatorColor(R.color.red);
            setOnboardPages(pages);

           // Toast.makeText(getApplication(), "Introduction", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onFinishButtonPressed() {

        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        mEditor = mUserDetails.edit();
        mEditor.putBoolean("v", true );
       // mEditor.putBoolean("autowall", true );
       // mEditor.putString("todayspick", "no wallpaper");
        mEditor.apply();

        Toast.makeText(getApplication(), "Starting...", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }
}
