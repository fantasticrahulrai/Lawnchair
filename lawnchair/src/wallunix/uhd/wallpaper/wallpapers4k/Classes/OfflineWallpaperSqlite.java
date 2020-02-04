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
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.List;

public class OfflineWallpaperSqlite extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "OfflineWallpaper";

    // Wallpapers table name
    private static final String TABLE_WALLPAPERS = "Offline";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String FIREBASE_ID = "fid";
    private static final String NAME = "name";
    private static final String WALLPAPERURL = "wallpaperurl";
    private static final String THUMBNAILURL = "thumbnailurl";
    private static final String DOWNLOADS = "downloads";
    private static final String CATEGORY = "category";

    Context mContext;


    public OfflineWallpaperSqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext=context;
    }






    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_WALLPAPERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," +  FIREBASE_ID + " STRING UNIQUE," + NAME + " TEXT,"
                + WALLPAPERURL + " TEXT, " +  THUMBNAILURL + " TEXT," + DOWNLOADS + " INTEGER," + CATEGORY + " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALLPAPERS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new wallpapers

    public void addWallpapers(Wallpaper wallpaper) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, wallpaper.getName()); // Contact Name
        values.put(FIREBASE_ID,wallpaper.getId());
        values.put(WALLPAPERURL, wallpaper.getWallpaper()); // Contact Phone
        values.put(THUMBNAILURL, wallpaper.getThumbnail()); // Contact Phone
        values.put(DOWNLOADS, wallpaper.getDownloads()); // Contact Phone
        values.put(CATEGORY, wallpaper.getCategory()); // Contact Phone


        // Inserting Row
        db.insert(TABLE_WALLPAPERS, null, values);
        db.close(); // Closing database connection

    }

    public void batchInsterstion(List<Wallpaper> mWallpaperListSqlite){


        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();

        for (Wallpaper wallpaper : mWallpaperListSqlite) {

            values.put(NAME, wallpaper.getName()); // Contact Name
            values.put(FIREBASE_ID,wallpaper.getId());
            values.put(WALLPAPERURL, wallpaper.getWallpaper()); // Contact Phone
            values.put(THUMBNAILURL, wallpaper.getThumbnail()); // Contact Phone
            values.put(DOWNLOADS, wallpaper.getDownloads()); // Contact Phone
            values.put(CATEGORY, wallpaper.getCategory()); // Contact Phone*/

            db.insert(TABLE_WALLPAPERS, null, values );

        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

    }

    public void insertTestData(List<Wallpaper> mWallpaperListSqlite) {
        String sql = "INSERT INTO " + TABLE_WALLPAPERS +
        " (" + NAME + ", "+ FIREBASE_ID + ", "+ WALLPAPERURL+ ", "+ THUMBNAILURL+ ", "+DOWNLOADS+ ", "+CATEGORY+ ") values (?, ?, ?, ?, ?, ?);";

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        SQLiteStatement stmt = db.compileStatement(sql);

        for (Wallpaper wallpaper : mWallpaperListSqlite) {

            stmt.bindString(1, wallpaper.getName());
            stmt.bindString(2, wallpaper.getId());
            stmt.bindString(3, wallpaper.getWallpaper());
            stmt.bindString(4, wallpaper.getThumbnail());
            stmt.bindDouble(5, wallpaper.getDownloads());
            stmt.bindString(6, wallpaper.getCategory());

            long entryID = stmt.executeInsert();
            stmt.clearBindings();
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }


    // Getting All Wallpapers
    public List<Wallpaper> getAllWallpapers() {
        List<Wallpaper> mWallpaperList = new ArrayList<Wallpaper>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WALLPAPERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Wallpaper wallpaper = new Wallpaper();
                wallpaper.setId(cursor.getString(1));
                wallpaper.setName(cursor.getString(2));
                wallpaper.setWallpaper(cursor.getString(3));
                wallpaper.setThumbnail(cursor.getString(4));
                wallpaper.setDownloads(Integer.parseInt(cursor.getString(5)));
                wallpaper.setCategory(cursor.getString(6));
                // Adding contact to list
                mWallpaperList.add(wallpaper);
            } while (cursor.moveToNext());
        }

        // return contact list
        return mWallpaperList;
    }

    public List<Wallpaper> getCategoryWallpaper (String Category){
        String CategoryInsideQuotes = "\"" + Category + "\"";

        List<Wallpaper> mWallpaperList = new ArrayList<Wallpaper>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WALLPAPERS + " WHERE CATEGORY = "+ CategoryInsideQuotes;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Wallpaper wallpaper = new Wallpaper();
                wallpaper.setId(cursor.getString(1));
                wallpaper.setName(cursor.getString(2));
                wallpaper.setWallpaper(cursor.getString(3));
                wallpaper.setThumbnail(cursor.getString(4));
                wallpaper.setDownloads(Integer.parseInt(cursor.getString(5)));
                wallpaper.setCategory(cursor.getString(6));
                // Adding contact to list
                mWallpaperList.add(wallpaper);
            } while (cursor.moveToNext());
        }

        return mWallpaperList;
    }


    public List<Wallpaper> getLatestWallpaper (){

        List<Wallpaper> mWallpaperList = new ArrayList<Wallpaper>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WALLPAPERS + " ORDER BY "+ NAME + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Wallpaper wallpaper = new Wallpaper();
                wallpaper.setId(cursor.getString(1));
                wallpaper.setName(cursor.getString(2));
                wallpaper.setWallpaper(cursor.getString(3));
                wallpaper.setThumbnail(cursor.getString(4));
                wallpaper.setDownloads(Integer.parseInt(cursor.getString(5)));
                wallpaper.setCategory(cursor.getString(6));
                // Adding contact to list
                mWallpaperList.add(wallpaper);
            } while (cursor.moveToNext());
        }

        return mWallpaperList;
    }

    public List<Wallpaper> getOldestWallpaper (){

        List<Wallpaper> mWallpaperList = new ArrayList<Wallpaper>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WALLPAPERS + " ORDER BY "+ NAME + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Wallpaper wallpaper = new Wallpaper();
                wallpaper.setId(cursor.getString(1));
                wallpaper.setName(cursor.getString(2));
                wallpaper.setWallpaper(cursor.getString(3));
                wallpaper.setThumbnail(cursor.getString(4));
                wallpaper.setDownloads(Integer.parseInt(cursor.getString(5)));
                wallpaper.setCategory(cursor.getString(6));
                // Adding contact to list
                mWallpaperList.add(wallpaper);
            } while (cursor.moveToNext());
        }

        return mWallpaperList;
    }

    public List<Wallpaper> getRandomClickWallpaper (String Category){
        String CategoryInsideQuotes = "\"" + Category + "\"";

        List<Wallpaper> mWallpaperList = new ArrayList<Wallpaper>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WALLPAPERS + " WHERE CATEGORY = "+ CategoryInsideQuotes+
                " ORDER BY RANDOM() LIMIT 8";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Wallpaper wallpaper = new Wallpaper();
                wallpaper.setId(cursor.getString(1));
                wallpaper.setName(cursor.getString(2));
                wallpaper.setWallpaper(cursor.getString(3));
                wallpaper.setThumbnail(cursor.getString(4));
                wallpaper.setDownloads(Integer.parseInt(cursor.getString(5)));
                wallpaper.setCategory(cursor.getString(6));
                // Adding contact to list
                mWallpaperList.add(wallpaper);
            } while (cursor.moveToNext());
        }

        return mWallpaperList;
    }

    // Getting contacts Count
    public int getFavouritesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_WALLPAPERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


}
