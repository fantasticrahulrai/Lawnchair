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
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.RATE_ON_GOOGLE_PLAY;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.openUrl;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.android.launcher3.R;
import com.bumptech.glide.Glide;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.parse.ParseObject;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;
import wallunix.uhd.wallpaper.wallpapers4k.Adapters.ViewPagerAdapter;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.AutoWallpaperAsyncTask;

public class MainActivity extends AppCompatActivity {

    private Drawer result = null;
    private TabLayout tabLayout;
    ViewPagerAdapter adapterViewPager;
    ViewPager vpPager;
    Toolbar toolbar;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private IProfile profileDefault;
    AccountHeader headerResult;

    private InterstitialAd mInterstitialAd;

    SharedPreferences mUserDetails, sharedPrefs;
    SharedPreferences.Editor mEditor;
    Boolean autoWallStatus, firstRun, alarmRunning, showRating;

    boolean doubleBackToExitPressedOnce = false;

    String profilePic, name, email;

    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private AppUpdateManager appUpdateManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallunix_activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wallunix");

        mUserDetails = getApplicationContext().getSharedPreferences("UserDetails", 0); // 0 - for private mode
        autoWallStatus = mUserDetails.getBoolean("autowall", false);
        firstRun = mUserDetails.getBoolean("firstrun", true);

        profilePic = mUserDetails.getString("profilepic", DEFAULT_PIC);
        name = mUserDetails.getString("name", "Login");
        email = mUserDetails.getString("email", "Login to WallUnix");


        if (firstRun) {

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            // get the height and width of screen
            int height = metrics.heightPixels;
            int width = metrics.widthPixels;

            mEditor = mUserDetails.edit();
            mEditor.putInt("screenheight", height);
            mEditor.putInt("screenwidth", width);
            mEditor.putBoolean("firstrun", false);
            mEditor.apply();

            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            mEditor = sharedPrefs.edit();
            mEditor.putString("frequencyPref", "Daily (Default)");
            mEditor.apply();

            createNotificationChannel();


        }

        setUpUIs();
        setupDrawer();
        checkForUpdate();

    }


    @Override
    public void onStart(){
        super.onStart();


        //*********************************interstitial ads ***********************

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


    public void setUpUIs() {

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new ViewPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.setCurrentItem(2);
        vpPager.setOffscreenPageLimit(4);//TODO: increase the no. everytime adding a new fragment

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }

    public void setupDrawer(){

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).error(R.drawable.dp).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.with(imageView.getContext()).clear(imageView);
            }


        });

        profileDefault = new ProfileDrawerItem().withName(name).withEmail(email).withIcon(Uri.parse(profilePic));

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(profileDefault)
                .withTextColor(getResources().getColor(R.color.white))
                .withSelectionListEnabledForSingleProfile(false)
                .withHeaderBackground(R.drawable.drawerbg)
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {

                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));

                        return false;
                    }
                })
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withHasStableIds(true)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(false)
                .withDrawerLayout(R.layout.material_drawer_fits_not)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.home).withIcon(R.drawable.baseline_home_white_24dp).withIconTintingEnabled(true),
                        new PrimaryDrawerItem().withName("Categories").withIcon(R.drawable.categories).withIconTintingEnabled(true),

                        new SectionDrawerItem().withName("Wallpapers"),
                        new PrimaryDrawerItem().withName("Popular Wallpapers").withIcon(R.drawable.popular).withIconTintingEnabled(true),
                        new PrimaryDrawerItem().withName(R.string.notch).withIcon(R.drawable.drop).withIconTintingEnabled(true),
                        new PrimaryDrawerItem().withName("LockScreen Wallpapers").withIcon(R.drawable.cellphone_lock).withIconTintingEnabled(true),
                        new PrimaryDrawerItem().withName("Favourites").withIcon(R.drawable.baseline_favorite_white_24dp).withIconTintingEnabled(true),

                        new SectionDrawerItem().withName(R.string.application),
                        new PrimaryDrawerItem().withName("My Profile").withIcon(R.drawable.account).withIconTintingEnabled(true),
                        new PrimaryDrawerItem().withName(R.string.rate).withIcon(R.drawable.outline_star_white_24dp).withIconTintingEnabled(true),
                        new PrimaryDrawerItem().withName(R.string.settings).withIcon(R.drawable.baseline_settings_white_24dp).withIconTintingEnabled(true),
                        new PrimaryDrawerItem().withName(R.string.about).withIcon(R.drawable.outline_info_white_24dp).withIconTintingEnabled(true)
                        //new PrimaryDrawerItem().withName("Support").withIcon(R.drawable.whatsapp).withIconTintingEnabled(true)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (position==1){}
                            //onBackPressed();
                        else if (position==2)
                            startActivity(new Intent(MainActivity.this, CategoryActivity.class));
                        else if (position==4)
                            startActivity(new Intent(MainActivity.this, PopularActivity.class));
                        else if (position==5)
                            startActivity(new Intent(MainActivity.this, NotchWallpaperActivity.class));
                        else if (position==6)
                            startActivity(new Intent(MainActivity.this, LockscreenActivity.class));
                        else if (position==7)
                            startActivity(new Intent(MainActivity.this, FavouriteActivity.class));
                        else if (position==9)
                            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        else if (position==10)
                            openUrl(getApplicationContext(),RATE_ON_GOOGLE_PLAY);
                        else if (position==11)
                            startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        else if (position==12)
                            startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        else if (position==13)
                            openUrl(getApplicationContext(),"https://wa.me/918860406055");




                        return false;
                    }
                }).build();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        //outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                return true;

            case R.id.action_search:
                //simpleSearchView.showSearch();
                startActivity(new Intent(MainActivity.this, ExploreActivity.class));
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {


        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        }

       /* if (!result.isDrawerOpen() && !doubleBackToExitPressedOnce && showRating)
            showDialog();*/

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);


    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...


    }



    private void shuffleWallpapers(){

        Toast.makeText(getApplicationContext(), "Setting random wallpaper...", Toast.LENGTH_SHORT).show();

        AutoWallpaperAsyncTask task = new AutoWallpaperAsyncTask(this);
        task.execute();
    }

//***************************** Methods not so important *****************

    /*private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }*/



    public void showNoInternetAlert()
    {
        new FancyGifDialog.Builder(this)
                .setTitle("No Internet")
                .setMessage("Please check your internet connection and try again.")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Ok")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.gif1)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(MainActivity.this,"Ok", Toast.LENGTH_SHORT).show();
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(MainActivity.this,"Cancel", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();



    }

    public void upgradeAlert(){

        new FancyGifDialog.Builder(this)
                .setTitle("Upgrade To PRO")
                .setMessage("Upgrade to pro to enjoy exclusive perks, no Ads and early access to new features")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Ok")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.gif1)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(MainActivity.this,"Ok", Toast.LENGTH_SHORT).show();
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(MainActivity.this,"Cancel", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Wallpaper Changed";
            String description = "Channel for android 8+";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("James Bond", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showDialog() {

        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .threshold(3)
                .session(2)
                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {

                        try {
                            ParseObject gameScore = new ParseObject("Feedbacks");
                            gameScore.put("msg", feedback);
                            gameScore.saveInBackground();
                        }
                        catch (Exception e){}

                    }
                }).build();

        ratingDialog.show();
    }

    //****************In app update methods***************

    private void checkForUpdate(){
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.registerListener(installStateUpdatedListener);

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){


                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE, MainActivity.this, REQ_CODE_VERSION_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
                popupSnackbarForCompleteUpdate();
            } else {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_VERSION_UPDATE) {
            if (resultCode != RESULT_OK) {

            }
        }
    }

    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED){
                        popupSnackbarForCompleteUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED){
                        if (appUpdateManager != null){
                            appUpdateManager.unregisterListener(installStateUpdatedListener);
                        }

                    } else {

                    }
                }
            };


    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.coordinatorLayout_main),
                        "WallUnix has downloaded an update",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", view -> {
            if (appUpdateManager != null){
                appUpdateManager.completeUpdate();
            }
        });


        snackbar.setActionTextColor(getResources().getColor(R.color.primary_dark));
        snackbar.show();
    }




}







