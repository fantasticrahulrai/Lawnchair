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
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.WindowManager.LayoutParams;
import com.google.android.a.LauncherOverlayBinder;
import com.google.android.a.c;
import com.google.android.libraries.material.progress.u;

//import com.google.android.gms.dynamite.descriptors.com.google.android.gms.ads.dynamite.ModuleDescriptor;

public abstract class LauncherOverlayInterfaceBinder extends LauncherOverlayBinder implements a {

    public LauncherOverlayInterfaceBinder() {
        attachInterface(this, "com.google.android.libraries.launcherclient.ILauncherOverlay");
    }

    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {//Todo: throws is new
        d dVar = null;
        if (a(i, parcel, parcel2, i2)) {
            return true;
        }
        IBinder readStrongBinder;
        IInterface queryLocalInterface;
        boolean HC;
        switch (i) {
            case 1:
                cnK();
                break;
            case 2:
                aL(parcel.readFloat());
                break;
            case 3:
                cnL();
                break;
            case u.uKO /*4*/:
                LayoutParams layoutParams = (LayoutParams) c.a(parcel, LayoutParams.CREATOR);
                readStrongBinder = parcel.readStrongBinder();
                if (readStrongBinder != null) {
                    queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.libraries.launcherclient.ILauncherOverlayCallback");
                    if (queryLocalInterface instanceof d) {
                        dVar = (d) queryLocalInterface;
                    } else {
                        dVar = new f(readStrongBinder);
                    }
                }
                a(layoutParams, dVar, parcel.readInt());
                break;
            case u.uKS /*5*/:
                od(c.a(parcel));
                break;
            case u.uKQ /*6*/:
                fI(parcel.readInt());
                break;
            case u.uKR /*7*/:
                onPause();
                break;
            case 8:
                onResume();
                break;
            case 9:
                BK(parcel.readInt());
                break;
            case 10:
                oe(c.a(parcel));
                break;
            case 11:
                String HB = HB();
                parcel2.writeNoException();
                parcel2.writeString(HB);
                break;
            case /*ModuleDescriptor.MODULE_VERSION*/12 /*12*/://Todo: modified, 12 was always there but the constant was there before
                HC = HC();
                parcel2.writeNoException();
                c.a(parcel2, HC);
                break;
            case 13:
                HC = cnM();
                parcel2.writeNoException();
                c.a(parcel2, HC);
                break;
            case 14:
                Bundle bundle = (Bundle) c.a(parcel, Bundle.CREATOR);
                readStrongBinder = parcel.readStrongBinder();
                if (readStrongBinder != null) {
                    queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.libraries.launcherclient.ILauncherOverlayCallback");
                    if (queryLocalInterface instanceof d) {
                        dVar = (d) queryLocalInterface;
                    } else {
                        dVar = new f(readStrongBinder);
                    }
                }
                a(bundle, dVar);
                break;
            case 16:
                BJ(parcel.readInt());
                break;
            case 17:
                HC = a(parcel.createByteArray(), (Bundle) c.a(parcel, Bundle.CREATOR));
                parcel2.writeNoException();
                c.a(parcel2, HC);
                break;
            default:
                return false;
        }
        return true;
    }
}
