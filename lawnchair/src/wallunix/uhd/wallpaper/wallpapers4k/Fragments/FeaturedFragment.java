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


import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.likeCounter;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.share;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.shareWhatsapp;

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
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.samthompson.bubbleactions.BubbleActions;
import me.samthompson.bubbleactions.Callback;
import wallunix.uhd.wallpaper.wallpapers4k.Adapters.ImageAdapter;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.EditorsChoiceSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.FavouriteWallpaperSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;
import wallunix.uhd.wallpaper.wallpapers4k.WallpaperClickActivity;

public class FeaturedFragment extends Fragment {

    private EditorsChoiceSqlite sqdb;
    private FavouriteWallpaperSqlite sqdb2;
    private static ArrayList<Wallpaper> mWallpaperList;
    private static List<Wallpaper> mWallpaperListSqlite;

    private GridView gridview;
    SwipeRefreshLayout pullToRefresh;
    ImageAdapter adapter;
    private LoadingAnimationView animation;

    SharedPreferences mUserDetails;
    SharedPreferences.Editor mEditor;
    Boolean tableCreated;

    private boolean hasLoadedOnce= false;


    public FeaturedFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FeaturedFragment newInstance(String param1, String param2) {
        FeaturedFragment fragment = new FeaturedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.wallunix_fragment_lockscreen, container, false);

        mWallpaperList = new ArrayList<>();
        adapter = new ImageAdapter(getActivity(),mWallpaperList);

        mUserDetails = getActivity().getSharedPreferences("UserDetails", 0); // 0 - for private mode
        tableCreated = mUserDetails.getBoolean("editortablecreated", false);

        sqdb = new EditorsChoiceSqlite(getActivity());
        sqdb2 = new FavouriteWallpaperSqlite(getActivity());

        gridview = (GridView) v.findViewById(R.id.gridview);
        pullToRefresh = v.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(false);
                updateWallpapers();

            }
        });

        animation = (LoadingAnimationView) v.findViewById(R.id.animation);
        animation.setVisibility(View.VISIBLE);
        animation.startAnimation();

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


        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                final Wallpaper mWallpaper;
                mWallpaper = mWallpaperList.get(position);

                BubbleActions.on(v)
                        .addAction("Favourite", R.drawable.bubble_fav, new Callback() {
                            @Override
                            public void doAction() {
                                sqdb2.addWallpapers(mWallpaper);
                                likeCounter(mWallpaper);
                            }
                        })
                        .addAction("Share", R.drawable.bubble_share, new Callback() {
                            @Override
                            public void doAction() {

                                share(getActivity(), "Hey found an awesome wallpaper:"+mWallpaper.getWallpaper());

                            }
                        })
                        .addAction("WhatsApp", R.drawable.bubble_whatsapp, new Callback() {
                            @Override
                            public void doAction() {

                                shareWhatsapp(getActivity(), "Hey found an awesome wallpaper:"+mWallpaper.getWallpaper());
                            }
                        })
                        .show();

                return false;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gridview.setNestedScrollingEnabled(true);
        }

        return v;

    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isVisibleToUser && !hasLoadedOnce) {

                if (!tableCreated) {
                    parseToSqlite();
                }

                if (tableCreated) {
                    addWallpaperToOffline();
                }

                //*****************************
                hasLoadedOnce = true;
            }
        }

    }

    private void addWallpaperToOffline(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("editorschoice");
        query.addDescendingOrder("createdAt");
        query.setLimit(200);
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

                    Collections.shuffle(mWallpaperList);
                    animation.setVisibility(View.INVISIBLE);
                    animation.stopAnimation();
                    gridview.setAdapter(adapter);


                } else {

                    Toast.makeText(getActivity(), "Server Error "+ e, Toast.LENGTH_SHORT).show();

                    mWallpaperListSqlite = sqdb.getLatestWallpaper();

                    for (Wallpaper mWallpaper : mWallpaperListSqlite )
                        mWallpaperList.add(mWallpaper);

                    Collections.shuffle(mWallpaperList);
                    animation.setVisibility(View.INVISIBLE);
                    animation.stopAnimation();
                    gridview.setAdapter(adapter);
                }
            }
        });
    }


    private void updateWallpapers() {


        mWallpaperList.clear();

        sqdb = new EditorsChoiceSqlite(getActivity());
        mWallpaperListSqlite = sqdb.getLatestWallpaper();

        for (Wallpaper mWallpaper : mWallpaperListSqlite )
            mWallpaperList.add(mWallpaper);

        Collections.shuffle(mWallpaperList);// for suffling the list
        adapter.notifyDataSetChanged();
        pullToRefresh.setRefreshing(false);

    }

    private void parseToSqlite(){

        final List<Wallpaper> mWallpaperListParse = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("editorschoice");
        query.addDescendingOrder("name");
        query.whereDoesNotExist("artistname");
        query.setLimit(1000);

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

                    Collections.shuffle(mWallpaperList);
                    animation.setVisibility(View.INVISIBLE);
                    animation.stopAnimation();
                    gridview.setAdapter(adapter);

                    //**************** marking table as created**************
                    mUserDetails = getActivity().getSharedPreferences("UserDetails", 0); // 0 - for private mode
                    mEditor = mUserDetails.edit();
                    mEditor.putBoolean("editortablecreated", true);
                    mEditor.apply();


                } else {

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...Server Error")
                            .setContentText("Please check your internet connection")
                            .show();

                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }



}
