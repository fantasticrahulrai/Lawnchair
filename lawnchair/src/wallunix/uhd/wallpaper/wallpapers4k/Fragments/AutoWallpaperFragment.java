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


import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.RATE_ON_GOOGLE_PLAY;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.cancelAlarmQuote;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.checkAlarmQuote;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.isJobServiceOn;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.openUrl;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.setAlarmQuote;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.turnOffJobScheduler;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.turnOnJobScheduler;

import android.app.Activity;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.launcher3.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.util.List;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;
import wallunix.uhd.wallpaper.wallpapers4k.FullscreenActivity;
import wallunix.uhd.wallpaper.wallpapers4k.SettingActivity;


public class AutoWallpaperFragment extends Fragment {

    private ImageView imageView, info, setting;
    private LabeledSwitch autoWallSwitch, quoteSwitch;


    SharedPreferences mUserDetails;
    SharedPreferences.Editor mEditor;
    String todaysPicString;
    String defaultWallString= "No Info";
    Wallpaper mWallpaper;
    private boolean autoWallStatus, quoteStatus, jobRunning, alarmRunningQuote;
    private boolean hasLoadedOnce= false;

    private Activity activity;

    //*********job schedular*************
    JobScheduler mJobScheduler;


    public AutoWallpaperFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AutoWallpaperFragment newInstance(String param1, String param2) {
        AutoWallpaperFragment fragment = new AutoWallpaperFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        mUserDetails = getActivity().getSharedPreferences("UserDetails", 0); // 0 - for private mode
        todaysPicString=mUserDetails.getString("todayspick", defaultWallString);
        autoWallStatus=mUserDetails.getBoolean("autowall", false);
        quoteStatus=mUserDetails.getBoolean("dailyquote", true);
        jobRunning = isJobServiceOn(getActivity());

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String intervalPref = sharedPrefs.getString("frequencyPref", "Daily (Default)");

        final View v=inflater.inflate(R.layout.wallunix_fragment_autowallpaper, container, false);

        imageView= (ImageView)v.findViewById(R.id.imageView);
        info= (ImageView)v.findViewById(R.id.info);
        setting= (ImageView)v.findViewById(R.id.setting);

        autoWallSwitch = v.findViewById(R.id.switchAuto);
        quoteSwitch = v.findViewById(R.id.switchQuote);


        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoWallpaperInfoAd(getActivity());
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getActivity(), FullscreenActivity.class);
                i.putExtra("url", mWallpaper);
                startActivity(i);


            }
        });

        autoWallSwitch.setOn(jobRunning);
        autoWallSwitch.setOnToggledListener((labeledSwitch, isOn) -> {

            if(isOn){

                mEditor = mUserDetails.edit();
                mEditor.putBoolean("autowall", true );
                mEditor.apply();

                jobRunning = isJobServiceOn(getActivity());

                if(!jobRunning) {
                    turnOnJobScheduler(getActivity());

                    new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Auto Wallpaper On")
                            .setContentText("And set to "+ intervalPref)
                            .show();
                }


            }
            else if(!isOn) {
                mEditor = mUserDetails.edit();
                mEditor.putBoolean("autowall", false );
                mEditor.apply();


                jobRunning = isJobServiceOn(getActivity());

                if(jobRunning) {
                    turnOffJobScheduler(getActivity());
                    new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Auto Wallpaper Off")
                            .show();
                }


            }

        });


        //**************************
        quoteSwitch.setOnToggledListener((labeledSwitch, isOn) -> {

            if(isOn){

                mEditor = mUserDetails.edit();
                mEditor.putBoolean("dailyquote", true );
                mEditor.apply();

                alarmRunningQuote = checkAlarmQuote(getActivity());

                setAlarmQuote(getActivity());



            }
            else if(!isOn) {
                mEditor = mUserDetails.edit();
                mEditor.putBoolean("dailyquote", false );
                mEditor.apply();


                alarmRunningQuote = checkAlarmQuote(getActivity());

                if(alarmRunningQuote) {
                    cancelAlarmQuote(getActivity());
                }


            }

        });


        try {
            loadSpotlight();
        }
        catch (Exception e){

        }


           return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //Code executes EVERY TIME user views the fragment

        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isVisibleToUser && !hasLoadedOnce) {

                autoWallStatus=mUserDetails.getBoolean("autowall", false);

                autoWallSwitch.setOn(isJobServiceOn(getActivity()));
                quoteSwitch.setOn(checkAlarmQuote(getActivity()));

                Boolean showAutoWallAd = mUserDetails.getBoolean("showAuto", true);
                if(showAutoWallAd && !autoWallStatus)
                    autoWallpaperAd();


                hasLoadedOnce = true;
            }
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    public void autoWallpaperInfoAd(Context context){

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String sDuration= sharedPrefs.getString("frequencyPref", "NULL");
        String sJobRunning = "";

        mUserDetails = getActivity().getSharedPreferences("UserDetails", 0);
        String changedAt = mUserDetails.getString("changedAt", "Never");

        jobRunning = isJobServiceOn(context);

        if(jobRunning){
            sJobRunning = "On & Set to "+ sDuration;}
        else{
            sJobRunning = "Off";}



        new FancyGifDialog.Builder(getActivity())
                .setTitle("Auto Wallpaper is "+ sJobRunning)
                .setMessage("This feature is still in BETA. Sometimes auto wallpaper may not work properly because of " +
                        "Power Saver mode or slow internet connection."+
                        "\n"+"Last Changed at: "+changedAt+
                        "\n Changed for: "+ todaysPicString)
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Ok")
                .setGifResource(R.drawable.gif3)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {

                    }
                })
                .setNegativeBtnText("Feedback")
                .setNegativeBtnBackground("#FFA9A7A8")
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {

                        openUrl(getActivity(),RATE_ON_GOOGLE_PLAY);
                    }
                })
                .build();
    }

    public void autoWallpaperAd(){

        new FancyGifDialog.Builder(getActivity())
                .setTitle("New Wallpapers Every Morning")
                .setMessage("Get new wallpapers every morning on your home screen automatically, just stay connected to the internet and see the awesomeness")
                .setNegativeBtnText("Not Now")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Turn On")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.gif3)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {

                        turnOnJobScheduler(getActivity());

                        mEditor = mUserDetails.edit();
                        mEditor.putBoolean("autowall", true );
                        mEditor.apply();
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        mUserDetails = getActivity().getSharedPreferences("UserDetails", 0); // 0 - for private mode
                        mEditor = mUserDetails.edit();
                        mEditor.putBoolean("showAuto", false);
                        mEditor.apply();
                    }
                })
                .build();
    }


    private void loadSpotlight(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("spotlight");
        query.addDescendingOrder("createdAt");
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> wallpaperlist, ParseException e) {
                if (e == null) {
                    for (ParseObject w : wallpaperlist) {

                        mWallpaper = new Wallpaper(w.getString("name"), w.getString("thumbnail"), w.getString("wallpaper"),
                                w.getInt("downloads"), w.getString("category"), w.getString("firebaseid"));

                    }


                    RequestBuilder<Drawable> thumbnailRequest = Glide
                            .with(activity)
                            .load(mWallpaper.getThumbnail());

                    Glide
                            .with(activity)
                            .load(mWallpaper.getWallpaper())
                            .thumbnail(thumbnailRequest)
                            .placeholder(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                            .into(imageView);




                } else {

                }
            }
        });
    }


}
