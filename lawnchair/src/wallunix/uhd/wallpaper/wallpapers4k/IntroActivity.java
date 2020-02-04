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

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import hd.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;

public class IntroActivity extends AppCompatActivity {


    ImageView imageView;
    Wallpaper mWallpaper;
    SharedPreferences mUserDetails;
    SharedPreferences.Editor mEditor;
    Button cont;

    TextView t1, t2, t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

       // t1 = (TextView)findViewById(R.id.textViewIntroTitle);
        t2 = (TextView)findViewById(R.id.textViewIntroText);
        t3 = (TextView)findViewById(R.id.textViewIntroTag);

        Typeface tf = Typeface.createFromAsset(getAssets(),"greatvibes.otf");
        //t1.setTypeface(tf, Typeface.BOLD);
        t2.setTypeface(tf, Typeface.BOLD);
        //t3.setTypeface(tf, Typeface.NORMAL);


        mUserDetails = getApplicationContext().getSharedPreferences("UserDetails", 0); // 0 - for private mode

        imageView = (ImageView)findViewById(R.id.imageViewIntroBg);
        cont = (Button)findViewById(R.id.btnContinue);

        /*Glide
                .with(getApplicationContext())
                .load(R.drawable.vehiclebg)
                .transition(withCrossFade())
                .placeholder(R.drawable.vehiclebg)
                .error(R.drawable.animebg)
                .into(imageView);*/



        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CustomizeFeedActivity.class);
                startActivity(i);


            }
        });


    }
}
