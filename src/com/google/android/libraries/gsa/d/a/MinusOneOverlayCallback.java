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

import android.content.res.Configuration;
import android.os.Message;
import com.google.android.libraries.material.progress.u;
import java.io.PrintWriter;

public final class MinusOneOverlayCallback extends OverlayControllerCallback {

    private final OverlaysController overlaysController;

    public MinusOneOverlayCallback(
            OverlaysController overlaysControllerVar, OverlayControllerBinder overlayControllerBinderVar) {
        super(overlayControllerBinderVar, 3);
        this.overlaysController = overlaysControllerVar;
    }

    final OverlayController createController(Configuration configuration) {
        return this.overlaysController.createController(configuration, this.overlayControllerBinder.mServerVersion, this.overlayControllerBinder.mClientVersion);
    }

    public final void dump(PrintWriter printWriter, String str) {
        printWriter.println(String.valueOf(str).concat("MinusOneOverlayCallback"));
        super.dump(printWriter, str);
    }

    public final boolean handleMessage(Message message) {
        if (super.handleMessage(message)) {
            return true;
        }
        OverlayController overlayControllerVar;
        long when;
        switch (message.what) {
            case 3:
                if (this.overlayController != null) {
                    overlayControllerVar = this.overlayController;
                    when = message.getWhen();
                    if (!overlayControllerVar.cnD()) {
                        SlidingPanelLayout slidingPanelLayoutVar = overlayControllerVar.slidingPanelLayout;
                        if (slidingPanelLayoutVar.uoC < slidingPanelLayoutVar.mTouchSlop) {
                            overlayControllerVar.slidingPanelLayout.BM(0);
                            overlayControllerVar.mAcceptExternalMove = true;
                            overlayControllerVar.unX = 0;
                            overlayControllerVar.slidingPanelLayout.mForceDrag = true;
                            overlayControllerVar.obZ = when - 30;
                            overlayControllerVar.b(0, overlayControllerVar.unX, overlayControllerVar.obZ);
                            overlayControllerVar.b(2, overlayControllerVar.unX, when);
                        }
                    }
                }
                return true;
            case u.uKO /*4*/:
                if (this.overlayController != null) {
                    overlayControllerVar = this.overlayController;
                    float floatValue = (float) message.obj;
                    when = message.getWhen();
                    if (overlayControllerVar.mAcceptExternalMove) {
                        overlayControllerVar.unX = (int) (floatValue * ((float) overlayControllerVar.slidingPanelLayout.getMeasuredWidth()));
                        overlayControllerVar.b(2, overlayControllerVar.unX, when);
                    }
                }
                return true;
            case u.uKS /*5*/:
                if (this.overlayController != null) {
                    overlayControllerVar = this.overlayController;
                    when = message.getWhen();
                    if (overlayControllerVar.mAcceptExternalMove) {
                        overlayControllerVar.b(1, overlayControllerVar.unX, when);
                    }
                    overlayControllerVar.mAcceptExternalMove = false;
                }
                return true;
            default:
                return false;
        }
    }
}
