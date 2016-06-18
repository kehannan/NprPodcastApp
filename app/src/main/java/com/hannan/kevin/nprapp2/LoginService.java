package com.hannan.kevin.nprapp2;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
}
