package com.tgb.media.adapter;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;
import com.tgb.media.R;
import com.tgb.media.adapter.model.DiscoverModel;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PagerContainer;

public class DiscoverAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    public List<DiscoverModel> list;

    public DiscoverAdapter(Context context){
        this.context = context;
        this.list = new LinkedList<>();
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

        switch(viewType){
            case DiscoverModel.CAROUSEL:
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.carousel_row, parent, false);

                holder = new CarouselHolder(context, itemView);

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

    private interface IHolder{
        void onBind(DiscoverModel model);
    }


}
