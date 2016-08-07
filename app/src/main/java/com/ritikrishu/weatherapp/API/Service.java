package com.ritikrishu.weatherapp.API;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ritikrishu on 26/05/16.
 */
public class Service {
    public static API.GETDATA hitRetro(){

        Retrofit.Builder builder = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API.BASE_URL);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .build();
                        return chain.proceed(request);
                    }
                }).build();
        builder.client(client);

        return builder
                .build()
                .create(API.GETDATA.class);
    }
}
