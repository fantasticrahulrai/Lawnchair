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


import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.isJobServiceOn;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.turnOnJobScheduler;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.launcher3.R;
import wallunix.uhd.wallpaper.wallpapers4k.Fragments.SettingsFragment;


public class SettingActivity extends AppCompatActivity {

    SharedPreferences prefs;
    long interval;
    boolean alarmRunning, jobRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_setting);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.settings);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();


        final SharedPreferences.OnSharedPreferenceChangeListener listener;
        //Loads Shared preferences
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Setup a shared preference listener for hpwAddress and restart transport
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

                if (key.equals("frequencyPref")) {
                    //Do stuff; restart activity in your case

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    //String intervalPref = sharedPrefs.getString("frequencyPref", "Daily (Default)");

                    if (isJobServiceOn(getApplicationContext()))
                        turnOnJobScheduler(getApplicationContext());

                    else if(!isJobServiceOn(getApplicationContext()))

                        Toast.makeText(getApplicationContext(), "First turn on Auto Wallpaper" , Toast.LENGTH_SHORT).show();


                }
            }

        };

        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_settings:
                // startSettings();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
