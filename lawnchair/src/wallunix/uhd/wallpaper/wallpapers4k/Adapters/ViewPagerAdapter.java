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

package wallunix.uhd.wallpaper.wallpapers4k.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import wallunix.uhd.wallpaper.wallpapers4k.Fragments.AutoWallpaperFragment;
import wallunix.uhd.wallpaper.wallpapers4k.Fragments.FeaturedFragment;
import wallunix.uhd.wallpaper.wallpapers4k.Fragments.FeedFragment;
import wallunix.uhd.wallpaper.wallpapers4k.Fragments.LatestFragment;
import wallunix.uhd.wallpaper.wallpapers4k.Fragments.RandomFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {

            case 0: return AutoWallpaperFragment.newInstance("FirstFragment","");
            case 1: return FeedFragment.newInstance("FirstFragment","");
            case 2: return LatestFragment.newInstance("FirstFragment","");
            case 3: return RandomFragment.newInstance("FirstFragment","");
            case 4: return FeaturedFragment.newInstance("FirstFragment","");


            default: return LatestFragment.newInstance("ThirdFragment","");
        }
    }

    //@Override
    public int getCount() {
        return 5;
    }

    private String tabTitles[] = new String[]{"Auto", "Feed", "Latest", "Random", "Featured"};



    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}