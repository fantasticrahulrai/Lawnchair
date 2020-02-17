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


import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.RATE_ON_GOOGLE_PLAY;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.launcher3.R;


public class AboutActivity extends AppCompatActivity implements View.OnClickListener {


    private static String GITHUB = "https://github.com/fantasticrahulrai";

    private static String TWITTER = "https://twitter.com/fantastic_rahul";
    private static String INSTAGRAM = "https://www.instagram.com/wallunix/";
    private static String WEBSITE = "https://wallunix.com";
    private static String POLICY = "https://thecoolkingrahul.wixsite.com/temp/privacy-policy";


    private static String ROHAN_EMAIL = "rohanraikr@gmail.com";
    private static String ROHAN_FACEBOOK = "https://www.facebook.com/";

    private static String NASHARAT_EMAIL = "nasharatkhankhana@gmail.com";
    private static String NASHRAT_FACEBOOK = "https://www.facebook.com/nashratkhankhana.husain";

    private static String DESIGNER_EMAIL = "designer@gmail.com";
    private static String DESIGNER_WEBSITE = "https://www.facebook.com/";


    LinearLayout changelog, intro, licenses, writeAnEmail, followOnTwitter,
                 privacyPolicy, visitWebsite, reportBugs, translate, donate, rateOnGooglePlay, joinCommunity;

    TextView appVersion;

    AppCompatButton st_1a, st_1b, st_2a, st_2b, st_3a, st_3b;

    String passwd = "NULL";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallunix_activity_about);


        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_about);

        intro = (LinearLayout)findViewById(R.id.intro);
        licenses = (LinearLayout)findViewById(R.id.licenses);
        writeAnEmail = (LinearLayout)findViewById(R.id.write_an_email);
        followOnTwitter = (LinearLayout)findViewById(R.id.follow_on_twitter);
        privacyPolicy = (LinearLayout)findViewById(R.id.privacy_policy);
        visitWebsite = (LinearLayout)findViewById(R.id.visit_website);

        translate = (LinearLayout)findViewById(R.id.translate);


        appVersion=(TextView)findViewById(R.id.app_version);
        appVersion.setText(getCurrentVersionName(this));


        setUpOnClickListeners();


    }


    @Override
    public void onClick(View v) {
        if (v == changelog) {
            webViewDialoug();
        } else if (v == licenses) {
            licencesDialoug();
        } else if (v == intro) {
            Intent i = new Intent(this, SplashActivity.class);
            i.putExtra("pressed", true); // passing boolean value
            startActivity(i);
        } else if (v == followOnTwitter) {
            openUrl(INSTAGRAM);
        } else if (v == privacyPolicy) {
            openUrl(POLICY);
        } else if (v == visitWebsite) {
            openUrl(WEBSITE);
        } else if (v == reportBugs) {
            //startActivity(new Intent(this, BugReportActivity.class));
        } else if (v == writeAnEmail) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:rahul@wallunix.com"));
            intent.putExtra(Intent.EXTRA_EMAIL, "rahul@wallunix.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Wallunix");
            startActivity(Intent.createChooser(intent, "E-Mail"));
        } else if (v == translate) {

        } else if (v == rateOnGooglePlay) {
            openUrl(RATE_ON_GOOGLE_PLAY);
        } else if (v == donate) {
            // if (App.isProVersion()) {
            //  DonationsDialog.create().show(getSupportFragmentManager(), "DONATION_DIALOG");
            // } else {
            //startActivity(new Intent(this, PurchaseActivity.class));
            // }
        }else if (v == joinCommunity) {

        }

        else if (v == st_1a)
        {
            openUrl(ROHAN_FACEBOOK);
        }
        else if (v == st_1b)
        {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"+ROHAN_EMAIL));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Wallunix");
            startActivity(Intent.createChooser(intent, "E-Mail"));

        }else if (v == st_2a)
        {
            openUrl(NASHRAT_FACEBOOK);
        }else if (v == st_2b)
        {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"+NASHARAT_EMAIL));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Wallunix");
            startActivity(Intent.createChooser(intent, "E-Mail"));

        }else if (v == st_3a)
        {
            openUrl(DESIGNER_WEBSITE);

        }else if (v == st_3b)
        {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"+DESIGNER_EMAIL));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Wallunix");
            startActivity(Intent.createChooser(intent, "E-Mail"));

        }

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/


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


    private void setUpOnClickListeners() {
        intro.setOnClickListener(this);
        licenses.setOnClickListener(this);
        followOnTwitter.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);
        visitWebsite.setOnClickListener(this);
        writeAnEmail.setOnClickListener(this);
        translate.setOnClickListener(this);


    }


    public void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void webViewDialoug(){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Whats New in this Version");

        WebView wv = new WebView(this);
        wv.loadUrl("file:///android_asset/changelog.html");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private static String getCurrentVersionName(@NonNull final Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName ;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "Unkown";
    }

    private void licencesDialoug(){

    }


}
