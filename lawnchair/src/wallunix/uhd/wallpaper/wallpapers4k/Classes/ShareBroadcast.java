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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import java.util.ArrayList;

public class ShareBroadcast extends BroadcastReceiver {


    private static ArrayList<Wallpaper> mWallpaperList;
    int totalImages = 0, randNo = 1;

    @Override
    public void onReceive(final Context context, Intent intent) {

        String msg = intent.getStringExtra("Thought");
       // shareWhatsapp(context,msg);

        Toast.makeText(context, "Some error while scheduling Auto Wallpaper", Toast.LENGTH_SHORT).show();

    }

}
