package com.tgb.media.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;
import com.tgb.media.R;
import com.tgb.media.adapter.model.DiscoverModel;
import com.tgb.media.helper.SpacesItemDecoration;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PagerContainer;

public class DiscoverAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private List<DiscoverModel> list;
    private int orientation;
    private Point screenDimensions;

    public DiscoverAdapter(Activity context, int orientation, Point screenDimensions){
        this.context = context;
        this.list = new LinkedList<>();
        this.orientation = orientation;
        this.screenDimensions = screenDimensions;
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addItem(DiscoverModel item){
        list.add(item);
        notifyItemChanged(list.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder= null;
        View itemView;

        switch(viewType){
            case DiscoverModel.CAROUSEL:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.carousel_row, parent, false);

                holder = new CarouselHolder(context, itemView);

                break;

            case DiscoverModel.LIST:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.videos_list_row, parent, false);

                holder = new VideosListHolder(context, orientation, screenDimensions, itemView);

                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((IHolder) holder).onBind(list.get(position));
    }

    class CarouselHolder extends RecyclerView.ViewHolder implements IHolder{

        private Context context;

        @BindView(R.id.videos_carousel) PagerContainer videosCarousel;
        @BindView(R.id.indicator) ExtensiblePageIndicator indicator;

        public CarouselHolder(Context context, View view) {
            super(view);
            ButterKnife.bind(this, view);

            this.context = context;
        }

        @Override
        public void onBind(DiscoverModel model) {
            CarouselAdapter adapter = new CarouselAdapter(context, model.getList());

            //Carousel pager
            ViewPager pager =  videosCarousel.getViewPager();

            pager.setAdapter(adapter);
            pager.setClipChildren(false);
            pager.setOffscreenPageLimit(15);
            pager.setCurrentItem(1);

            new CoverFlow.Builder()
                    .with(pager)
                    .pagerMargin(0)
                    .spaceSize(10)
                    .scale(0.13f)
                    .build();

            indicator.initViewPager(pager);
        }
    }

    class VideosListHolder extends RecyclerView.ViewHolder implements IHolder{

        private Activity context;
        private int orientation;
        private Point screenDimensions;

        @BindView(R.id.title) TextView title;
        @BindView(R.id.videos) RecyclerView videosList;

        public VideosListHolder(Activity context, int orientation,
                                Point screenDimensions, View view) {
            super(view);
            ButterKnife.bind(this, view);

            this.context = context;
            this.orientation = orientation;
            this.screenDimensions = screenDimensions;

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

            videosList.setLayoutManager(layoutManager);

            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(videosList);
        }

        @Override
        public void onBind(DiscoverModel model) {
            DiscoverListAdapter adapter =  new DiscoverListAdapter(
                    this.context,
                    this.screenDimensions,
                    this.orientation
            );

            adapter.setList(model.getList());

            videosList.setItemAnimator(new DefaultItemAnimator());
            videosList.addItemDecoration(new SpacesItemDecoration(5));
            videosList.setAdapter(adapter);

            if(!TextUtils.isEmpty(model.getTitle()))
                title.setText(model.getTitle());
        }
    }

    private interface IHolder{
        void onBind(DiscoverModel model);
    }


}
