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


import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.getImageUri;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.getRandomDrawbleColor;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.likeCounter;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.launcher3.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FileDownloadTask.TaskSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.FavouriteWallpaperSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.OfflineWallpaperSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;

public class WallpaperClickActivity extends AppCompatActivity {

    Wallpaper mWallpaper;
    Toolbar toolbar;

    FirebaseStorage storage;
    StorageReference storageRef;

    String wallurl, id;

    private InterstitialAd mInterstitialAd;

    SharedPreferences sharedPrefs, mUserDetails;
    SharedPreferences.Editor mEditor;

    private static ArrayList<Wallpaper> mWallpaperList;
    private static List<Wallpaper> mWallpaperListSqlite;
    private OfflineWallpaperSqlite sqdb;

    ImageView imageView2, imageView3, imageView4, imageView5, imageView6, imageView7;
    Button btnMore;

    SweetAlertDialog pDialogSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallunix_activity_wallpaper_click);

        ImageView imageView =(ImageView)findViewById(R.id.imageView);
        LinearLayout set=(LinearLayout) findViewById(R.id.SetWallpaper);
        LinearLayout save=(LinearLayout) findViewById(R.id.DownloadWallpaper);
        LinearLayout fav=(LinearLayout) findViewById(R.id.FavouriteWallpaper);

        LinearLayout linearLayoutWallpaperClick=(LinearLayout) findViewById(R.id.linearLayoutWallpaperClick);
        //**********

        imageView2 =(ImageView)findViewById(R.id.imageView2);
        imageView3 =(ImageView)findViewById(R.id.imageView3);
        imageView4 =(ImageView)findViewById(R.id.imageView4);
        imageView5 =(ImageView)findViewById(R.id.imageView5);
        imageView6 =(ImageView)findViewById(R.id.imageView6);
        imageView7 =(ImageView)findViewById(R.id.imageView7);

        btnMore = (Button)findViewById(R.id.btnMore);

        mWallpaper=(Wallpaper)getIntent().getSerializableExtra("url");

        //sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mUserDetails = getApplicationContext().getSharedPreferences("UserDetails", 0); // 0 - for private mode

        int width = mUserDetails.getInt("screenwidth",500);
        imageView.getLayoutParams().height = width+200; //previously 100

        //*********************

        wallurl=mWallpaper.getWallpaper();
        id=mWallpaper.getId();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl(mWallpaper.getWallpaper());

        final FavouriteWallpaperSqlite sqdb = new FavouriteWallpaperSqlite(this);


        //*********************************rewarded ads ***********************
        MobileAds.initialize(getApplicationContext(), getString(R.string.reward_ad_unit_id));
        MobileAds.setAppMuted(true);

        //****************************

        Boolean showSnackbar = mUserDetails.getBoolean("showFullscreen", true);
        if(showSnackbar) {

            Snackbar snackbar = Snackbar
                    .make(linearLayoutWallpaperClick, "Tap Once more for fullscreen", Snackbar.LENGTH_LONG)
                    .setAction("GOT IT", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            mUserDetails = getApplicationContext().getSharedPreferences("UserDetails", 0); // 0 - for private mode
                            mEditor = mUserDetails.edit();
                            mEditor.putBoolean("showFullscreen", false);
                            mEditor.apply();
                        }
                    });

            snackbar.show();
        }



        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                downloadWallpaperAndSet(storageRef);
                likeCounter(mWallpaper);

                if (mInterstitialAd.isLoaded())
                    mInterstitialAd.show();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermission()) {

                    downloadWallpaper(storageRef);
                    likeCounter(mWallpaper);

                    if (mInterstitialAd.isLoaded())
                        mInterstitialAd.show();

                }
                else{
                    requestPermissions();

                }
            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sqdb.addWallpapers(mWallpaper);
                likeCounter(mWallpaper);


            }
        });


        //**********more options*********
        set.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                downloadWallpaperAndSetMore(storageRef);
                likeCounter(mWallpaper);

                if (mInterstitialAd.isLoaded())
                    mInterstitialAd.show();

                return true;
            }
        });

        //****************loading the wallpaper************

        try {
            RequestBuilder<Drawable> thumbnailRequest = Glide
                    .with(this)
                    .load(mWallpaper.getThumbnail());

            Glide
                    .with(this)
                    .load(wallurl)
                    .thumbnail(thumbnailRequest)
                    .placeholder(getRandomDrawbleColor())
                    .error(R.drawable.ic_error)
                    .into(imageView);
        }
        catch (OutOfMemoryError e){

            Toast.makeText(this, "Loaded Smaller Size image because:\n "+e, Toast.LENGTH_SHORT).show();

            Glide
                    .with(this)
                    .load(mWallpaper.getThumbnail())
                    .placeholder(getRandomDrawbleColor())
                    .error(R.drawable.ic_error)
                    .into(imageView);
        }



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mWallpaper=(Wallpaper)getIntent().getSerializableExtra("url");
                Intent i = new Intent(getApplicationContext(), FullscreenActivity.class);
                i.putExtra("url", mWallpaper);
                startActivity(i);
            }
        });


    }



    @Override
    public void onStart(){
        super.onStart();

        try
        {
            populateGridView();
        }
        catch (Exception e){

        }

        //*********************************interstitial ads ***********************
       // MobileAds.initialize(this, new OnInitializationCompleteListener() {
         //   @Override
         //   public void onInitializationComplete(InitializationStatus initializationStatus) {}
        //});
       // MobileAds.setAppMuted(true);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        finish();// this refresh the images in the grid every time bcz activity is called agian

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_settings:
                // startSettings();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //**********permission methods start********

    public void requestPermissions(){

        ActivityCompat.requestPermissions(WallpaperClickActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                1);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED ;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    downloadWallpaper(storageRef);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(WallpaperClickActivity.this, "Permission denied to write your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    //**********permission method ends*************

    public void downloadWallpaper(StorageReference storageRef){

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Downloading...");
        pDialog.setCancelable(false);
        pDialog.show();


        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    final Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    // mImageView.setImageBitmap(bitmap);
                    saveImage(bitmap);
                    pDialog.dismiss();
                    //***************saving the pics if fetching is successful***********************************************************


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    pDialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    pDialog.setContentText("Downloaded "+(int)progress+"%");
                }
            });
        } catch (IOException e ) {

            Toast.makeText(getApplicationContext(), "Error loading wallpaper", Toast.LENGTH_SHORT).show();
        }

    }

    //*************
    public void downloadWallpaperAndSet(StorageReference storageRef){

        pDialogSet = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogSet.setTitleText("Setting Wallpaper...");
        pDialogSet.setCancelable(false);
        pDialogSet.show();

        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    final Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    // mImageView.setImageBitmap(bitmap);
                    setBitmap(bitmap);
                    //***************saving the pics if fetching is successful***********************************************************


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    pDialogSet.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    pDialogSet.setContentText("Downloaded "+(int)progress+"%");
                }
            })
            ;
        } catch (IOException e ) {

            Toast.makeText(getApplicationContext(), "Error setting wallpaper", Toast.LENGTH_SHORT).show();
        }

    }


    private void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File( root + "/WallUnix");
        if (!myDir.exists()) {
            boolean folderCreated = myDir.mkdir();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Wallunix-"+ n +".jpg";
        File file = new File(myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Toast.makeText(getApplicationContext(), "Wallpaper Downloaded", Toast.LENGTH_SHORT).show();

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error Saving file " + e, Toast.LENGTH_SHORT).show();
        }

        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });



    }


    public void setBitmap(Bitmap bitmap){


        //Intent wallpaperIntent = WallpaperManager.getInstance(this).getCropAndSetWallpaperIntent(getImageUri(this,bitmap));
        //startActivity(wallpaperIntent);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // get the height and width of screen
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;


        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());
        try {

            myWallpaperManager.suggestDesiredDimensions(width, height);
            myWallpaperManager.setBitmap(bitmap);

            Toast.makeText(getApplicationContext(), "Wallpaper Set", Toast.LENGTH_SHORT).show();
            pDialogSet.dismiss();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    //************************ more options method *********
    public void downloadWallpaperAndSetMore(StorageReference storageRef){

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Loading more options...");
        pDialog.setCancelable(false);
        pDialog.show();

        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    final Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    // mImageView.setImageBitmap(bitmap);
                    setBitmapMore(bitmap);
                    pDialog.dismiss();
                    //***************saving the pics if fetching is successful****************


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    pDialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    pDialog.setContentText("Downloaded "+(int)progress+"%");
                }
            })
            ;
        } catch (IOException e ) {

            Toast.makeText(getApplicationContext(), "Error setting wallpaper", Toast.LENGTH_SHORT).show();
        }

    }

    private void setBitmapMore(Bitmap bitmap){

        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.setDataAndType(getImageUri(this, bitmap), "image/*");
        startActivity(Intent.createChooser(intent, "Select Wallpaper"));
    }

    //*********************basic methods finished**********************

    public void populateGridView(){

        String category = mWallpaper.getCategory();

        sqdb = new OfflineWallpaperSqlite(this);
        mWallpaperListSqlite = sqdb.getRandomClickWallpaper(category);
        mWallpaperList = new ArrayList<>();


        for (Wallpaper mWallpaper : mWallpaperListSqlite ) {

            if (!id.equals(mWallpaper.getId()))
                mWallpaperList.add(mWallpaper);

        }

                Glide
                        .with(getApplicationContext())
                        .load(mWallpaperList.get(0).getThumbnail())
                        .placeholder(getRandomDrawbleColor())
                        .error(R.drawable.ic_error)
                        .centerCrop()
                        .into(imageView2);
                Glide
                        .with(getApplicationContext())
                        .load(mWallpaperList.get(1).getThumbnail())
                        .placeholder(getRandomDrawbleColor())
                        .error(R.drawable.ic_error)
                        .centerCrop()
                        .into(imageView3);
                Glide
                        .with(getApplicationContext())
                        .load(mWallpaperList.get(2).getThumbnail())
                        .placeholder(getRandomDrawbleColor())
                        .error(R.drawable.ic_error)
                        .centerCrop()
                        .into(imageView4);
                Glide
                        .with(getApplicationContext())
                        .load(mWallpaperList.get(3).getThumbnail())
                        .placeholder(getRandomDrawbleColor())
                        .error(R.drawable.ic_error)
                        .centerCrop()
                        .into(imageView5);
                Glide
                        .with(getApplicationContext())
                        .load(mWallpaperList.get(4).getThumbnail())
                        .placeholder(getRandomDrawbleColor())
                        .error(R.drawable.ic_error)
                        .centerCrop()
                        .into(imageView6);
                Glide
                        .with(getApplicationContext())
                        .load(mWallpaperList.get(5).getThumbnail())
                        .placeholder(getRandomDrawbleColor())
                        .error(R.drawable.ic_error)
                        .centerCrop()
                        .into(imageView7);


                imageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        mWallpaper = mWallpaperList.get(0);
                        Intent i = new Intent(getApplicationContext(), WallpaperClickActivity.class);
                        i.putExtra("url", mWallpaper);
                        startActivity(i);
                    }
                });

                imageView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        mWallpaper = mWallpaperList.get(1);
                        Intent i = new Intent(getApplicationContext(), WallpaperClickActivity.class);
                        i.putExtra("url", mWallpaper);
                        startActivity(i);
                    }
                });

                imageView4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mWallpaper = mWallpaperList.get(2);
                        Intent i = new Intent(getApplicationContext(), WallpaperClickActivity.class);
                        i.putExtra("url", mWallpaper);
                        startActivity(i);
                    }
                });

                imageView5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mWallpaper = mWallpaperList.get(3);
                        Intent i = new Intent(getApplicationContext(), WallpaperClickActivity.class);
                        i.putExtra("url", mWallpaper);
                        startActivity(i);
                    }
                });

                imageView6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mWallpaper = mWallpaperList.get(4);
                        Intent i = new Intent(getApplicationContext(), WallpaperClickActivity.class);
                        i.putExtra("url", mWallpaper);
                        startActivity(i);
                    }
                });

                imageView7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mWallpaper = mWallpaperList.get(5);
                        Intent i = new Intent(getApplicationContext(), WallpaperClickActivity.class);
                        i.putExtra("url", mWallpaper);
                        startActivity(i);
                    }
                });

                btnMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mWallpaper = mWallpaperList.get(6);
                        Intent i = new Intent(getApplicationContext(), WallpaperClickActivity.class);
                        i.putExtra("url", mWallpaper);
                        startActivity(i);
                    }
                });


    }

}
