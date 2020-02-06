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

import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.setAlarmQuote;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class QuoteAlarmService extends IntentService {


    public QuoteAlarmService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences mUserDetails = getSharedPreferences("UserDetails", 0); // 0 - for private mode
        boolean quoteStatus=mUserDetails.getBoolean("dailyquote", true);

        if (quoteStatus)
        setAlarmQuote(this);

        Toast.makeText(this, "Service Called", Toast.LENGTH_LONG).show();


    }
}


