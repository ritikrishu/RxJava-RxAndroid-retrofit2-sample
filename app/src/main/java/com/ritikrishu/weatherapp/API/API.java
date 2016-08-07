package com.ritikrishu.weatherapp.API;

import com.ritikrishu.weatherapp.CurrentWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ritikrishu on 26/05/16.
 */
public interface API {
    String BASE_URL = "https://api.forecast.io/forecast/67245e2b8637f11582f80eb024d2cefe/"; //  /67245e2b8637f11582f80eb024d2cefe/28.524579,77.206615";
    String CLIENT_ID = "67245e2b8637f11582f80eb024d2cefe";

    interface GETDATA{
        @GET("{latitude},{longitude}")
        Call<CurrentWeather> currentWeather(@Path("latitude") String latitude,
                                            @Path("longitude") String longitude);
    }
}
