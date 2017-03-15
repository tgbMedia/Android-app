package tgb.tmdb;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import tgb.tmdb.models.MovieOverview;
import tgb.tmdb.models.Search;

public class TmdbAPI {

    //OkHttp
    private OkHttpClient okHttpClient;

    //Retrofit
    private Retrofit retrofit;
    private TmdbService service;

    //Api
    private String apiKey;
    private String language;

    //Finals
    private static final String SEARCH = "search/movie/";

    public TmdbAPI(String tmdbApiKey, final String apiLanguage)
    {
        //API Auth
        this.apiKey = tmdbApiKey;
        this.language = apiLanguage.substring(0, 2); //Convert to ISO 639-1(2 letters)

        //Create new instance of OkHttpClient to add the api_key parameter to every request
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        okHttpClientBuilder.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                HttpUrl url = request
                        .url()
                        .newBuilder()
                        .addQueryParameter("api_key", apiKey)
                        .addQueryParameter("language", language)
                        .build();

                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        });

        //Logging
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override public void log(String message) {
                //Timber.tag("OkHttp").d(message);
                Log.i("retrofitlog", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        okHttpClientBuilder.addInterceptor(logging);

        okHttpClient = okHttpClientBuilder.build();

        //Retrofit initialize
        retrofit = new Retrofit.Builder()
                .baseUrl(TmdbService.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(TmdbService.class);
    }

    public String getLanguage(){
        return this.language;
    }

    public MovieOverview searchMovieByName(final String movieName) throws Exception{
        //Search
        retrofit2.Response<Search> searchResponse = call()
                        .search(movieName, 1)
                        .execute();

        if(!searchResponse.isSuccessful() || searchResponse.body().results.size() < 1)
            return null;

        //Movie overview
        retrofit2.Response<MovieOverview> movieDiscoverResponse = call()
                        .movie(searchResponse.body().results.get(0).id)
                        .execute();

        if(!movieDiscoverResponse.isSuccessful())
            return null;

        return movieDiscoverResponse.body();
    }

    public TmdbService call(){
        return service;
    }
}
