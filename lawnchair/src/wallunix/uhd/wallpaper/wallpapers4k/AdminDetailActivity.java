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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.launcher3.R;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.parse.ParseObject;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;

public class AdminDetailActivity extends AppCompatActivity {


    FirebaseStorage storage;
    StorageReference storageReferenceWall, storageReferenceThumb;

    SharedPreferences mUserDetails;

    TextView title, category, download;
    Button addToEditors, addToSpolight, deleteWallpaper, updateCategory;
    ImageView adminPreview;
    private Spinner categories;

    Wallpaper mWallpaper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallunix_activity_admin_detail);


        mWallpaper=(Wallpaper)getIntent().getSerializableExtra("url");

        mUserDetails = getApplicationContext().getSharedPreferences("UserDetails", 0); // 0 - for private mode

        int width = mUserDetails.getInt("screenwidth",500);

        title = (TextView)findViewById(R.id.textViewAdminTitle);
        category = (TextView)findViewById(R.id.textViewAdminCategory);
        download = (TextView)findViewById(R.id.textViewAdminDownload);

        addToEditors = (Button)findViewById(R.id.buttonAddToEditors);
        addToSpolight = (Button)findViewById(R.id.buttonAddToSpotlight);
        deleteWallpaper = (Button)findViewById(R.id.buttonDeleteWallpaper);
        updateCategory = (Button)findViewById(R.id.buttonUpdateCategory);

        categories = (Spinner)findViewById(R.id.spinner);

        adminPreview = (ImageView)findViewById(R.id.imageViewAdmin);
        adminPreview.getLayoutParams().height = width+100;


        //*****************************************
        title.setText(mWallpaper.getName());
        category.setText(mWallpaper.getCategory());
        download.setText("Downloads: "+mWallpaper.getDownloads());

        Glide
                .with(this)
                .load(mWallpaper.getThumbnail())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_error)
                .into(adminPreview);



        addToEditors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addToEditorsChoice();

            }
        });

        addToSpolight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addToSpotlight();

            }
        });

        deleteWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteWallpaper();

            }
        });

        updateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              updateCategory();

            }
        });



    }


    public void deleteWallpaper(){

        storage = FirebaseStorage.getInstance();

        storageReferenceWall = storage.getReferenceFromUrl(mWallpaper.getWallpaper());
        storageReferenceThumb = storage.getReferenceFromUrl(mWallpaper.getThumbnail());
        storageReferenceThumb.delete();
        storageReferenceWall.delete();



    }


    private void addToEditorsChoice(){


        ParseObject w = new ParseObject("editorschoice");
        w.put("name", mWallpaper.getName());
        w.put("thumbnail", mWallpaper.getThumbnail());
        w.put("wallpaper", mWallpaper.getWallpaper());
        w.put("downloads", mWallpaper.getDownloads());
        w.put("category", mWallpaper.getCategory());
        w.put("firebaseid", mWallpaper.getId());
        w.saveInBackground();

        Toast.makeText(getApplicationContext(), "Added to Editors choice", Toast.LENGTH_SHORT).show();

    }

    private void addToSpotlight(){


        ParseObject w = new ParseObject("spotlight");
        w.put("name", mWallpaper.getName());
        w.put("thumbnail", mWallpaper.getThumbnail());
        w.put("wallpaper", mWallpaper.getWallpaper());
        w.put("downloads", mWallpaper.getDownloads());
        w.put("category", mWallpaper.getCategory());
        w.put("firebaseid", mWallpaper.getId());
        w.saveInBackground();

        Toast.makeText(getApplicationContext(), "Added to Spotlight", Toast.LENGTH_SHORT).show();

    }

    private void updateCategory(){

       // myRef.getRef().child(mWallpaper.getId()).child("category").setValue(categories.getSelectedItem().toString());

        Toast.makeText(this, "Category Updated", Toast.LENGTH_SHORT).show();

    }
}
