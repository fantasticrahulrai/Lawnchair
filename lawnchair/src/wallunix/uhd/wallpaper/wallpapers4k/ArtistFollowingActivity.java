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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.android.launcher3.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import wallunix.uhd.wallpaper.wallpapers4k.Adapters.ListArtistFollowingAdapter;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Artist;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.ArtistFollowingSqlite;

public class ArtistFollowingActivity extends AppCompatActivity {

    private static List<Artist> mListSqlite;
    ArrayList<Artist> mList;

    private ListView listView;
    private ListArtistFollowingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallunix_activity_artist_following);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Following");

        mListSqlite = new ArrayList<>();
        mList = new ArrayList<>();

        final ArtistFollowingSqlite sqdb = new ArtistFollowingSqlite(this);
        mListSqlite = sqdb.getAllArtist();

        for (Artist a : mListSqlite )
            mList.add(a);


        Collections.reverse(mList);
        adapter = new ListArtistFollowingAdapter(getApplicationContext(), mList);
        listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                final Artist a;
                a = mList.get(position);

                Intent i = new Intent(getApplicationContext(), ArtistProfileActivity.class);
                i.putExtra("artistsid", a.getArtistid());
                i.putExtra("artistname", a.getArtistname());
                i.putExtra("artistdp", a.getArtistdp());
                startActivity(i);



            }
        });





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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

}
