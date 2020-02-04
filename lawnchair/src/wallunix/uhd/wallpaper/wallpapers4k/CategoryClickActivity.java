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
import com.android.launcher3.R;
import com.cleveroad.androidmanimation.LoadingAnimationView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import wallunix.uhd.wallpaper.wallpapers4k.Adapters.ImageAdapter;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.OfflineWallpaperSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;

public class CategoryClickActivity extends AppCompatActivity {

    private OfflineWallpaperSqlite sqdb;
    private static ArrayList<Wallpaper> mWallpaperList;
    private static List<Wallpaper> mWallpaperListSqlite;

    GridView gridview;
    SwipeRefreshLayout pullToRefresh;
    ImageAdapter adapter;
    private LoadingAnimationView animation;

    String categoryText = "";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallunix_activity_category_click);

        categoryText = getIntent().getStringExtra("categoryText");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(categoryText);


        mWallpaperList = new ArrayList<>();
        adapter = new ImageAdapter(getApplicationContext(), mWallpaperList);

        gridview = (GridView) findViewById(R.id.gridview);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refreshData(); // your code

                updateWallpapers();

            }
        });

        sqdb = new OfflineWallpaperSqlite(this);
        mWallpaperListSqlite = sqdb.getCategoryWallpaper(categoryText);

        for (Wallpaper mWallpaper : mWallpaperListSqlite )
            mWallpaperList.add(mWallpaper);

        Collections.shuffle(mWallpaperList);// for suffling the list
        gridview.setAdapter(adapter);

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


    private void updateWallpapers() {

        mWallpaperList.clear();
        mWallpaperListSqlite = sqdb.getCategoryWallpaper(categoryText);

        for (Wallpaper mWallpaper : mWallpaperListSqlite )
            mWallpaperList.add(mWallpaper);

        Collections.shuffle(mWallpaperList);
        adapter.notifyDataSetChanged();
        pullToRefresh.setRefreshing(false);

    }


}
