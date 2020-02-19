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

package wallunix.uhd.feed.news;

import static wallunix.uhd.wallpaper.wallpapers4k.Classes.Utils.getRandomDrawbleColor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ch.deletescape.lawnchair.LawnchairApp;
import com.android.launcher3.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import java.util.List;
import java.util.Random;


public class RecyclerNewsAdapter extends RecyclerView.Adapter<RecyclerNewsAdapter.ArticleViewHolder>{

        private List<Article> articleArrayList;
        private Context mContext;
        private InterstitialAd mInterstitialAd;

        //*********Google SignIn Variables*********


        public RecyclerNewsAdapter(List<Article> articleArrayList) {
                this.articleArrayList = articleArrayList;


        }

        @Override
        public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.feed_list_item, parent, false);

                mContext=parent.getContext();

                return new ArticleViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ArticleViewHolder holder, int position) {

                final Article mArticle = articleArrayList.get(position);


                holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(mArticle.getUrl()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(i);

                                if (mInterstitialAd.isLoaded())
                                        mInterstitialAd.show();
                        }
                });


                holder.title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(mArticle.getUrl()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(i);

                                if (mInterstitialAd.isLoaded())
                                        mInterstitialAd.show();

                        }
                });

                holder.body.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(mArticle.getUrl()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(i);

                                if (mInterstitialAd.isLoaded())
                                        mInterstitialAd.show();

                        }
                });

                holder.title.setText(mArticle.getTitle());
                holder.body.setText(mArticle.getDescription());
                holder.timestamp.setText(mArticle.getPublishedAt().substring(0,10));

                Glide
                        .with(mContext)
                        .asBitmap()
                        .load(mArticle.getUrlToImage())
                        .centerCrop()
                        .placeholder(getRandomDrawbleColor())
                        .error(R.drawable.news_error_paceholder)
                        .into(holder.thumbnail);


                loadAds();
        }

        @Override
        public int getItemCount() {
                return articleArrayList.size();
        }

        public class ArticleViewHolder extends RecyclerView.ViewHolder {

                public ImageView thumbnail, dotmenu;
                public TextView title, body, timestamp;

                public ArticleViewHolder(View view) {
                        super(view);

                        thumbnail = (ImageView) view.findViewById(R.id.imageView);
                        title = (TextView) view.findViewById(R.id.textViewTitle);
                        body = (TextView) view.findViewById(R.id.textViewBody);
                        timestamp = (TextView) view.findViewById(R.id.timestamp);
                        dotmenu = (ImageView) view.findViewById(R.id.dotmenu);
                }
        }


        //******************Miscelenious Methods***************


        private void loadAds(){

                mInterstitialAd = new InterstitialAd(mContext);
                mInterstitialAd.setAdUnitId(mContext.getResources().getString(R.string.interstitial_ad_unit_id));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());

                mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                                // Code to be executed when an ad finishes loading.
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                                // Code to be executed when an ad request fails.
                        }

                        @Override
                        public void onAdOpened() {
                                // Code to be executed when the ad is displayed.
                        }

                        @Override
                        public void onAdClicked() {
                                // Code to be executed when the user clicks on an ad.
                        }

                        @Override
                        public void onAdLeftApplication() {
                                // Code to be executed when the user has left the app.
                        }

                        @Override
                        public void onAdClosed() {
                                // Code to be executed when the interstitial ad is closed.
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                });

        }

}
