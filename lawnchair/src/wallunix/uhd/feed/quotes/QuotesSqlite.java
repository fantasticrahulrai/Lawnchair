/*
 *     Copyright (C) 2020 Lawnchair Team.
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

package wallunix.uhd.feed.quotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;

public class QuotesSqlite extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "QuotesDatabase";

    // Wallpapers table name
    private static final String QUOTES_TABLE = "Quotes";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String QUOTE = "quote";
    private static final String AUTHOR = "author";

    Context mContext;


    public QuotesSqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext=context;
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + QUOTES_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + QUOTE + " TEXT,"
                + AUTHOR + " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + QUOTES_TABLE);

        // Create tables again
        onCreate(db);
    }


    public void batchInsterstion(List<Quotes> mQuoteListSqlite){


        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();

        for (Quotes q : mQuoteListSqlite) {

            values.put(QUOTE, q.getQuote());
            values.put(AUTHOR, q.getAuthor());

            db.insert(QUOTES_TABLE, null, values );

        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

    }


    public Quotes getRandomQuote() {


        String selectQuery = "SELECT  * FROM " + QUOTES_TABLE + " ORDER BY RANDOM() LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        Quotes q = null;

        if (cursor.moveToFirst()) {
            do {
                q = new Quotes(cursor.getString(1), cursor.getString(2));

            } while (cursor.moveToNext());
        }

        cursor.close();
        return q;
    }





}

