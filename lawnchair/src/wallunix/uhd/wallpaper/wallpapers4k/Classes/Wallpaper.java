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

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Wallpaper implements Serializable {

    @SerializedName(value ="name")
    private String name;

    @SerializedName(value ="thumbnail")
    private String thumbnail;

    @SerializedName(value ="wallpaper")
    private String wallpaper;

    @SerializedName(value ="downloads")
    private Integer downloads;

    @SerializedName(value ="firebaseid")
    private String id;

    @SerializedName(value ="category")
    private String category;

    private String artistid;

    private String artistname;

    private String artistdp;


    public Wallpaper() {
    }

    public Wallpaper(String name, String wallpaper) {
        this.name = name;
        this.wallpaper = wallpaper;
    }

    public Wallpaper(String name, String thumbnail, String wallpaper,
            Integer downloads, String category) {
        this.name = name;
        this.thumbnail=thumbnail;
        this.wallpaper=wallpaper;
        this.downloads=downloads;
        this.category=category;

    }

    public Wallpaper(String name, String thumbnail, String wallpaper,
            Integer downloads, String category, String id) {
        this.name = name;
        this.thumbnail=thumbnail;
        this.wallpaper=wallpaper;
        this.downloads=downloads;
        this.category=category;
        this.id = id;

    }

    public Wallpaper(String name, String thumbnail, String wallpaper,
            Integer downloads, String category,
                     String id, String artistid, String artistname, String artistdp) {
        this.name = name;
        this.thumbnail=thumbnail;
        this.wallpaper=wallpaper;
        this.downloads=downloads;
        this.category=category;
        this.id = id;
        this.artistid = artistid;
        this.artistname = artistname;
        this.artistdp = artistdp;

    }

    public String getName() {
        return name;
    }

    public String getWallpaper() {
        return wallpaper;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getArtistid() { return artistid; }

    public String getArtistname() { return artistname;}

    public String getArtistdp() { return artistdp;}


    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setWallpaper(String wallpaper) {
        this.wallpaper = wallpaper;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    public void setId(String id){this.id=id;}

    public void setCategory(String category){this.category=category;}

    public void setArtistid(String artistid){this.artistid=artistid;}

    public void setArtistname(String artistname){this.artistname=artistname;}

    public void setArtistdp(String artistdp){this.artistdp=artistdp;}
}