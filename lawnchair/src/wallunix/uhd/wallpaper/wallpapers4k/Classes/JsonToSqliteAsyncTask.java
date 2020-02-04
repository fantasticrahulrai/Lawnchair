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

package wallunix.uhd.wallpaper.wallpapers4k.Classes;


import static wallunix.uhd.wallpaper.wallpapers4k.Fragments.LatestFragment.mWallpaperList;
import static wallunix.uhd.wallpaper.wallpapers4k.Fragments.LatestFragment.animation;
import static wallunix.uhd.wallpaper.wallpapers4k.Fragments.LatestFragment.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonToSqliteAsyncTask extends AsyncTask<String, String, String> {

    SharedPreferences mUserDetails, sharedPrefs;
    SharedPreferences.Editor mEditor;

    private String resp;
    private WeakReference<Context> contextRef;

    private static List<Wallpaper> mListFirstFew;
    private static List<Wallpaper> mListRest;




    public JsonToSqliteAsyncTask(Context context) {

        contextRef = new WeakReference<>(context);

    }


    @Override
    protected void onPreExecute() {

        Context mContext = contextRef.get();
        Toast.makeText(mContext, "Loading Wallpapers", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onProgressUpdate(String... text) {


    }

    @Override
    protected String doInBackground(String... params) {

        Context mContext = contextRef.get();
        createTableWallpaper(mContext);
        saveAndLoadSqlite(mContext);

        return resp;
    }

    @Override
    protected void onPostExecute(String result) {
        // execution of result of Long time consuming operation
        Context mContext = contextRef.get();
        updateUI(mContext);
        Toast.makeText(mContext, "Wallpapers loaded", Toast.LENGTH_SHORT).show();
    }


    //**************************JOSN to Sqlite methods*****************

    public String loadJSONFromAsset(Context context, String json) {

        try {
            InputStream is = context.getAssets().open(json);

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


    public List createTableWallpaper(Context context){

        final List<Wallpaper> mWallpaperListSqlite = new ArrayList<>();

        try {
            JSONObject mainObj = new JSONObject(loadJSONFromAsset(context, "wallpapers.json"));
            JSONArray editorsObj = mainObj.getJSONArray("results");
            JSONObject wallpaperObj;

            for (int i = 0; i < editorsObj.length(); ++i) {

                wallpaperObj = editorsObj.getJSONObject(i);

                if(wallpaperObj.length()< 10) {
                    Gson gson = new Gson();
                    Wallpaper w = gson.fromJson(wallpaperObj.toString(), Wallpaper.class);
                    mWallpaperListSqlite.add(w);
                }

            }

             createTableEditosChoice(context);

        } catch (JSONException e) {
            e.printStackTrace();

        }

        return mWallpaperListSqlite;
    }

    public List createTableEditosChoice(Context context){

        final List<Wallpaper> mWallpaperListSqlite = new ArrayList<>();
        String key ="";

        try {

            JSONObject mainObj = new JSONObject(loadJSONFromAsset(context, "editorschoice.json"));
            JSONArray editorsObj = mainObj.getJSONArray("results");
            JSONObject wallpaperObj;

            for (int i = 0; i < editorsObj.length(); ++i) {

                wallpaperObj = editorsObj.getJSONObject(i);


                    Gson gson = new Gson();
                    Wallpaper w = gson.fromJson(wallpaperObj.toString(), Wallpaper.class);
                    mWallpaperListSqlite.add(w);

            }



        } catch (JSONException e) {
            e.printStackTrace();

        }

        return mWallpaperListSqlite;
    }

    public List createTableLockscreen(Context context){

        final List<Wallpaper> mWallpaperListSqlite = new ArrayList<>();
        String key ="";

        try {
            JSONObject mainObj = new JSONObject(loadJSONFromAsset(context, "raiwallpaper.json"));
            JSONObject editorsObj = (JSONObject)mainObj.get("lockscreens");
            JSONObject wallpaperObj;

            Iterator<String> keyList = editorsObj.keys();

            while (keyList.hasNext()){
                key = keyList.next();
                wallpaperObj = (JSONObject)editorsObj.get(key);

                Gson gson = new Gson();
                Wallpaper mWallpaper = gson.fromJson(wallpaperObj.toString(), Wallpaper.class);
                mWallpaper.setId(key);

                mWallpaperListSqlite.add(mWallpaper);

            }




        } catch (JSONException e) {
            e.printStackTrace();

        }

        return mWallpaperListSqlite;
    }

    public List createTableNotch(Context context){

        final List<Wallpaper> mWallpaperListSqlite = new ArrayList<>();
        String key ="";

        try {
            JSONObject mainObj = new JSONObject(loadJSONFromAsset(context, "raiwallpaper.json"));
            JSONObject editorsObj = (JSONObject)mainObj.get("notches");
            JSONObject wallpaperObj;

            Iterator<String> keyList = editorsObj.keys();

            while (keyList.hasNext()){
                key = keyList.next();
                wallpaperObj = (JSONObject)editorsObj.get(key);

                Gson gson = new Gson();
                Wallpaper mWallpaper = gson.fromJson(wallpaperObj.toString(), Wallpaper.class);
                mWallpaper.setId(key);

                mWallpaperListSqlite.add(mWallpaper);

            }


        } catch (JSONException e) {
            e.printStackTrace();

        }

        return mWallpaperListSqlite;
    }



    public void saveAndLoadSqlite(Context context){

        OfflineWallpaperSqlite sqdb = new OfflineWallpaperSqlite(context);
        EditorsChoiceSqlite sqdb2 = new EditorsChoiceSqlite(context);

        sqdb.batchInsterstion(createTableWallpaper(context));
        sqdb2.batchInsterstion(createTableEditosChoice(context));


    }


    public void updateUI(Context context){

        //************ populating grid methods ****************

        OfflineWallpaperSqlite sqdb = new OfflineWallpaperSqlite(context);
        mWallpaperList = new ArrayList<>();

        for (Wallpaper mWallpaper : sqdb.getLatestWallpaper() )
            mWallpaperList.add(mWallpaper);

        mListFirstFew = mWallpaperList.subList(0, 50);
        Collections.shuffle(mListFirstFew);

        mListRest = mWallpaperList.subList(50, mWallpaperList.size());
        Collections.shuffle(mListRest);

        adapter.refreshMyList(mWallpaperList);
        animation.setVisibility(View.INVISIBLE);
        animation.stopAnimation();

        //**************** marking table as created**************
        mUserDetails = context.getSharedPreferences("UserDetails", 0); // 0 - for private mode
        mEditor = mUserDetails.edit();
        mEditor.putBoolean("tablecreated", true);
        mEditor.apply();
    }

}