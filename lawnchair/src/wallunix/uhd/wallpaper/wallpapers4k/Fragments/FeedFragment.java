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

package wallunix.uhd.wallpaper.wallpapers4k.Fragments;

import static com.parse.Parse.getApplicationContext;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.isAuth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.android.launcher3.R;
import com.cleveroad.androidmanimation.LoadingAnimationView;
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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import wallunix.uhd.wallpaper.wallpapers4k.Adapters.ListImageAdapter;
import wallunix.uhd.wallpaper.wallpapers4k.Adapters.RecyclerArtistSggestionAdapter;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Artist;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.ArtistFollowingSqlite;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;

public class FeedFragment extends Fragment {

    //private static ArrayList<Wallunix> mWallpaperList;
    //ListView listView;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    private static List<Artist> mArtistListSqlite;
    private List<Wallpaper> mWallpaperList;
    private ListView listView;


    SwipeRefreshLayout pullToRefresh;
    ListImageAdapter adapter;
    RecyclerArtistSggestionAdapter adapterSuggestion;
    private LoadingAnimationView animation;


    SharedPreferences mUserDetails;
    SharedPreferences.Editor mEditor;
    String autoWallString = "null wallpaper";
    Boolean autoWallStatus;

    Set<String> categorySet = new HashSet<String>();
    Set<String> defaultSet = new HashSet<String>();

    Set<String> artistSet = new HashSet<String>();

    private boolean hasLoadedOnce= false;

    ArtistFollowingSqlite sqdb;
    LinearLayout linearLayout;


    public FeedFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.wallunix_fragment_feed, container, false);

        mWallpaperList = new ArrayList<>();

        final ArtistFollowingSqlite sqdb = new ArtistFollowingSqlite(getActivity());
        mArtistListSqlite = sqdb.getAllArtist();

        for (Artist a : mArtistListSqlite )
            artistSet.add(a.getArtistid());


        defaultSet.add("Art");
        defaultSet.add("Fantasy");
        defaultSet.add("Superheroes");
        defaultSet.add("Space");
        defaultSet.add("Anime");
        defaultSet.add("Games");
        defaultSet.add("Movies");
        defaultSet.add("Animal");


        mUserDetails = getActivity().getSharedPreferences("UserDetails", 0); // 0 - for private mode
        autoWallStatus=mUserDetails.getBoolean("autowall", false);
        categorySet = mUserDetails.getStringSet("categorySet", defaultSet);



        listView = (ListView) v.findViewById(R.id.listview);
       /* pullToRefresh = v.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(false);

                updateWallpapers();

            }
        });*/

        //**************scrolling master code***********

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }



        animation = (LoadingAnimationView) v.findViewById(R.id.animation);
        animation.setVisibility(View.VISIBLE);
        animation.startAnimation();


        return v;


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isVisibleToUser && !hasLoadedOnce) {

                newFeed();


                Boolean showSnackbar = mUserDetails.getBoolean("showCustomization", true);
                if(showSnackbar) {

                    Snackbar snackbar = Snackbar
                            .make(listView, "This section contain exclusive wallpapers made by artists.", Snackbar.LENGTH_LONG);

                    snackbar.show();

                    mUserDetails = getActivity().getSharedPreferences("UserDetails", 0); // 0 - for private mode
                    mEditor = mUserDetails.edit();
                    mEditor.putBoolean("showCustomization", false);
                    mEditor.apply();
                }


                hasLoadedOnce = true;
            }
        }

    }


    private void updateWallpapers() {

        Collections.shuffle(mWallpaperList);
        adapter.notifyDataSetChanged();
        pullToRefresh.setRefreshing(false);

    }


    //******************reward ads**********************


    //******************reward ads**********************



    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void newFeed(){

        //***************************

        Boolean showSuggestion = mUserDetails.getBoolean("showCustomization", true);
        final int random = new Random().nextInt(2);

        if (random==1 || showSuggestion)
            loadArtistSuggestion();

        // Add a footer to the ListView
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup footer = (ViewGroup)inflater.inflate(R.layout.wallunix_feed_footer,listView,false);
        listView.addFooterView(footer);

        //**************************

        ParseQuery<ParseObject> query = ParseQuery.getQuery("wallpapers");
        query.addDescendingOrder("createdAt");
        query.setLimit(10000);
        query.whereExists("artistname");
        query.whereNotContainedIn("artistsid", artistSet);
       // query.whereContainedIn("category", categorySet);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> wallpaperlist, ParseException e) {
                if (e == null) {
                    for (ParseObject w : wallpaperlist) {

                        Wallpaper mWallpaper = new Wallpaper(w.getString("name"), w.getString("thumbnail"), w.getString("wallpaper"),
                                w.getInt("downloads"), w.getString("category"), w.getString("firebaseid"),
                                w.getString("artistsid"), w.getString("artistname"), w.getString("artistdp"));

                        //w.put("artistsid","RFURh3IkYL");
                        //w.put("artistname","Philipp Rietz");
                        //w.put("artistdp","https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/o/artists_dp%2FPhilipp%20Rietz.jpeg?alt=media&token=2eb3a678-8f38-4f98-8e2d-b72e461a23b8");
                        //w.saveInBackground();


                        mWallpaperList.add(mWallpaper);


                    }

                    Collections.shuffle(mWallpaperList);// for suffling the list
                    mWallpaperList.subList(100, mWallpaperList.size()).clear();// for getting only 100 shuffled wallpapers
                    followingFeed();


                } else {

                    Toast.makeText(getActivity(), "Server Error "+ e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void followingFeed(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("wallpapers");
        query.addDescendingOrder("createdAt");
        query.setLimit(10000);
        query.whereContainedIn("artistsid", artistSet);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> wallpaperlist, ParseException e) {
                if (e == null) {
                    for (ParseObject w : wallpaperlist) {

                        Wallpaper mWallpaper = new Wallpaper(w.getString("name"), w.getString("thumbnail"), w.getString("wallpaper"),
                                w.getInt("downloads"), w.getString("category"), w.getString("firebaseid"),
                                w.getString("artistsid"), w.getString("artistname"), w.getString("artistdp"));

                        mWallpaperList.add(mWallpaper);


                    }

                    Collections.shuffle(mWallpaperList);// for suffling the list
                    //mWallpaperList.subList(100, mWallpaperList.size()).clear();// for getting only 100 shuffled wallpapers
                    adapter = new ListImageAdapter(getActivity(),mWallpaperList);
                    listView.setAdapter(adapter);

                    animation.stopAnimation();
                    animation.setVisibility(View.INVISIBLE);



                } else {

                    Toast.makeText(getActivity(), "Server Error "+ e, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void loadArtistSuggestion(){


        // Add a header to the ListView
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.wallunix_feed_artist_suggestion,listView,false);
        listView.addHeaderView(header);


        linearLayout = (LinearLayout) header.findViewById(R.id.titlebar);
        final Button login = (Button) header.findViewById(R.id.buttonFollow);

        if(!isAuth(getApplicationContext())) {

            linearLayout.setVisibility(View.VISIBLE);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    signInAlert();

                }
            });

        }

        final RecyclerView recyclerView = (RecyclerView) header.findViewById(R.id.recyclerviewHorizontal);
        sqdb = new ArtistFollowingSqlite(getActivity());
        final List<Artist> mArtistListParse = new ArrayList<>();;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("artists");
        query.addDescendingOrder("createdAt");
        query.setLimit(100);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> artistlist, ParseException e) {
                if (e == null) {
                    for (ParseObject w : artistlist) {

                        Artist a  = new Artist(w.getObjectId(), w.getString("name"), w.getString("profileurl"));

                        if (!sqdb.checkFollowing(w.getObjectId()))
                             mArtistListParse.add(a);



                    }

                    Collections.shuffle(mArtistListParse);
                    adapterSuggestion = new RecyclerArtistSggestionAdapter(mArtistListParse);

                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    recyclerView.setAdapter(adapterSuggestion);


                    //**********suggestion shown on first run
                    mUserDetails = getActivity().getSharedPreferences("UserDetails", 0); // 0 - for private mode
                    mEditor = mUserDetails.edit();
                    mEditor.putBoolean("showCustomization", false);
                    mEditor.apply();



                } else {

                    // Toast.makeText(getActivity(), "Server Error "+ e, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public void signInAlert(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mAuth = FirebaseAuth.getInstance();

        new FancyGifDialog.Builder(getActivity())
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
                        Toast.makeText(getActivity(),"Connecting to Google...", Toast.LENGTH_SHORT).show();
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(getActivity(),"Plz Signin to unlock all features.", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
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
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
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

                            linearLayout.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(),"Welcome, we recommend following some artists.",
                                    Toast.LENGTH_LONG).show();


                        } else {

                        }

                    }
                });
    }

}
