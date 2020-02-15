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

package com.google.android.libraries.gsa.d.a;

import android.os.Build;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.WindowManager.LayoutParams;

final class OverlayControllerLayoutChangeListener implements OnLayoutChangeListener {

    private final OverlayController overlayController;

    OverlayControllerLayoutChangeListener(OverlayController overlayControllerVar) {
        this.overlayController = overlayControllerVar;
    }

    public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.overlayController.window.getDecorView().removeOnLayoutChangeListener(this);
        if (this.overlayController.panelState == PanelState.CLOSED) {//Todo: PanelState.uoe was default
            OverlayController overlayControllerVar = this.overlayController;
            LayoutParams attributes = overlayControllerVar.window.getAttributes();
            if (Build.VERSION.SDK_INT >= 26) {
                float f = attributes.alpha;
                attributes.alpha = 0.0f;
                if (f != attributes.alpha) {
                    overlayControllerVar.window.setAttributes(attributes);
                    return;
                }
                return;
            }
            attributes.x = overlayControllerVar.mWindowShift;
            attributes.flags |= 512;
            overlayControllerVar.unZ = false;
            overlayControllerVar.window.setAttributes(attributes);
        }
    }
}
