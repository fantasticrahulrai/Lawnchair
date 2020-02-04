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
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.launcher3.R;
import com.bumptech.glide.Glide;
import java.util.List;
import wallunix.uhd.wallpaper.wallpapers4k.ArtistProfileActivity;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Artist;


public class RecyclerArtistSggestionAdapter extends RecyclerView.Adapter<RecyclerArtistSggestionAdapter.ArtistViewHolder>{

    private List<Artist> mArtistList;
    private Context mContext;

    //*********Google SignIn Variables*********


    public RecyclerArtistSggestionAdapter(List<Artist> mArtistList) {
        this.mArtistList = mArtistList;


    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallunix_list_item_artist_suggestion_horizontal, parent, false);

        mContext=parent.getContext();

        return new ArtistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ArtistViewHolder holder, int position) {


        final Artist a = mArtistList.get(position);


        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, ArtistProfileActivity.class);
                i.putExtra("artistsid", a.getArtistid());
                i.putExtra("artistname", a.getArtistname());
                i.putExtra("artistdp", a.getArtistdp());
                mContext.startActivity(i);


            }
        });

        holder.profliepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, ArtistProfileActivity.class);
                i.putExtra("artistsid", a.getArtistid());
                i.putExtra("artistname", a.getArtistname());
                i.putExtra("artistdp", a.getArtistdp());
                mContext.startActivity(i);


            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent i = new Intent(mContext, ArtistProfileActivity.class);
                i.putExtra("artistsid", a.getArtistid());
                i.putExtra("artistname", a.getArtistname());
                i.putExtra("artistdp", a.getArtistdp());
                mContext.startActivity(i);


            }
        });





        Glide
                .with(mContext)
                .asBitmap()
                .centerCrop()
                .load(a.getArtistdp())
                .placeholder(getRandomDrawbleColor())
                .error(R.drawable.dp)
                .into(holder.profliepic);

        holder.title.setText(a.getArtistname());


    }

    @Override
    public int getItemCount() {
        return mArtistList.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {

        public ImageView profliepic;
        public TextView title;
        public Button follow;

        public ArtistViewHolder(View view) {
            super(view);

            profliepic = (ImageView)view.findViewById(R.id.profile_image);
            title = (TextView) view.findViewById(R.id.textViewArtistName);
            follow=(Button) view.findViewById(R.id.buttonFollow);
        }
    }



}

