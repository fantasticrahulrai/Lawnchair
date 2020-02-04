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

import android.content.Intent;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import wallunix.uhd.wallpaper.wallpapers4k.Adapters.ImageAdapter;
import wallunix.uhd.wallpaper.wallpapers4k.AdminDetailActivity;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;


public class AdminWallpaperFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private static ArrayList<Wallpaper> mWallpaperList;
    ImageAdapter adapter;
    GridView gridview;
    SwipeRefreshLayout pullToRefresh;


    public AdminWallpaperFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminWallpaperFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminWallpaperFragment newInstance(String param1, String param2) {
        AdminWallpaperFragment fragment = new AdminWallpaperFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v= inflater.inflate(R.layout.wallunix_fragment_delete_admin, container, false);

        mWallpaperList = new ArrayList<>();
        gridview = (GridView) v.findViewById(R.id.gridview);
        pullToRefresh = v.findViewById(R.id.pullToRefresh);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //updateWallpaper();
                pullToRefresh.setRefreshing(false);
            }
        });

        adapter=new ImageAdapter(getActivity(), mWallpaperList);
        //gridview.setAdapter(adapter);

        // Read from the database


        ParseQuery<ParseObject> query = ParseQuery.getQuery("wallpapers");
        query.addDescendingOrder("createdAt");
        query.setLimit(10000);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> wallpaperlist, ParseException e) {
                if (e == null) {
                    for (ParseObject w : wallpaperlist) {

                        Wallpaper mWallpaper = new Wallpaper(w.getString("name"), w.getString("thumbnail"), w.getString("wallpaper"),
                                w.getInt("downloads"), w.getString("category"), w.getString("firebaseid"));

                        mWallpaperList.add(mWallpaper);

                    }
                    adapter=new ImageAdapter(getActivity(), mWallpaperList);
                    gridview.setAdapter(adapter);

                } else {

                    Toast.makeText(getActivity(), "Server Error "+ e, Toast.LENGTH_SHORT).show();
                }
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Sending image id to FullScreenActivity
                final Wallpaper mWallpaper;
                mWallpaper = mWallpaperList.get(position);

                Intent i = new Intent(getActivity(), AdminDetailActivity.class);
                // passing array index
                i.putExtra("url", mWallpaper);
                startActivity(i);


            }
        });

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {

                return false;
            }
        });



        return v;
    }



    @Override
    public void onDetach() {
        super.onDetach();

    }




}


