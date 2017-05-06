package com.tgb.media.server;

import com.tgb.media.server.models.MovieFile;
import com.tgb.media.server.models.Response;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TgbService {

    @GET("movies_list")
    Observable<Response<MovieFile>> moviesList();

    @GET("kilLastProcess")
    Call<Response<Object>> kilLastProcess();

}
