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
import android.support.v7.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {
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

        Intent veri = new Intent(this, MainActivity.class);// for testing purpose
        this.startActivity(veri);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }
}
