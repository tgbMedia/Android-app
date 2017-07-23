package com.tgb.media.adapter.model;

import com.tgb.media.database.MovieOverviewModel;

import java.util.LinkedList;
import java.util.List;

public class DiscoverModel {

    //Properties
    private List<MovieOverviewModel> list;
    private int type;
    private String title;

    //Finals
    public static final int CAROUSEL = 0;
    public static final int LIST = 2;

    public DiscoverModel(List<MovieOverviewModel> list, int type, String title) {
        this.list = list;
        this.title = title;
        this.type = type;
    }

    public DiscoverModel(int type) {
        this.list = new LinkedList<>();
        this.type = type;
    }

    public DiscoverModel(int type, String title) {
        this.list = new LinkedList<>();
        this.title = title;
        this.type = type;
    }

    public List<MovieOverviewModel> getList() {
        return list;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }
}
