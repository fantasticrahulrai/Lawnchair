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

import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.RATE_ON_GOOGLE_PLAY;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.getRandomDrawbleColor;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.likeCounter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.launcher3.R;
import com.bumptech.glide.Glide;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import wallunix.uhd.wallpaper.wallpapers4k.ArtistProfileActivity;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.FavouriteWallpaperSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;
import wallunix.uhd.wallpaper.wallpapers4k.WallpaperClickActivity;

public class RecyclerImageAdapter extends RecyclerView.Adapter<RecyclerImageAdapter.WallpaperViewHolder>{

        private List<Wallpaper> mWallpaperList;
        private Context mContext;
        String profliePicString;

        //*********Google SignIn Variables*********


        public RecyclerImageAdapter(List<Wallpaper> mWallpaperList) {
                this.mWallpaperList = mWallpaperList;


        }

        @Override
        public WallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.wallunix_list_item, parent, false);

                mContext=parent.getContext();

                return new WallpaperViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final WallpaperViewHolder holder, int position) {

                WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm. getDefaultDisplay();
                Point size = new Point();
                display. getSize(size);

                int width = size. x;
                int height = size. y;

                final Wallpaper mWallpaper = mWallpaperList.get(position);
                final FavouriteWallpaperSqlite sqdb = new FavouriteWallpaperSqlite(mContext);

                String artistname = "Rai Studios";

                //********************

                holder.thumbnail.getLayoutParams().width = width;
                holder.thumbnail.getLayoutParams().height = width;

                holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                Intent i = new Intent(mContext, WallpaperClickActivity.class);
                                i.putExtra("url", mWallpaper);
                                mContext.startActivity(i);
                        }
                });


                holder.fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                sqdb.addWallpapers(mWallpaper);

                                int likes = mWallpaper.getDownloads()+1;
                                holder.likecounter.setText(likes+"");
                                likeCounter(mWallpaper);


                        }
                });

                holder.whatsapp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                                if (checkPermission()) {
                                        BitmapDrawable drawable = (BitmapDrawable) holder.thumbnail.getDrawable();
                                        Bitmap bitmap = drawable.getBitmap();
                                        shareImageOnWhatsapp(bitmap);
                                }
                                else{
                                        requestPermissions();


                                }


                        }
                });

                holder.follow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                Intent i = new Intent(mContext, ArtistProfileActivity.class);
                                i.putExtra("artistsid", mWallpaper.getArtistid());
                                i.putExtra("artistname", mWallpaper.getArtistname());
                                i.putExtra("artistdp", mWallpaper.getArtistdp());
                                mContext.startActivity(i);


                        }
                });

                holder.profliepic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                Intent i = new Intent(mContext, ArtistProfileActivity.class);
                                i.putExtra("artistsid", mWallpaper.getArtistid());
                                i.putExtra("artistname", mWallpaper.getArtistname());
                                i.putExtra("artistdp", mWallpaper.getArtistdp());
                                mContext.startActivity(i);


                        }
                });

                holder.title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {



                                Intent i = new Intent(mContext, ArtistProfileActivity.class);
                                i.putExtra("artistsid", mWallpaper.getArtistid());
                                i.putExtra("artistname", mWallpaper.getArtistname());
                                i.putExtra("artistdp", mWallpaper.getArtistdp());
                                mContext.startActivity(i);


                        }
                });




                Glide
                        .with(mContext)
                        .asBitmap()
                        .load(mWallpaper.getThumbnail())
                        .centerCrop()
                        .placeholder(getRandomDrawbleColor())
                        .error(R.drawable.ic_error)
                        .into(holder.thumbnail);


                holder.likecounter.setText(mWallpaper.getDownloads()+"");

                //long date =  -1*-1*new Date().getTime();
                //long t = -1* Long.parseLong(mWallpaper.getName());
                //Date d = new Date(TimeUnit.SECONDS.toMillis(t));

                if(mWallpaper.getArtistname()!=null)
                        holder.title.setText(mWallpaper.getArtistname());
                else
                        holder.title.setText(mWallpaper.getCategory());

                //profliePicString = mWallpaper.getCategory().toLowerCase()+"bg";
                //.load(mContext.getResources().getIdentifier(profliePicString, "drawable", mContext.getPackageName()))



                Glide
                        .with(mContext)
                        .asBitmap()
                        .centerCrop()
                        .load(mWallpaper.getArtistdp())
                        .placeholder(getRandomDrawbleColor())
                        .error(R.drawable.dp)
                        .into(holder.profliepic);


        }

        @Override
        public int getItemCount() {
                return mWallpaperList.size();
        }

        public class WallpaperViewHolder extends RecyclerView.ViewHolder {

                public ImageView thumbnail, profliepic;
                public TextView likecounter, title;
                public ImageView fav;
                public ImageView whatsapp;
                public Button follow;

                public WallpaperViewHolder(View view) {
                        super(view);

                        thumbnail = (ImageView) view.findViewById(R.id.imageViewList);
                        profliepic = (ImageView)view.findViewById(R.id.profile_image);
                        likecounter = (TextView) view.findViewById(R.id.likecounter);
                        title = (TextView) view.findViewById(R.id.title);
                        fav=(ImageView) view.findViewById(R.id.liketooglebtn);
                        whatsapp=(ImageView) view.findViewById(R.id.sharebtn);
                        follow=(Button) view.findViewById(R.id.buttonFollow);
                }
        }


        //************miselaneous methods***************

        public Uri getImageUri(Context inContext, Bitmap inImage) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
                return Uri.parse(path);
        }

        public void shareImageOnWhatsapp(Bitmap bitmap){

                Uri imgUri = getImageUri(mContext,bitmap);
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "For more awesome wallpapers like this visit: "+RATE_ON_GOOGLE_PLAY);
                whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
                whatsappIntent.setType("image/jpeg");
                whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                        mContext.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(mContext, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }

                File f = new File(imgUri.getPath());
                f.delete();

        }



        //**********permission methods start********

        public void requestPermissions(){

                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                        1);
        }

        private boolean checkPermission() {
                int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                return result == PackageManager.PERMISSION_GRANTED ;
        }


        public void onRequestPermissionsResult(int requestCode,
                                               String permissions[], int[] grantResults) {
                switch (requestCode) {
                        case 1: {

                                // If request is cancelled, the result arrays are empty.
                                if (grantResults.length > 0
                                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                                        Toast.makeText(mContext, "Now, click the button to share on Whatsapp", Toast.LENGTH_SHORT).show();

                                } else {

                                        // permission denied, boo! Disable the
                                        // functionality that depends on this permission.
                                        Toast.makeText(mContext, "Permission needed for sharing images", Toast.LENGTH_SHORT).show();
                                }
                                return;
                        }

                        // other 'case' lines to check for other
                        // permissions this app might request
                }
        }


        //**********permission method ends*************

}
