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


import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.getRandomDrawbleColor;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.isAuth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.launcher3.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;
import java.util.ArrayList;
import java.util.List;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.ArtistFollowingSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;

public class ArtistProfileActivity extends AppCompatActivity implements RewardedVideoAdListener {


    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;


    TextView name, fan, bio;
    ImageView profliepic, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7;
    Button btnFollow, btnMore;

    Context context;
    RewardedVideoAd mRewardedVideoAd;

    SharedPreferences mUserDetails, sharedPrefs;
    SharedPreferences.Editor mEditor;

    String artistid, artistName, artistDp;
    private static ArrayList<Wallpaper> mWallpaperList;
    Wallpaper mWallpaper;

    private ArtistFollowingSqlite sqdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallunix_activity_artist_profile);

        context = this;

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Artist");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        mUserDetails = getApplicationContext().getSharedPreferences("UserDetails", 0); // 0 - for private mode

        artistid = (String)getIntent().getSerializableExtra("artistsid");
        artistName = (String)getIntent().getSerializableExtra("artistname");
        artistDp = (String)getIntent().getSerializableExtra("artistdp");

        sqdb = new ArtistFollowingSqlite(this);

        mWallpaperList = new ArrayList<>();

        name = (TextView)findViewById(R.id.textViewArtistName);
        fan = (TextView)findViewById(R.id.textViewFan);
        bio = (TextView)findViewById(R.id.textViewBio);
        profliepic = (ImageView)findViewById(R.id.profile_image);

        imageView2 =(ImageView)findViewById(R.id.imageView2);
        imageView3 =(ImageView)findViewById(R.id.imageView3);
        imageView4 =(ImageView)findViewById(R.id.imageView4);
        imageView5 =(ImageView)findViewById(R.id.imageView5);
        imageView6 =(ImageView)findViewById(R.id.imageView6);
        imageView7 =(ImageView)findViewById(R.id.imageView7);

        btnFollow = (Button)findViewById(R.id.buttonFollow);
        btnMore = (Button)findViewById(R.id.btnMore);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAuth(getApplicationContext()))
                {

                    if(!sqdb.checkFollowing(artistid)) {

                        followArtist();
                    }

                    else if (sqdb.checkFollowing(artistid)) {

                      unfollowArtist();

                    }




                }
                else {

                    signInAlert();
                }
            }
        });


        //*********************************rewarded ads ***********************
        MobileAds.initialize(getApplicationContext(), getString(R.string.reward_ad_unit_id));
        MobileAds.setAppMuted(true);
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getApplicationContext());
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        if (sqdb.checkFollowing(artistid)){

            btnFollow.setText("Unfollow");

        }

        name.setText(artistName);

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

                    fan.setText("Fans: "+object.getInt("fans"));
                    bio.setText(object.getString("bio"));


                } else {

                }
            }
        });

        ParseQuery<ParseObject> queryWallpapers = ParseQuery.getQuery("wallpapers");
        queryWallpapers.whereEqualTo("artistsid",artistid);
        queryWallpapers.addDescendingOrder("downloads");
        queryWallpapers.setLimit(6);
        queryWallpapers.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> wallpaperlist, ParseException e) {
                if (e == null) {
                    for (ParseObject w : wallpaperlist) {

                        mWallpaper = new Wallpaper(w.getString("name"), w.getString("thumbnail"), w.getString("wallpaper"),
                                w.getInt("downloads"), w.getString("category"), w.getString("firebaseid"));

                        mWallpaperList.add(mWallpaper);

                    }

                    try {
                        populateGridView();
                    }
                    catch (Exception ex){

                    }



                } else {

                }
            }
        });





    }


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

                            followArtist();


                        } else {

                        }

                    }
                });
    }

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


    public void signInAlert(){

        new FancyGifDialog.Builder((Activity) context)
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
                        Toast.makeText(context,"Connecting to Google...", Toast.LENGTH_SHORT).show();
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(context,"Plz Signin to unlock all features.", Toast.LENGTH_SHORT).show();
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

        sqdb.addArtist(artistid, artistName, artistDp);
        btnFollow.setText("Unfollow");
        followCounter();
        Toast.makeText(context,"You followed "+ artistName, Toast.LENGTH_SHORT).show();
        adAlert();

    }

    public void unfollowArtist(){

        btnFollow.setText("Follow");
        sqdb.removeArtist(artistid);
        Toast.makeText(context,"You unfollowed "+ artistName, Toast.LENGTH_SHORT).show();

    }

    public void adAlert(){

        new FancyGifDialog.Builder((Activity) context)
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

                        loadRewardedVideo();

                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {

                    }
                })
                .build();
    }


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

        Toast.makeText(context,"Thanks for supporting "+ artistName, Toast.LENGTH_SHORT).show();

    }

    //******************reward ads**********************

}
