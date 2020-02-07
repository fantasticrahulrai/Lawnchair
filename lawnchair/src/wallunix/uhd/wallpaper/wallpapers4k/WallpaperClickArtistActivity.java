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
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.isAuth;
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
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;
import com.android.launcher3.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FileDownloadTask.TaskSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.ArtistFollowingSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.FavouriteWallpaperSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;

public class WallpaperClickArtistActivity extends AppCompatActivity {

    Wallpaper mWallpaper;
    Toolbar toolbar;

    FirebaseStorage storage;
    StorageReference storageRef;

    String wallurl, id;

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    SharedPreferences sharedPrefs, mUserDetails;
    SharedPreferences.Editor mEditor;

    private static ArrayList<Wallpaper> mWallpaperList;
    FavouriteWallpaperSqlite sqdb;
    private ArtistFollowingSqlite sqdb2;

    ImageView profliepic, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7;
    Button btnMore, btnFollow;

    String artistid, artistName, artistDp;
    TextView name, morefrom, bio;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallunix_activity_wallpaper_click_arist);

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

        sqdb = new FavouriteWallpaperSqlite(this);
        sqdb2 = new ArtistFollowingSqlite(this);


        //*********************************rewarded ads ***********************
        MobileAds.initialize(getApplicationContext(), getString(R.string.reward_ad_unit_id));
        MobileAds.setAppMuted(true);


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
            populateArtistDetail();
        }
        catch (Exception e){

        }

        //*********************************interstitial ads ***********************
       // MobileAds.initialize(this, new OnInitializationCompleteListener() {
       //     @Override
       //     public void onInitializationComplete(InitializationStatus initializationStatus) {}
       // });
      //  MobileAds.setAppMuted(true);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstital_ad_unit_artist));
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

        ActivityCompat.requestPermissions(WallpaperClickArtistActivity.this,
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
                    Toast.makeText(WallpaperClickArtistActivity.this, "Permission denied to write your External storage", Toast.LENGTH_SHORT).show();
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
                    saveImage(bitmap);
                    progressDialog.dismiss();
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
            });
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

    public void populateGridView(){


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
                Intent i = new Intent(getApplicationContext(), FullscreenActivity.class);
                i.putExtra("url", mWallpaper);
                startActivity(i);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mWallpaper = mWallpaperList.get(1);
                Intent i = new Intent(getApplicationContext(), FullscreenActivity.class);
                i.putExtra("url", mWallpaper);
                startActivity(i);
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mWallpaper = mWallpaperList.get(2);
                Intent i = new Intent(getApplicationContext(), FullscreenActivity.class);
                i.putExtra("url", mWallpaper);
                startActivity(i);
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mWallpaper = mWallpaperList.get(3);
                Intent i = new Intent(getApplicationContext(), FullscreenActivity.class);
                i.putExtra("url", mWallpaper);
                startActivity(i);
            }
        });

        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mWallpaper = mWallpaperList.get(4);
                Intent i = new Intent(getApplicationContext(), FullscreenActivity.class);
                i.putExtra("url", mWallpaper);
                startActivity(i);
            }
        });

        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mWallpaper = mWallpaperList.get(5);
                Intent i = new Intent(getApplicationContext(), FullscreenActivity.class);
                i.putExtra("url", mWallpaper);
                startActivity(i);
            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAuth(getApplicationContext())) {
                    Intent i = new Intent(getApplicationContext(), ArtistWallpaperActivity.class);
                    i.putExtra("artistsid", artistid);
                    i.putExtra("artistname", artistName);
                    startActivity(i);
                }

                else { signInAlert(); }
            }
        });


    }

    //************************Artist Detail Fetching *******************

    public void populateArtistDetail(){

        mWallpaperList = new ArrayList<>();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();


        artistid = (String)getIntent().getSerializableExtra("artistsid");
        artistName = (String)getIntent().getSerializableExtra("artistname");
        artistDp = (String)getIntent().getSerializableExtra("artistdp");

        name = (TextView)findViewById(R.id.textViewArtistName);
        bio = (TextView)findViewById(R.id.textViewBio);
        morefrom = (TextView)findViewById(R.id.morefrom);
        profliepic = (ImageView)findViewById(R.id.profile_image);

        btnFollow = (Button)findViewById(R.id.buttonFollow);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAuth(getApplicationContext()))
                {

                    if(!sqdb2.checkFollowing(artistid)) {

                        followArtist();
                    }

                    else if (sqdb2.checkFollowing(artistid)) {

                        unfollowArtist();

                    }

                }
                else {

                    signInAlert();
                }
            }
        });

        name.setText(artistName);
        morefrom.setText("More From "+artistName);
        if (sqdb2.checkFollowing(artistid)){
            btnFollow.setText("Unfollow");
        }

        Glide
                .with(getApplicationContext())
                .asBitmap()
                .centerCrop()
                .load(artistDp)
                .placeholder(getRandomDrawbleColor())
                .error(R.drawable.dp)
                .into(profliepic);



        ParseQuery<ParseObject> query = ParseQuery.getQuery("artists");
        query.getInBackground(artistid, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    bio.setText(object.getString("bio"));


                } else {

                }
            }
        });

        ParseQuery<ParseObject> queryWallpapers = ParseQuery.getQuery("wallpapers");
        queryWallpapers.whereEqualTo("artistsid",artistid);
        queryWallpapers.addDescendingOrder("downloads");
        queryWallpapers.setLimit(100);
        queryWallpapers.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> wallpaperlist, ParseException e) {
                if (e == null) {
                    for (ParseObject w : wallpaperlist) {

                        mWallpaper = new Wallpaper(w.getString("name"), w.getString("thumbnail"), w.getString("wallpaper"),
                                w.getInt("downloads"), w.getString("category"), w.getString("firebaseid"));

                        mWallpaperList.add(mWallpaper);

                    }

                    try {

                        Collections.shuffle(mWallpaperList);
                        populateGridView();

                    }
                    catch (Exception ex){

                    }



                } else {

                }
            }
        });
    }

    public void signInAlert(){

        new FancyGifDialog.Builder(this)
                .setTitle("SignIn Using Google")
                .setMessage("Login using Google for enjoying all features of WallUnix:"
                        +"\n"+"Follow your favourite artists"+"\n"+"Upload your own creation"+"\n"+"Unlock all the features")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("SignIn")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.gif20)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(getApplicationContext(),"Connecting to Google...", Toast.LENGTH_SHORT).show();
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(getApplicationContext(),"Plz Signin to unlock all features.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }


    public void followCounter(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("artists");
        query.getInBackground(artistid, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    object.increment("fans");
                    object.saveInBackground();


                } else {

                }
            }
        });
    }

    public void followArtist(){

        sqdb2.addArtist(artistid, artistName, artistDp);
        btnFollow.setText("Unfollow");
        followCounter();
        Toast.makeText(this,"You followed "+ artistName, Toast.LENGTH_SHORT).show();
        //adAlert();

    }

    public void unfollowArtist(){

        btnFollow.setText("Follow");
        sqdb2.removeArtist(artistid);
        Toast.makeText(this,"You unfollowed "+ artistName, Toast.LENGTH_SHORT).show();

    }

    public void adAlert(){

        new FancyGifDialog.Builder(this)
                .setTitle("Support "+artistName)
                .setMessage("Watch an Rewarded Ad and the money generated will go to the artist."
                        +"\n"+"Plz support the artist if you love their work.")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Watch Ad")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.gif9)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {

                       // loadRewardedVideo();

                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {

                    }
                })
                .build();
    }

    //**********************SignIn Methods *****************************

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            try {

                                FirebaseUser user = mAuth.getCurrentUser();
                                mEditor = mUserDetails.edit();
                                mEditor.putBoolean("signedin", true);
                                mEditor.putString("profilepic", user.getPhotoUrl().toString());
                                mEditor.putString("name", user.getDisplayName());
                                mEditor.putString("email", user.getEmail());
                                mEditor.apply();


                                ParseObject u = new ParseObject("userdetails");
                                u.put("name", user.getDisplayName());
                                u.put("email", user.getEmail());
                                u.put("dp", user.getPhotoUrl().toString());
                                u.saveInBackground();
                            }
                            catch (Exception e){}


                        } else {

                        }

                    }
                });
    }

}
