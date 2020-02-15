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

package com.google.android.libraries.i;

import android.os.Bundle;
import android.os.IInterface;
import android.view.WindowManager.LayoutParams;

public interface a extends IInterface {
    void BJ(int i);

    void BK(int i);

    String HB();

    boolean HC();

    void a(Bundle bundle, d dVar);

    void a(LayoutParams layoutParams, d dVar, int i);

    boolean a(byte[] bArr, Bundle bundle);

    void aL(float f);

    void cnK();

    void cnL();

    boolean cnM();

    void fI(int i);

    void od(boolean z);

    void oe(boolean z);

    void onPause();

    void onResume();
}
