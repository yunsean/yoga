package com.yoga.weixinapp.wxapi;

import com.yoga.core.data.api.RequestFormat;
import com.yoga.core.data.api.ResponseFormat;
import org.springframework.web.bind.annotation.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WxApi {

    @GET("/sns/jscode2session")
    @RequestFormat(RequestFormat.RAW)
    @ResponseFormat(ResponseFormat.JSON)
    Call<WxSessionResult> getSession(@Query("appid") String appid, @Query("secret") String secret, @Query("js_code") String code, @Query("grant_type") String type /*= "authorization_code"*/);

    @GET("/cgi-bin/token")
    @RequestFormat(RequestFormat.RAW)
    @ResponseFormat(ResponseFormat.JSON)
    Call<WxTokenResult> getAccessToken(@Query("appid") String appid, @Query("secret") String secret, @Query("grant_type") String type /*= "client_credential"*/);

    @POST("/cgi-bin/message/subscribe/send")
    @RequestFormat(RequestFormat.JSON)
    @ResponseFormat(ResponseFormat.JSON)
    Call<WxBaseResult> sendSubscribe(@Query("access_token") String token, @Body WxSendSubscribeRequest request);
}
