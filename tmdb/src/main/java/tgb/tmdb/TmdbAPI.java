package tgb.tmdb;

import android.util.Log;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import tgb.tmdb.deserialize.ReleaseDateDeserializer;
import tgb.tmdb.deserialize.TrailerDeserialize;
import tgb.tmdb.models.Movie;
import tgb.tmdb.models.MovieOverview;
import tgb.tmdb.models.ReleaseDate;
import tgb.tmdb.models.Search;
import tgb.tmdb.models.Trailer;

public class TmdbAPI {

    //OkHttp
    private OkHttpClient okHttpClient;

    //Retrofit
    private Retrofit retrofit;
    private TmdbService service;

    //Api
    private String apiKey;
    private String language;

    //Dates
    private DateFormat releaseDateFormat;

    public TmdbAPI(String tmdbApiKey, final String apiLanguage)
    {
        //API Auth
        this.apiKey = tmdbApiKey;
        this.language = apiLanguage.substring(0, 2); //Convert to ISO 639-1(2 letters)

        //Dates
        this.releaseDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

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

        //GSON
        GsonBuilder gsonBuilder = new GsonBuilder();

        //GSON Adapters
        gsonBuilder.registerTypeAdapter(Trailer.class, new TrailerDeserialize(language));
        gsonBuilder.registerTypeAdapter(ReleaseDate.class, new ReleaseDateDeserializer(releaseDateFormat));

        //Retrofit initialize
        retrofit = new Retrofit.Builder()
                .baseUrl(TmdbService.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(TmdbService.class);
    }

    public MovieOverview searchMovieByName(final String movieName, final int releaseYear) throws Exception{
        //Search
        retrofit2.Response<Search> searchResponse = call()
                        .search(movieName, 1)
                        .execute();

        if(!searchResponse.isSuccessful() || searchResponse.body().results.size() < 1)
            return null;

        /*Collections.sort(searchResponse.body().results, new Comparator<Movie>() {
            @Override
            public int compare(Movie left, Movie right) {
                boolean leftIsSameName = left.title.equals(movieName);
                boolean rightIsSameName = right.title.equals(movieName);

                if(leftIsSameName && rightIsSameName)
                {
                    //Same title, compare by release date
                    Boolean leftIsSameYear = left.relaseDate.getYear() == releaseYear;
                    Boolean rightIsSameYear = right.relaseDate.getYear() == releaseYear;

                    return leftIsSameYear.compareTo(rightIsSameYear);
                }

                return leftIsSameName ? -1 : 1;
            }
        });*/

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
