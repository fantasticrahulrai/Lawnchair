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

package wallunix.uhd.wallpaper.wallpapers4k.Adapters;


import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.getRandomDrawbleColor;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.android.launcher3.R;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;

public class ImageAdapterThree extends BaseAdapter {
    private Context mContext;
    private ArrayList<Wallpaper> mWallpaperList;
    ViewHolder viewHolder;

    //private Wallunix mWallpaper;
    static class ViewHolder {
        ImageView thumbnail;
    }
    // Constructor
    public ImageAdapterThree(Context c) {
        mContext = c;
    }

    public ImageAdapterThree(Context c, ArrayList<Wallpaper> mWallpaperList){

        mContext=c;
        this.mWallpaperList=mWallpaperList;

    }

    public int getCount() {

        int i= mWallpaperList.size();
        return i;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm. getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        int height = size. x/3;// size.y is the actual height


        final Wallpaper mWallpaper = mWallpaperList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.wallunix_grid_item, parent, false);
        }
        viewHolder = new ViewHolder();

        viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.imageView);
        //viewHolder.thumbnail.getLayoutParams().width = width;
        viewHolder.thumbnail.getLayoutParams().height = height;


        Glide
                .with(mContext)
                .asBitmap()
                .load(mWallpaper.getThumbnail())
                .placeholder(getRandomDrawbleColor())
                .error(R.drawable.ic_error)
                .centerCrop()
                .into(viewHolder.thumbnail);


        return convertView;
    }


    public void refreshMyList(ArrayList<Wallpaper> list){

        this.mWallpaperList.clear();
        this.mWallpaperList.addAll(list);
        this.notifyDataSetChanged();
    }


}