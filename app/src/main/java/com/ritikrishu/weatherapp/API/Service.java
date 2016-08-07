package com.ritikrishu.weatherapp.API;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ritikrishu on 26/05/16.
 */
public class Service {
    public static API.GETDATA hitRetro(){
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request request = chain.request()
//                                .newBuilder()
//                                .build();
//                        return chain.proceed(request);
//                    }
//                }).build();

        return new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(API.GETDATA.class);
    }
}
