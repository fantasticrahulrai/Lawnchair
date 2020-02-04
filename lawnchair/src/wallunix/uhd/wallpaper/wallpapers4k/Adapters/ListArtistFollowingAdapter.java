package wallunix.uhd.wallpaper.wallpapers4k.Adapters;


import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.getRandomDrawbleColor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.launcher3.R;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Artist;

public class ListArtistFollowingAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Artist> mWallpaperList;
    ListImageAdapter.ViewHolder viewHolder;
    String likes = "0";


    static class ViewHolder {
        ImageView thumbnail;
        TextView likecounter;
        LinearLayout fav;
    }
    // Constructor
    public ListArtistFollowingAdapter(Context c) {
        mContext = c;
    }

    public ListArtistFollowingAdapter(Context c, ArrayList<Artist> mWallpaperList){

        mContext=c;
        this.mWallpaperList=mWallpaperList;

    }

    public int getCount() {

        int i= mWallpaperList.size();
        return i;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {


        final Artist mWallpaper = mWallpaperList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.wallunix_list_item_artist_following, parent, false);
        }
        viewHolder = new ListImageAdapter.ViewHolder();

        viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.profile_image);
        viewHolder.likecounter = (TextView) convertView.findViewById(R.id.title);



        Glide
                .with(mContext)
                .asBitmap()
                .load(mWallpaper.getArtistdp())
                .centerCrop()
                .placeholder(getRandomDrawbleColor())
                .error(R.drawable.ic_error)
                .into(viewHolder.thumbnail);

        viewHolder.likecounter.setText(mWallpaper.getArtistname());



        return convertView;
    }


}

