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
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import com.google.android.libraries.gsa.d.a.OverlayController;
import com.google.android.libraries.gsa.d.a.OverlaysController;
import com.google.android.libraries.gsa.d.a.v;


public final class ConfigurationOverlayController extends OverlaysController {

    private final Context mContext;

    public ConfigurationOverlayController(Service service) {
        super(service);
        this.mContext = service;
    }

    //Todo: was protected
    public final int Hx() {
        return 24;
    }

    public final OverlayController createController(@Nullable Configuration configuration, int i, int i2) {
        Context context = this.mContext;
        if (VERSION.SDK_INT >= 17 && configuration != null) {
            context = context.createConfigurationContext(configuration);
        }
        return new Overlay(context, i, i2);
    }

    //Todo: was protected, and return modified
    public final v HA() {
        return new v();
    }
}
