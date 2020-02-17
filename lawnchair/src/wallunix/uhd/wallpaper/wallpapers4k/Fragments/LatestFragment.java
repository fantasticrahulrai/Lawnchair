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

package wallunix.uhd.wallpaper.wallpapers4k.Fragments;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.android.launcher3.R;
import com.cleveroad.androidmanimation.LoadingAnimationView;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wallunix.uhd.wallpaper.wallpapers4k.Adapters.ImageAdapter;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.FavouriteWallpaperSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.OfflineWallpaperSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;
import wallunix.uhd.wallpaper.wallpapers4k.WallpaperClickActivity;


public class LatestFragment extends Fragment {

    private OfflineWallpaperSqlite sqdb;
    private FavouriteWallpaperSqlite sqdb2;
    public static ArrayList<Wallpaper> mWallpaperList;
    private static List<Wallpaper> mWallpaperListSqlite;

    public static GridView gridview;
    SwipeRefreshLayout pullToRefresh;
    public static ImageAdapter adapter;
    public static LoadingAnimationView animation;

    SharedPreferences mUserDetails;
    SharedPreferences.Editor mEditor;
    String autoWallString = "null wallpaper";
    Boolean tableCreated;

    private boolean hasLoadedOnce= false;

    private static List<Wallpaper> mListFirstFew;
    private static List<Wallpaper> mListRest;


    public LatestFragment() {
        // Required empty public constructor
    }

    public static LatestFragment newInstance(String param1, String param2) {
        LatestFragment fragment = new LatestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.wallunix_fragment_latest, container, false);

        mWallpaperList = new ArrayList<>();
        adapter = new ImageAdapter(getActivity(), mWallpaperList);

        mUserDetails = getActivity().getSharedPreferences("UserDetails", 0); // 0 - for private mode
        tableCreated = mUserDetails.getBoolean("tablecreated", false);

        gridview = (GridView) v.findViewById(R.id.gridview);
        pullToRefresh = v.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateWallpapers();

            }
        });

        sqdb = new OfflineWallpaperSqlite(getActivity());
        sqdb2 = new FavouriteWallpaperSqlite(getActivity());

        gridview.setAdapter(adapter);

        animation = (LoadingAnimationView) v.findViewById(R.id.animation);
        animation.setVisibility(View.VISIBLE);
        animation.startAnimation();

        if (!tableCreated) {
            parseToSqlite();
        }
        else {
            addWallpaperToOffline();
        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Sending image id to FullScreenActivity
                final Wallpaper mWallpaper;
                mWallpaper = mWallpaperList.get(position);

                Intent i = new Intent(getActivity(), WallpaperClickActivity.class);
                // passing array index
                i.putExtra("url", mWallpaper);
                startActivity(i);


            }
        });



        //**************scrolling master code***********

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gridview.setNestedScrollingEnabled(true);
        }

        showDialog();

        return v;


    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isVisibleToUser && !hasLoadedOnce) {


                //***********************
                hasLoadedOnce = true;
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void addWallpaperToOffline(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("wallpapers");
        query.addDescendingOrder("name");
        query.whereDoesNotExist("artistname");
        query.setLimit(500);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> wallpaperlist, ParseException e) {
                if (e == null) {
                    for (ParseObject w : wallpaperlist) {

                        Wallpaper mWallpaper = new Wallpaper(w.getString("name"), w.getString("thumbnail"), w.getString("wallpaper"),
                                w.getInt("downloads"), w.getString("category"), w.getString("firebaseid"));

                        sqdb.addWallpapers(mWallpaper);

                    }

                    mWallpaperListSqlite = sqdb.getLatestWallpaper();

                    for (Wallpaper mWallpaper : mWallpaperListSqlite )
                        mWallpaperList.add(mWallpaper);

                    mListFirstFew = mWallpaperList.subList(0, 50);
                    Collections.shuffle(mListFirstFew);

                    mListRest = mWallpaperList.subList(50, mWallpaperList.size());

                    animation.setVisibility(View.INVISIBLE);
                    animation.stopAnimation();

                    gridview.setAdapter(adapter);



                } else {

                    Toast.makeText(getActivity(), "Server Error "+ e, Toast.LENGTH_SHORT).show();

                    mWallpaperListSqlite = sqdb.getLatestWallpaper();

                    for (Wallpaper mWallpaper : mWallpaperListSqlite )
                        mWallpaperList.add(mWallpaper);

                    mListFirstFew = mWallpaperList.subList(0, 50);
                    Collections.shuffle(mListFirstFew);

                    mListRest = mWallpaperList.subList(50, mWallpaperList.size());

                    animation.setVisibility(View.INVISIBLE);
                    animation.stopAnimation();

                    gridview.setAdapter(adapter);

                }
            }
        });

    }


    private void updateWallpapers() {

        mWallpaperList.clear();
        mWallpaperListSqlite = sqdb.getLatestWallpaper();

        for (Wallpaper mWallpaper : mWallpaperListSqlite )
            mWallpaperList.add(mWallpaper);

        mListFirstFew = mWallpaperList.subList(0, 50);
        Collections.shuffle(mListFirstFew);

        mListRest = mWallpaperList.subList(50, mWallpaperList.size());

        adapter.notifyDataSetChanged();
        pullToRefresh.setRefreshing(false);

    }

    private void showDialog() {

        final RatingDialog ratingDialog = new RatingDialog.Builder(getActivity())
                .threshold(3)
                .session(2)
                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {

                        try {
                            ParseObject gameScore = new ParseObject("Feedbacks");
                            gameScore.put("msg", feedback);
                            gameScore.saveInBackground();
                        }
                        catch (Exception e){}

                    }
                }).build();

        ratingDialog.show();
    }

    private void parseToSqlite(){

        final List<Wallpaper> mWallpaperListParse = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("wallpapers");
        query.addDescendingOrder("name");
        query.whereDoesNotExist("artistname");
        query.setLimit(10000);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> wallpaperlist, ParseException e) {
                if (e == null) {
                    for (ParseObject w : wallpaperlist) {

                        Wallpaper mWallpaper = new Wallpaper(w.getString("name"), w.getString("thumbnail"), w.getString("wallpaper"),
                                w.getInt("downloads"), w.getString("category"), w.getString("firebaseid"));

                        mWallpaperListParse.add(mWallpaper);
                    }

                    sqdb.batchInsterstion(mWallpaperListParse);


                    //************ populating grid methods ****************

                    mWallpaperListSqlite = sqdb.getLatestWallpaper();

                    for (Wallpaper mWallpaper : mWallpaperListSqlite )
                        mWallpaperList.add(mWallpaper);

                    mListFirstFew = mWallpaperList.subList(0, 50);
                    Collections.shuffle(mListFirstFew);

                    mListRest = mWallpaperList.subList(50, mWallpaperList.size());

                    animation.setVisibility(View.INVISIBLE);
                    animation.stopAnimation();

                    gridview.setAdapter(adapter);

                    //**************** marking table as created**************
                    mUserDetails = getActivity().getSharedPreferences("UserDetails", 0); // 0 - for private mode
                    mEditor = mUserDetails.edit();
                    mEditor.putBoolean("tablecreated", true);
                    mEditor.apply();


                } else {

                    Toast.makeText(getActivity(), "Oops...Server Error", Toast.LENGTH_SHORT).show();


                }
            }
        });

    }

}
