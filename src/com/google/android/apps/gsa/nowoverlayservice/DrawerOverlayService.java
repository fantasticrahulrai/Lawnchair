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

package com.google.android.apps.gsa.nowoverlayservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.google.android.libraries.gsa.d.a.OverlaysController;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class DrawerOverlayService extends Service {

    private OverlaysController overlaysController;

    public void onCreate() {
        super.onCreate();
        this.overlaysController = new ConfigurationOverlayController(this);
    }

    public void onDestroy() {
        this.overlaysController.onDestroy();
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return this.overlaysController.onBind(intent);
    }

    public boolean onUnbind(Intent intent) {
        this.overlaysController.unUnbind(intent);
        return false;
    }

    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.overlaysController.dump(printWriter);
    }
}
