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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.animation.Interpolator;

final class SlidingPanelLayoutInterpolator extends AnimatorListenerAdapter implements Interpolator {

    private ObjectAnimator mAnimator;
    int mFinalX;
    private final SlidingPanelLayout slidingPanelLayout;

    public SlidingPanelLayoutInterpolator(SlidingPanelLayout slidingPanelLayoutVar) {
        this.slidingPanelLayout = slidingPanelLayoutVar;
    }

    public final void cnP() {
        if (this.mAnimator != null) {
            this.mAnimator.removeAllListeners();
            this.mAnimator.cancel();
        }
    }

    public final void dt(int i, int i2) {
        cnP();
        this.mFinalX = i;
        if (i2 > 0) {
            this.mAnimator = ObjectAnimator.ofInt(this.slidingPanelLayout, SlidingPanelLayout.PANEL_X, new int[]{i}).setDuration((long) i2);
            this.mAnimator.setInterpolator(this);
            this.mAnimator.addListener(this);
            this.mAnimator.start();
            return;
        }
        onAnimationEnd(null);
    }

    public final boolean isFinished() {
        return this.mAnimator == null;
    }

    public final void onAnimationEnd(Animator animator) {
        this.mAnimator = null;
        this.slidingPanelLayout.BM(this.mFinalX);
        SlidingPanelLayout slidingPanelLayoutVar = this.slidingPanelLayout;
        if (slidingPanelLayoutVar.mSettling) {
            slidingPanelLayoutVar.mSettling = false;
            if (slidingPanelLayoutVar.uoC == 0) {
                if (SlidingPanelLayout.DEBUG) {
                    Log.d("wo.SlidingPanelLayout", "onPanelClosed");
                }
                slidingPanelLayoutVar.cnO();
                slidingPanelLayoutVar.mIsPanelOpen = false;
                slidingPanelLayoutVar.mIsPageMoving = false;
                if (slidingPanelLayoutVar.uoH != null) {
                    slidingPanelLayoutVar.uoH.close();
                }
            } else if (slidingPanelLayoutVar.uoC == slidingPanelLayoutVar.getMeasuredWidth()) {
                slidingPanelLayoutVar.cnG();
            }
        }
    }

    public final float getInterpolation(float f) {
        float f2 = f - 1.0f;
        return (f2 * (((f2 * f2) * f2) * f2)) + 1.0f;
    }
}
