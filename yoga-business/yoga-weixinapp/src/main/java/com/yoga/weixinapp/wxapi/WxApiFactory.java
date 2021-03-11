package com.yoga.weixinapp.wxapi;

import com.yoga.core.data.api.ConverterFactory;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class WxApiFactory {
    private static final Logger logger = LoggerFactory.getLogger(WxApiFactory.class);

    @Getter
    private WxApi wxApi;

    @PostConstruct
    public void init() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor((String message) -> logger.info(message));
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(logInterceptor)
                .build();
        wxApi = new Retrofit.Builder()
                .baseUrl("https://api.weixin.qq.com")
                .client(client)
                .addConverterFactory(ConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(WxApi.class);
    }
}
