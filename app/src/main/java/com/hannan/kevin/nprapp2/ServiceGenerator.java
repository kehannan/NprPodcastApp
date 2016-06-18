package com.hannan.kevin.nprapp2;

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

//    public static <S> S createService(Class<S> serviceClass, String clientId,
//                                      String clientSecret, String code, String redirectUrl) {
//
//        Retrofit retrofit = builder.client(httpClient.build()).build();
//    }

//    public static <S> S createService(Class<S> serviceClass, String username, String password) {
//        // we shortened this part, because it’s covered in
//        // the previous post on basic authentication with Retrofit
//    }

//    public static <S> S createService(Class<S> serviceClass, AccessToken token) {
//        if (token != null) {
//            httpClient.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Interceptor.Chain chain) throws IOException {
//                    Request original = chain.request();
//
//                    Request.Builder requestBuilder = original.newBuilder()
//                            .header("Accept", "application/json")
//                            .header("Authorization",
//                                    token.getTokenType() + " " + token.getAccessToken())
//                            .method(original.method(), original.body());
//
//                    Request request = requestBuilder.build();
//                    return chain.proceed(request);
//                }
//            });
//        }

//        OkHttpClient client = httpClient.build();
//        Retrofit retrofit = builder.client(client).build();
//        return retrofit.create(serviceClass);
//    }
}