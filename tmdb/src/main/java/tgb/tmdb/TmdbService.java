package tgb.tmdb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tgb.tmdb.models.MovieOverview;
import tgb.tmdb.models.Search;

public interface TmdbService {

    String BASE_URL = "https://api.themoviedb.org/3/";

    //@GET("search/movie")
    //Observable<Search> search(@Query("query") String moviesName, @Query("page") int page);

    @GET("search/movie")
    Call<Search> search(@Query("query") String moviesName, @Query("page") int page);

    @GET("movie/{id}?append_to_response=credits,images,keywords,videos")
    Call<MovieOverview> movie(@Path("id") long movieId);


}
