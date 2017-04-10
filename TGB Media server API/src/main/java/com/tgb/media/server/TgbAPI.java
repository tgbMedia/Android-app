package com.tgb.media.server;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.tgb.media.server.deserialize.ResponseDeserializer;
import com.tgb.media.server.models.MovieFile;

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

public class TgbAPI {

    //OkHttp
    private OkHttpClient okHttpClient;

    //Retrofit
    private Retrofit retrofit;
    private TgbService service;

    //Api
    private String baseUrl;
    private String apiKey;


    public TgbAPI(String tgbBaseUrl, String tgbApiKey)
    {
        //API Auth
        this.baseUrl = tgbBaseUrl;
        this.apiKey = tgbApiKey;

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
        //gsonBuilder.registerTypeAdapter(MovieFile.class, new ResponseDeserializer<MovieFile>());

        //Retrofit initialize
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(TgbService.class);
    }

    public TgbService call(){
        return service;
    }

}
