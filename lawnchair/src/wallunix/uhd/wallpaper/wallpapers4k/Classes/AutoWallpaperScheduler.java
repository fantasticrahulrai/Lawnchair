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

import android.app.job.JobParameters;
import android.app.job.JobService;

public class AutoWallpaperScheduler extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {

       // Toast.makeText(this, "Auto Wallpaper started", Toast.LENGTH_SHORT).show();

        AutoWallpaperAsyncTask task = new AutoWallpaperAsyncTask(this);
        task.execute();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

       // Toast.makeText(this, "Auto Wallpaper stopped", Toast.LENGTH_SHORT).show();

        return false;
    }

}

