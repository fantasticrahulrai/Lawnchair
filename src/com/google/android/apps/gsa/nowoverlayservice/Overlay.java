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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.android.launcher3.R;
import com.google.android.libraries.gsa.d.a.OverlayController;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wallunix.uhd.feed.news.APIInterface;
import wallunix.uhd.feed.news.ApiClient;
import wallunix.uhd.feed.news.RecyclerNewsAdapter;
import wallunix.uhd.feed.news.Article;
import wallunix.uhd.feed.news.ResponseModel;


public class Overlay extends OverlayController {

    private final Context context;

    private static final String API_KEY = "68dd0fe7501f462497a96d0fa7f98625";

    private SwipeRefreshLayout pullToRefresh;
    private RecyclerView recyclerView;
    private RecyclerNewsAdapter adapter;
    private List<Article> mArticleList;


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
        View v = LayoutInflater.from(context).inflate(R.layout.feed_overlay, this.container, true);
        //TextView textView = rowView.findViewById(R.id.textView);
        //textView.setText("Hello Window this");

        mArticleList = new ArrayList<>();
        recyclerView = (RecyclerView) v.findViewById(R.id.activity_main_rv);
        pullToRefresh = v.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                 loadFeed();
            }
        });



    }


    /* Below is various event forwarding to Google Now */

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        Toast.makeText(getApplicationContext(), "Attached", Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        Toast.makeText(getApplicationContext(), "Dettached", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        super.onWindowFocusChanged(focused);

       // Toast.makeText(getApplicationContext(), ""+ focused, Toast.LENGTH_SHORT).show();

        if (focused){

            loadFeed();

        }


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }


    private void loadFeed(){

        pullToRefresh.setRefreshing(true);

        final APIInterface apiService = ApiClient.getClient().create(APIInterface.class);
        Call<ResponseModel> call = apiService.getLatestNews("in",API_KEY);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                if(response.body().getStatus().equals("ok")) {

                    mArticleList = response.body().getArticles();
                    if(mArticleList.size()>0) {

                        adapter = new RecyclerNewsAdapter(mArticleList);

                        recyclerView.setAdapter(adapter);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        pullToRefresh.setRefreshing(false);

                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }


}
