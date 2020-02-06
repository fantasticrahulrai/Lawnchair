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
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import com.android.launcher3.R;
import com.cleveroad.androidmanimation.LoadingAnimationView;
import com.ferfalk.simplesearchview.SimpleSearchView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import wallunix.uhd.wallpaper.wallpapers4k.Adapters.ImageAdapterThree;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.OfflineWallpaperSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;

public class ExploreActivity extends AppCompatActivity {



    SwipeRefreshLayout pullToRefresh;
    private LoadingAnimationView animation;
    ImageAdapterThree adapter;
    GridView gridview;

    private OfflineWallpaperSqlite sqdb;
    private static ArrayList<Wallpaper> mWallpaperList;
    private static List<Wallpaper> mWallpaperListSqlite;

    Toolbar toolbar;
    private SimpleSearchView simpleSearchView;
    TagContainerLayout mTagContainerLayout;

    SharedPreferences mUserDetails;
    SharedPreferences.Editor mEditor;
    Set<String> categorySet = new HashSet<String>();
    Set<String> defaultSet = new HashSet<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallunix_activity_explore);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Explore");

        simpleSearchView = findViewById(R.id.searchView);
        simpleSearchView.showSearch(false);
        simpleSearchView.clearFocus();

        mWallpaperList = new ArrayList<>();

        adapter=new ImageAdapterThree(getApplicationContext(), mWallpaperList);
        gridview = (GridView)findViewById(R.id.gridview);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(false);

                 updateWallpaper();

            }
        });

        animation = (LoadingAnimationView)findViewById(R.id.animation);
        animation.setVisibility(View.VISIBLE);
        animation.startAnimation();

        defaultSet.add("Art");
        defaultSet.add("Fantasy");
        defaultSet.add("Superheroes");
        defaultSet.add("Anime");
        defaultSet.add("Movies");
        defaultSet.add("People");



        List<String> taglist = new ArrayList<String>();
        taglist.add("Blue");
        taglist.add("Motorcycle");
        taglist.add("Lamborghini");
        taglist.add("Yellow");


       // mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tagContainer);
       // mTagContainerLayout.setTags(taglist);


        mUserDetails = this.getSharedPreferences("UserDetails", 0); // 0 - for private mode
        categorySet = mUserDetails.getStringSet("categorySet", defaultSet);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Sending image id to FullScreenActivity
                Wallpaper mWallpaper;
                mWallpaper=mWallpaperList.get(position);


                Intent i = new Intent(getApplicationContext(),FullscreenActivity.class);
                // passing array index
                i.putExtra("url", mWallpaper);
                startActivity(i);


            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gridview.setNestedScrollingEnabled(true);
        }

        setupSearch();
        exploreFeed();


    }



    public void setupSearch(){


        simpleSearchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                searchParse(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               // Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextCleared() {
                Toast.makeText(getApplicationContext(), "No seach query", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        simpleSearchView.setOnSearchViewListener(new SimpleSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
               // Toast.makeText(getApplicationContext(), "Wallpaper beign set right now", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchViewClosed() {

            }

            @Override
            public void onSearchViewShownAnimation() {

            }

            @Override
            public void onSearchViewClosedAnimation() {

            }
        });

        //*******************tag click listener****************

      /*  mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

            @Override
            public void onTagClick(int position, String text) {
                searchParse(text);

            }

            @Override
            public void onTagLongClick(final int position, String text) {
                // ...
            }

            @Override
            public void onSelectedTagDrag(int position, String text){
                // ...
            }

            @Override
            public void onTagCrossClick(int position) {
                // ...
            }
        });*/
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        //outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (simpleSearchView.onActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;


            case R.id.action_search:
                simpleSearchView.showSearch();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {

       /* if (!result.isDrawerOpen() && !doubleBackToExitPressedOnce && showRating)
            showDialog();*/

       /* if (simpleSearchView.onBackPressed()) {
            return;
        }*/

        super.onBackPressed();

    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...


    }


    //*****************custom methods***********
    private void searchParse(String s){

        mWallpaperList.clear();

        animation.setVisibility(View.VISIBLE);
        animation.startAnimation();

        String cap = s.substring(0, 1).toUpperCase() + s.substring(1);


        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("wallpapers");
        query1.whereExists("tags");
        query1.whereStartsWith("tags", s);

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("wallpapers");
        query2.whereExists("tags");
        query2.whereStartsWith("tags", cap);

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> wallpaperlist, ParseException e) {
                if (e == null) {
                    for (ParseObject w : wallpaperlist) {

                        Wallpaper mWallpaper = new Wallpaper(w.getString("name"), w.getString("thumbnail"), w.getString("wallpaper"),
                                w.getInt("downloads"), w.getString("category"), w.getString("firebaseid"));

                           mWallpaperList.add(mWallpaper);


                    }

                    if (mWallpaperList.size()==0) {
                        showNoFavWalls();
                        animation.stopAnimation();
                        animation.setVisibility(View.INVISIBLE);
                        adapter.notifyDataSetChanged();
                    }

                    else {
                        animation.stopAnimation();
                        animation.setVisibility(View.INVISIBLE);
                        adapter.notifyDataSetChanged();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "Error Searching.."+ e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void exploreFeed(){

        sqdb = new OfflineWallpaperSqlite(getApplicationContext());
        mWallpaperListSqlite = sqdb.getLatestWallpaper();

        for (Wallpaper mWallpaper : mWallpaperListSqlite ) {

            for (String temp : categorySet) {

                if (mWallpaper.getCategory().equals(temp))
                    mWallpaperList.add(mWallpaper);

            }
        }

        Collections.shuffle(mWallpaperList);
        animation.stopAnimation();
        animation.setVisibility(View.INVISIBLE);
        gridview.setAdapter(adapter);

    }


    private void updateWallpaper(){

        Collections.shuffle(mWallpaperList);
        adapter.notifyDataSetChanged();
        pullToRefresh.setRefreshing(false);
    }

    public void showNoFavWalls(){

        new FancyGifDialog.Builder(this)
                .setTitle("Opps..")
                .setMessage("Plz refine your search: \n"+"Try different tags\n"+"Try popular and common tags")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Okay")
                .setGifResource(R.drawable.gif1)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        exploreFeed();
                    }
                })
                .setNegativeBtnText("Close Search")
                .setNegativeBtnBackground("#FFA9A7A8")
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        onBackPressed();
                    }
                })
                .build();
    }


}
