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

package wallunix.uhd.wallpaper.wallpapers4k.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class ArtistFollowingSqlite extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "FollowingArtist";

    // Wallpapers table name
    private static final String ARTIST_WALLPAPERS = "Following";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String ARTIST_ID = "fid";
    private static final String NAME = "name";
    private static final String DP = "wallpaperurl";

    Context mContext;


    public ArtistFollowingSqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext=context;
    }






    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + ARTIST_WALLPAPERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," +  ARTIST_ID+ " STRING UNIQUE," + NAME + " TEXT,"
                + DP + " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ARTIST_WALLPAPERS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new wallpapers

    public void addArtist(String id, String name, String dp) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ARTIST_ID, id);
        values.put(NAME,name);
        values.put(DP,dp);


        // Inserting Row
        db.insert(ARTIST_WALLPAPERS, null, values);
        db.close(); // Closing database connection


    }


    public List<Artist> getAllArtist() {
        List<Artist> mArtistList = new ArrayList<Artist>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ARTIST_WALLPAPERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Artist a = new Artist(cursor.getString(1),cursor.getString(2),cursor.getString(3));
                mArtistList.add(a);
            } while (cursor.moveToNext());
        }

        return mArtistList;
    }


    public void removeArtist(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ARTIST_WALLPAPERS, ARTIST_ID + " = ?",
                new String[] { id });
        db.close();
    }


    public boolean checkFollowing(String id) {

        String idQuotes = "\"" + id+ "\"";

        String countQuery = "SELECT  * FROM " + ARTIST_WALLPAPERS + " WHERE "+ ARTIST_ID + " = "+ idQuotes ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor.getCount()==0)
        return false;

        else
            return true;
    }

    // Getting contacts Count
    public int getFavouritesCount() {
        String countQuery = "SELECT  * FROM " + ARTIST_WALLPAPERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


}
