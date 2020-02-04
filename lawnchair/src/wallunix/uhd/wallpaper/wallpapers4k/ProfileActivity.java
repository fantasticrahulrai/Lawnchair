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


import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.DEFAULT_PIC;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.getRandomDrawbleColor;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.isAuth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.launcher3.R;
import com.bumptech.glide.Glide;
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
import com.parse.ParseObject;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

public class ProfileActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    TextView name, email, artist, favourites, downloads;
    Button btnLogin, btnMore;
    ImageView profliepic, iartist, ifavourites, idownloads;

    SharedPreferences mUserDetails, sharedPrefs;
    SharedPreferences.Editor mEditor;

    String sName, sEmail, sDp;

    Context context;

    ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallunix_activity_profile);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);


        context = this;

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My profile");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        mUserDetails = getApplicationContext().getSharedPreferences("UserDetails", 0); // 0 - for private mode

        sDp = mUserDetails.getString("profilepic", DEFAULT_PIC);
        sName = mUserDetails.getString("name", "Login");
        sEmail = mUserDetails.getString("email", "Login to WallUnix");

        name = (TextView)findViewById(R.id.textViewName);
        email = (TextView)findViewById(R.id.textViewEmail);
        profliepic = (ImageView)findViewById(R.id.profile_image);

        artist = (TextView)findViewById(R.id.textView1);
        favourites = (TextView)findViewById(R.id.textView2);
        downloads = (TextView)findViewById(R.id.textView3);

        iartist = (ImageView)findViewById(R.id.imageView8);
        ifavourites = (ImageView)findViewById(R.id.imageView9);
        idownloads = (ImageView)findViewById(R.id.imageView10);


        btnLogin = (Button)findViewById(R.id.buttonLogin);


        Glide
                .with(getApplicationContext())
                .asBitmap()
                .centerCrop()
                .load(sDp)
                .placeholder(getRandomDrawbleColor())
                .error(R.drawable.dp)
                .into(profliepic);


        if(isAuth(getApplicationContext()))
        {
            name.setText(sName);
            email.setText(sEmail);
            btnLogin.setVisibility(View.INVISIBLE);

            artist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(ProfileActivity.this, ArtistFollowingActivity.class));

                }
            });

            favourites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(ProfileActivity.this, FavouriteActivity.class));
                }
            });

            downloads.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   uploadAlert();
                }
            });
        }
        else {

            artist.setVisibility(View.INVISIBLE);
            iartist.setVisibility(View.INVISIBLE);
            favourites.setVisibility(View.INVISIBLE);
            ifavourites.setVisibility(View.INVISIBLE);
            downloads.setVisibility(View.INVISIBLE);
            idownloads.setVisibility(View.INVISIBLE);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    signInAlert();
                }
            });
        }

    }

    @Override
    public void onStart() {
        super.onStart();

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

                            finish();
                            startActivity(getIntent());


                        } else {

                        }

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

    public void uploadAlert(){

        new FancyGifDialog.Builder((Activity) context)
                .setTitle("Upload your creations")
                .setMessage("Send us your few best creation and wait for our approval:"
                        +"\n"+"Once approved you are a part of our exclusive team")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Send Mail")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.gif21)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {

                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:rahul@wallunix.com"));
                        intent.putExtra(Intent.EXTRA_EMAIL, "rahul@wallunix.com");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Wallunix");
                        startActivity(Intent.createChooser(intent, "E-Mail"));

                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {

                    }
                })
                .build();
    }

}
