package com.tgb.media.helper;

import com.tgb.media.database.MovieOverviewModel;

/**
 * Created by yonig on 18/03/2017.
 */

public class MovieObesrvableResult {

    public int position;
    public MovieOverviewModel movie;

    public MovieObesrvableResult(int position, MovieOverviewModel movie) {
        this.position = position;
        this.movie = movie;
    }

    public int getPosition() {
        return position;
    }

    public MovieOverviewModel getMovie() {
        return movie;
    }
}
