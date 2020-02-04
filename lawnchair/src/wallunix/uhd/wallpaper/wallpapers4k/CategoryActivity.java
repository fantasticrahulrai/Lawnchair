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


import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.ABSTRACT_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.AMOLED_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.ANIMAL_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.ANIME_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.ART_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.BUILDINGS_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.CARS_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.CITYSCAPE_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.FANTASY_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.FLOWER_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.GAMES_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.LIGHT_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.LOVE_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.MINIMAL_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.MOTERCYCLES_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.MOVIES_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.NATURE_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.OTHERS_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.PEOPLE_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.SPACE_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.SPORTS_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.SUPERHEROES_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.TECHNOLOGY_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.VEHICLE_URL;
import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.getRandomDrawbleColor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.android.launcher3.R;
import com.bumptech.glide.Glide;

public class CategoryActivity extends AppCompatActivity {

    CardView cAbstract, cAmoled, cAnime, cAnimal, cArt,  cBuildings, cCityscape, cFantasy, cFlower,
             cGames, cLight, cMinimal, cNature, cSpace, cSuperheroes, cVehicles,
            cMovies, cPeople, cOthers, cMotercycles, cCars, cLove, cSports, cTechnology;

    ImageView iAbstract, iAmoled, iAnime, iAnimal, iArt,  iBuildings, iCityscape, iFantasy, iFlower,
            iGames, iLight, iMinimal, iNature, iSpace, iSuperheroes, iVehicles,
            iMovies, iPeople, iOthers, iMotercycles, iCars, iLove, iSports, iTechnology;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallunix_activity_category);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Categories");

        initViews();

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        pupulateView();


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
    public void onDestroy() {
        super.onDestroy();

        finish();
    }

    private void initViews(){

        cAbstract =(CardView)findViewById(R.id.cardViewAbstract);
        cAmoled =(CardView)findViewById(R.id.cardViewAmoled);
        cAnime =(CardView)findViewById(R.id.cardViewAnime);
        cAnimal =(CardView)findViewById(R.id.cardViewAnimal);
        cArt =(CardView)findViewById(R.id.cardViewArt);
        cBuildings =(CardView)findViewById(R.id.cardViewBuildings);
        cCityscape =(CardView)findViewById(R.id.cardViewCityscape);
        cFantasy =(CardView)findViewById(R.id.cardViewFantasy);
        cFlower =(CardView)findViewById(R.id.cardViewFlower);
        cGames =(CardView)findViewById(R.id.cardViewGames);
        cLight =(CardView)findViewById(R.id.cardViewLight);
        cMinimal =(CardView)findViewById(R.id.cardViewMinimal);
        cNature =(CardView)findViewById(R.id.cardViewNature);
        cSpace =(CardView)findViewById(R.id.cardViewSpace);
        cSuperheroes =(CardView)findViewById(R.id.cardViewSuperhero);
        cVehicles =(CardView)findViewById(R.id.cardViewVehicle);
        cMovies =(CardView)findViewById(R.id.cardViewMovies);
        cPeople =(CardView)findViewById(R.id.cardViewPeople);
        cOthers =(CardView)findViewById(R.id.cardViewOthers);
        cMotercycles =(CardView)findViewById(R.id.cardViewMotercycles);
        cCars =(CardView)findViewById(R.id.cardViewCars);
        cLove =(CardView)findViewById(R.id.cardViewLove);
        cSports =(CardView)findViewById(R.id.cardViewSports);
        cTechnology =(CardView)findViewById(R.id.cardViewTechnology);

        iAbstract =(ImageView) findViewById(R.id.imageViewAbstract);
        iAmoled =(ImageView)findViewById(R.id.imageViewAmoled);
        iAnime =(ImageView)findViewById(R.id.imageViewAnime);
        iAnimal =(ImageView)findViewById(R.id.imageViewAnimal);
        iArt =(ImageView)findViewById(R.id.imageViewArt);
        iBuildings =(ImageView)findViewById(R.id.imageViewBuildings);
        iCityscape =(ImageView)findViewById(R.id.imageViewCityscape);
        iFantasy =(ImageView)findViewById(R.id.imageViewFantasy);
        iFlower =(ImageView)findViewById(R.id.imageViewFlower);
        iGames =(ImageView)findViewById(R.id.imageViewGames);
        iLight =(ImageView)findViewById(R.id.imageViewLight);
        iMinimal =(ImageView)findViewById(R.id.imageViewMinimal);
        iNature =(ImageView)findViewById(R.id.imageViewNature);
        iSpace =(ImageView)findViewById(R.id.imageViewSpace);
        iSuperheroes =(ImageView)findViewById(R.id.imageViewSuperheroes);
        iVehicles =(ImageView)findViewById(R.id.imageViewVehicle);
        iMovies =(ImageView)findViewById(R.id.imageViewMovies);
        iPeople =(ImageView)findViewById(R.id.imageViewPeople);
        iOthers =(ImageView)findViewById(R.id.imageViewOthers);
        iMotercycles =(ImageView)findViewById(R.id.imageViewMotercycles);
        iCars =(ImageView)findViewById(R.id.imageViewCars);
        iLove =(ImageView)findViewById(R.id.imageViewLove);
        iSports =(ImageView)findViewById(R.id.imageViewSports);
        iTechnology =(ImageView)findViewById(R.id.imageViewTechnology);

    }

    private void pupulateView(){
        cAbstract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Abstract");
                startActivity(i);
            }
        });

        cAmoled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Amoled");
                startActivity(i);
            }
        });
        cAnime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Anime");
                startActivity(i);
            }
        });
        cAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Animal");
                startActivity(i);
            }
        });
        cArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Art");
                startActivity(i);
            }
        });
        cBuildings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Buildings");
                startActivity(i);
            }
        });
        cCityscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Cityscape");
                startActivity(i);
            }
        });
        cFantasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Fantasy");
                startActivity(i);
            }
        });
        cFlower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Flower");
                startActivity(i);
            }
        });
        cGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Games");
                startActivity(i);
            }
        });
        cLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Light");
                startActivity(i);
            }
        });

        cMinimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Minimal");
                startActivity(i);
            }
        });
        cNature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Nature");
                startActivity(i);
            }
        });
        cSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Space");
                startActivity(i);
            }
        });
        cSuperheroes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Superheroes");
                startActivity(i);
            }
        });
        cVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Vehicle");
                startActivity(i);
            }
        });
        cMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Movies");
                startActivity(i);
            }
        });
        cPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "People");
                startActivity(i);
            }
        });

        cOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Others");
                startActivity(i);
            }
        });

        cMotercycles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Motorcycles");
                startActivity(i);
            }
        });

        cCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Cars");
                startActivity(i);
            }
        });

        cLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Love");
                startActivity(i);
            }
        });

        cSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Sports");
                startActivity(i);
            }
        });

        cTechnology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), CategoryClickActivity.class);
                i.putExtra("categoryText", "Technology");
                startActivity(i);
            }
        });


        //************ImageView Population

        Glide
                .with(this)
                .asBitmap()
                .load(ABSTRACT_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iAbstract);

        Glide
                .with(this)
                .asBitmap()
                .load(AMOLED_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iAmoled);

        Glide
                .with(this)
                .asBitmap()
                .load(ANIMAL_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iAnimal);
        Glide
                .with(this)
                .asBitmap()
                .load(ANIME_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iAnime);
        Glide
                .with(this)
                .asBitmap()
                .load(ART_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iArt);
        Glide
                .with(this)
                .asBitmap()
                .load(BUILDINGS_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iBuildings);
        Glide
                .with(this)
                .asBitmap()
                .load(CITYSCAPE_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iCityscape);
        Glide
                .with(this)
                .asBitmap()
                .load(FANTASY_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iFantasy);
        Glide
                .with(this)
                .asBitmap()
                .load(FLOWER_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iFlower);

        Glide
                .with(this)
                .asBitmap()
                .load(GAMES_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iGames);

        Glide
                .with(this)
                .asBitmap()
                .load(LIGHT_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iLight);


        Glide
                .with(this)
                .asBitmap()
                .load(MINIMAL_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iMinimal);


        Glide
                .with(this)
                .asBitmap()
                .load(MOVIES_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iMovies);

        Glide
                .with(this)
                .asBitmap()
                .load(NATURE_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iNature);

        Glide
                .with(this)
                .asBitmap()
                .load(OTHERS_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iOthers);

        Glide
                .with(this)
                .asBitmap()
                .load(PEOPLE_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iPeople);

        Glide
                .with(this)
                .asBitmap()
                .load(SPACE_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iSpace);

        Glide
                .with(this)
                .asBitmap()
                .load(SUPERHEROES_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iSuperheroes);

        Glide
                .with(this)
                .asBitmap()
                .load(VEHICLE_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iVehicles);

        Glide
                .with(this)
                .asBitmap()
                .load(MOTERCYCLES_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iMotercycles);

        Glide
                .with(this)
                .asBitmap()
                .load(CARS_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iCars);

        Glide
                .with(this)
                .asBitmap()
                .load(LOVE_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iLove);

        Glide
                .with(this)
                .asBitmap()
                .load(SPORTS_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iSports);

        Glide
                .with(this)
                .asBitmap()
                .load(TECHNOLOGY_URL)
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(getRandomDrawbleColor())
                .into(iTechnology);

    }
}
