package com.hannan.kevin.api;


import com.hannan.kevin.model.ItemsList;
import com.hannan.kevin.nprapp2.AccessToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by kehannan on 6/6/16.
 */
public interface LoginService {

    @FormUrlEncoded
    @POST("/authorization/v2/token")
    Call<AccessToken> getAccessToken(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code,
            @Field("redirect_uri") String redirectUrl);

    @GET("/listening/v2/recommendations")
    Call<ItemsList> getRecommendations(
            @Header("Authorization") String token);
}
