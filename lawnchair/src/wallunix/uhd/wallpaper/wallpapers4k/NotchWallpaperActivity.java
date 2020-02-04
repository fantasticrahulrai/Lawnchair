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
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.android.launcher3.R;
import com.cleveroad.androidmanimation.LoadingAnimationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;
import wallunix.uhd.wallpaper.wallpapers4k.Adapters.ImageAdapter;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.NotchSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;

public class NotchWallpaperActivity extends AppCompatActivity {

    private NotchSqlite sqdb;
    private ArrayList<Wallpaper> mWallpaperList;
    private List<Wallpaper> mWallpaperListSqlite;
    GridView gridview;
    SwipeRefreshLayout pullToRefresh;
    ImageAdapter adapter;
    private LoadingAnimationView animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallunix_activity_notch_wallpaper);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.notch));

        mWallpaperList = new ArrayList<>();
        adapter = new ImageAdapter(getApplicationContext(), mWallpaperList);

        gridview = (GridView) findViewById(R.id.gridview);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refreshData(); // your code

                updateLockscreens();

            }
        });


        animation = (LoadingAnimationView) findViewById(R.id.animation);
        animation.setVisibility(View.VISIBLE);
        animation.startAnimation();


        sqdb = new NotchSqlite(this);
        mWallpaperListSqlite = sqdb.getAllWallpapers();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Sending image id to FullScreenActivity
                Wallpaper mWallpaper;
                mWallpaper = mWallpaperList.get(position);

                Intent i = new Intent(getApplicationContext(), FullscreenActivity.class);
                // passing array index
                i.putExtra("url", mWallpaper);
                startActivity(i);


            }
        });

        load();

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

    @Override
    public void onDestroy() {
        super.onDestroy();

        finish();
    }

    private void updateLockscreens() {


    }

    private void load(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("notch");
        query.addDescendingOrder("createdAt");
        query.setLimit(10000);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> wallpaperlist, ParseException e) {
                if (e == null) {
                    for (ParseObject w : wallpaperlist) {

                        Wallpaper mWallpaper = new Wallpaper(w.getString("name"), w.getString("thumbnail"), w.getString("wallpaper"),
                                w.getInt("downloads"), w.getString("category"), w.getString("firebaseid"),
                                w.getString("artistsid"), w.getString("artistname"), w.getString("artistdp"));

                        mWallpaperList.add(mWallpaper);


                    }


                    animation.stopAnimation();
                    animation.setVisibility(View.INVISIBLE);

                   // Collections.shuffle(mWallpaperList);// for suffling the list
                    gridview.setAdapter(adapter);



                } else {

                    Toast.makeText(getApplicationContext(), "Server Error "+ e, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
