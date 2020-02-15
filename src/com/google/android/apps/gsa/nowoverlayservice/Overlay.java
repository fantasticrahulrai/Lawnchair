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

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.android.launcher3.R;
import com.google.android.libraries.gsa.d.a.OverlayController;


public class Overlay extends OverlayController {

    private final Context context;

    public Overlay(Context context, int i, int i2) {
        super(context, R.style.AppTheme, R.style.Theme_SearchNow);
        this.context = context;
    }

    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.container.setFitsSystemWindows(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        View rowView = LayoutInflater.from(context).inflate(R.layout.wallunix_activity_feed, this.container, true);
        TextView textView = rowView.findViewById(R.id.textView);
        textView.setText("Hello Window this");
    }
}
