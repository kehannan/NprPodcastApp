package com.hannan.kevin.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by kehannan on 6/6/16.
 */
public class ServiceGenerator {

    public static final String API_BASE_URL = "https://api.npr.org/";


    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);


    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder().addInterceptor(logging);

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}