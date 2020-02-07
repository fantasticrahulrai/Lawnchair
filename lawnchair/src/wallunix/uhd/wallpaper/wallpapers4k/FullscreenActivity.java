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
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.ablanco.zoomy.Zoomy;
import com.android.launcher3.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FileDownloadTask.TaskSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.FavouriteWallpaperSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;

public class FullscreenActivity extends AppCompatActivity implements RewardedVideoAdListener {

    Wallpaper mWallpaper;
    Toolbar toolbar;

    FirebaseStorage storage;
    StorageReference storageRef;

    String wallurl;

    RewardedVideoAd mRewardedVideoAd;
    AdRequest adRequest;

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallunix_activity_fullscreen);


        ImageView imageView =(ImageView)findViewById(R.id.imageView);
        LinearLayout set=(LinearLayout) findViewById(R.id.SetWallpaper);
        LinearLayout save=(LinearLayout) findViewById(R.id.DownloadWallpaper);
        LinearLayout fav=(LinearLayout) findViewById(R.id.FavouriteWallpaper);

        mWallpaper=(Wallpaper)getIntent().getSerializableExtra("url");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean previewQualityLow = sharedPrefs.getBoolean("previewPref", true);

        wallurl=mWallpaper.getWallpaper();


        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl(mWallpaper.getWallpaper());

        Zoomy.Builder builder = new Zoomy.Builder(this).target(imageView);
        builder.register();


        final FavouriteWallpaperSqlite sqdb = new FavouriteWallpaperSqlite(this);


        //*********************************rewarded ads ***********************
        MobileAds.initialize(getApplicationContext(), getString(R.string.reward_ad_unit_id));
        MobileAds.setAppMuted(true);
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getApplicationContext());
        mRewardedVideoAd.setRewardedVideoAdListener(this);

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }



    @Override
    public void onStart(){
        super.onStart();

        //*********************************interstitial ads ***********************

      //  MobileAds.initialize(this, new OnInitializationCompleteListener() {
      //      @Override
      //      public void onInitializationComplete(InitializationStatus initializationStatus) {}
      //  });
      //  MobileAds.setAppMuted(true);

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

        finish();
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

        ActivityCompat.requestPermissions(FullscreenActivity.this,
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

                         if (mInterstitialAd.isLoaded())
                            mInterstitialAd.show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(FullscreenActivity.this, "Permission denied to write your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    //**********permission method ends*************

    public void downloadWallpaper(StorageReference storageRef){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Downloading...");
        progressDialog.show();


        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                   final Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                   // mImageView.setImageBitmap(bitmap);
                    progressDialog.dismiss();

                    saveImage(bitmap);
                    //setWallpaper(bitmap);
                                //***************saving the pics if fetching is successful***********************************************************


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Downloaded "+(int)progress+"%");
                }
            })
            ;
        } catch (IOException e ) {

            Toast.makeText(getApplicationContext(), "Error loading wallpaper", Toast.LENGTH_SHORT).show();
        }

    }

    //*************
    public void downloadWallpaperAndSet(StorageReference storageRef){


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Setting Wallpaper...");
        progressDialog.show();


        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    final Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    // mImageView.setImageBitmap(bitmap);
                    setBitmap(bitmap);
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Wallpaper Set", Toast.LENGTH_SHORT).show();
                    //***************saving the pics if fetching is successful***********************************************************


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Downloaded "+(int)progress+"%");
                }
            })
            ;
        } catch (IOException e ) {

            Toast.makeText(getApplicationContext(), "Error setting wallpaper", Toast.LENGTH_SHORT).show();
        }

    }


    private void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/WallUnix");
        if (!myDir.exists()) {
            myDir.mkdirs();
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
            out.flush();
            out.close();

            Toast.makeText(getApplicationContext(), "Wallpaper Downloaded", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error Saving file", Toast.LENGTH_SHORT).show();
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

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //************************ more options method *********
    public void downloadWallpaperAndSetMore(StorageReference storageRef){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading more options...");
        progressDialog.show();

        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    final Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    // mImageView.setImageBitmap(bitmap);
                    setBitmapMore(bitmap);
                    progressDialog.dismiss();
                    //***************saving the pics if fetching is successful****************


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Downloaded "+(int)progress+"%");
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

    //******************reward ads**********************

    private void loadRewardedVideo() {

        mRewardedVideoAd.loadAd(getString(R.string.reward_ad_unit_id),
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewarded(RewardItem reward) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {


    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {

    }

    @Override
    public void onRewardedVideoAdLoaded() {

        try {
            if (mRewardedVideoAd.isLoaded()) {
                mRewardedVideoAd.show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    //******************reward ads**********************

}