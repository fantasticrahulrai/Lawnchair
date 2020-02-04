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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
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
import wallunix.uhd.wallpaper.wallpapers4k.WallpaperClickArtistActivity;


public class ListImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Wallpaper> mWallpaperList;
    ListImageAdapter.ViewHolder viewHolder;


    static class ViewHolder {
        public ImageView thumbnail, profliepic;
        public TextView likecounter, title;
        public ImageView fav;
        public ImageView whatsapp;
        public Button follow;
    }
    // Constructor
    public ListImageAdapter(Context c) {
        mContext = c;
    }

    public ListImageAdapter(Context c, List<Wallpaper> mWallpaperList){

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
        int width = size. x;
        int height = size. y;

        final Wallpaper mWallpaper = mWallpaperList.get(position);
        final FavouriteWallpaperSqlite sqdb = new FavouriteWallpaperSqlite(mContext);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.wallunix_list_item, parent, false);
        }
        viewHolder = new ListImageAdapter.ViewHolder();


        viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.imageViewList);
        viewHolder.profliepic = (ImageView) convertView.findViewById(R.id.profile_image);
        viewHolder.likecounter = (TextView) convertView.findViewById(R.id.likecounter);
        viewHolder.title = (TextView) convertView.findViewById(R.id.title);
        viewHolder.fav = (ImageView) convertView.findViewById(R.id.liketooglebtn);
        viewHolder.whatsapp = (ImageView) convertView.findViewById(R.id.sharebtn);
        viewHolder.follow = (Button) convertView.findViewById(R.id.buttonFollow);


        viewHolder.thumbnail.getLayoutParams().width = width;
        viewHolder.thumbnail.getLayoutParams().height = width;



        viewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, WallpaperClickArtistActivity.class);
                i.putExtra("url", mWallpaper);

                i.putExtra("artistsid", mWallpaper.getArtistid());
                i.putExtra("artistname", mWallpaper.getArtistname());
                i.putExtra("artistdp", mWallpaper.getArtistdp());

                mContext.startActivity(i);
            }
        });


        viewHolder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sqdb.addWallpapers(mWallpaper);

                int likes = mWallpaper.getDownloads()+1;
                viewHolder.likecounter.setText(likes+"");
                likeCounter(mWallpaper);


            }
        });

        viewHolder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkPermission()) {
                    BitmapDrawable drawable = (BitmapDrawable) viewHolder.thumbnail.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    shareImageOnWhatsapp(bitmap);
                }
                else{
                    requestPermissions();


                }


            }
        });

        viewHolder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, ArtistProfileActivity.class);
                i.putExtra("artistsid", mWallpaper.getArtistid());
                i.putExtra("artistname", mWallpaper.getArtistname());
                i.putExtra("artistdp", mWallpaper.getArtistdp());
                mContext.startActivity(i);


            }
        });

        viewHolder.profliepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, ArtistProfileActivity.class);
                i.putExtra("artistsid", mWallpaper.getArtistid());
                i.putExtra("artistname", mWallpaper.getArtistname());
                i.putExtra("artistdp", mWallpaper.getArtistdp());
                mContext.startActivity(i);


            }
        });

        viewHolder.title.setOnClickListener(new View.OnClickListener() {
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
                .into(viewHolder.thumbnail);

        viewHolder.likecounter.setText(mWallpaper.getDownloads()+"");

        if(mWallpaper.getArtistname()!=null)
            viewHolder.title.setText(mWallpaper.getArtistname());
        else
            viewHolder.title.setText(mWallpaper.getCategory());


        Glide
                .with(mContext)
                .asBitmap()
                .centerCrop()
                .load(mWallpaper.getArtistdp())
                .placeholder(getRandomDrawbleColor())
                .error(R.drawable.dp)
                .into(viewHolder.profliepic);

        return convertView;
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
