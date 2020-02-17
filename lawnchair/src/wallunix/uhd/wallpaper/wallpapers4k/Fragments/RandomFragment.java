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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.android.launcher3.R;
import com.cleveroad.androidmanimation.LoadingAnimationView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import wallunix.uhd.wallpaper.wallpapers4k.Adapters.ImageAdapter;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.FavouriteWallpaperSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.OfflineWallpaperSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;
import wallunix.uhd.wallpaper.wallpapers4k.WallpaperClickActivity;


public class RandomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LoadingAnimationView animation;

    private OfflineWallpaperSqlite sqdb;
    private FavouriteWallpaperSqlite sqdb2;
    private static ArrayList<Wallpaper> mWallpaperList;
    private static List<Wallpaper> mWallpaperListSqlite;

    ImageAdapter adapter;
    GridView gridview;
    SwipeRefreshLayout pullToRefresh;


    private boolean hasLoadedOnce= false;


    public RandomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RandomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RandomFragment newInstance(String param1, String param2) {
        RandomFragment fragment = new RandomFragment();
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
        final View v = inflater.inflate(R.layout.wallunix_fragment_best, container, false);

        mWallpaperList = new ArrayList<>();
        adapter=new ImageAdapter(getActivity(), mWallpaperList);
        gridview = (GridView) v.findViewById(R.id.gridview);
        pullToRefresh = v.findViewById(R.id.pullToRefresh);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateWallpaper();
            }
        });


        animation = (LoadingAnimationView) v.findViewById(R.id.animation);
        animation.setVisibility(View.VISIBLE);
        animation.startAnimation();


        /**
         * On Click event for Single Gridview Item
         * */


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Sending image id to FullScreenActivity
                Wallpaper mWallpaper;
                mWallpaper=mWallpaperList.get(position);


                Intent i = new Intent(getActivity(), WallpaperClickActivity.class);
                // passing array index
                i.putExtra("url", mWallpaper);
                startActivity(i);


            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gridview.setNestedScrollingEnabled(true);
        }


        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isVisibleToUser && !hasLoadedOnce) {

                sqdb = new OfflineWallpaperSqlite(getActivity());
                sqdb2 = new FavouriteWallpaperSqlite(getActivity());

                mWallpaperListSqlite = sqdb.getLatestWallpaper();

                for (Wallpaper mWallpaper : mWallpaperListSqlite )
                    mWallpaperList.add(mWallpaper);

                        Collections.shuffle(mWallpaperList);
                        animation.stopAnimation();
                        animation.setVisibility(View.INVISIBLE);
                        gridview.setAdapter(adapter);


                //*************************
                hasLoadedOnce = true;
            }
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void updateWallpaper(){

        mWallpaperList.clear();
        mWallpaperListSqlite = sqdb.getAllWallpapers();

        for (Wallpaper mWallpaper : mWallpaperListSqlite )
            mWallpaperList.add(mWallpaper);

        Collections.shuffle(mWallpaperList);
        adapter.notifyDataSetChanged();
        pullToRefresh.setRefreshing(false);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
