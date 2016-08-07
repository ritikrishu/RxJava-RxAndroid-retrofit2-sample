package com.ritikrishu.weatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ritikrishu.weatherapp.API.Service;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    CurrentWeather mCurrentWeather;
    @BindView(R.id.timeLabel) TextView mTimeLabel;
    @BindView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @BindView(R.id.humidityValue) TextView mHumidityValue;
    @BindView(R.id.precipValue) TextView mPrecipValue;
    @BindView(R.id.summaryLabel) TextView mSummaryLabel;
    @BindView(R.id.iconImageView) ImageView mIconImageView;
    @BindView(R.id.refresh) ImageView mRefreshImageView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    private boolean refreshing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        decideProgress();
        retrofitRequest();
        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshing = true;
                decideProgress();
                updateDisplay();
                mSummaryLabel.setText("Fetching current data...");
                retrofitRequest();
            }
        });
    }


    private void retrofitRequest() {

        final String lat = "28.524579";
        final String lon = "77.206615";
        Service.hitRetro().currentWeather(lat, lon).enqueue(new retrofit2.Callback<CurrentWeather>() {
            @Override
            public void onResponse(retrofit2.Call<CurrentWeather> call, retrofit2.Response<CurrentWeather> response) {
                refreshing = false;
                decideProgress();
                try {
                    if (response.isSuccessful()) {
                        mCurrentWeather = response.body();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateDisplay();
                            }
                        });
                    } else {
                        new AlertDailogueFragment().show(getSupportFragmentManager(), AlertDailogueFragment.TAG);
                    }
                }

                catch (Exception e){

                }
            }

            @Override
            public void onFailure(retrofit2.Call<CurrentWeather> call, Throwable t) {
                refreshing = false;
                decideProgress();
                new AlertDailogueFragment().show(getSupportFragmentManager(), AlertDailogueFragment.TAG);
            }
        });
    }



    private void decideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(refreshing){
                    mProgressBar.setVisibility(View.VISIBLE);
                    mRefreshImageView.setVisibility(View.INVISIBLE);
                }
                else{
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mRefreshImageView.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    private void updateDisplay() {
        CurrentWeather.currently currently = mCurrentWeather.getCurrently();
        mTemperatureLabel.setText(currently.getTemperature() + "");
        mHumidityValue.setText(currently.getHumidity() + "%");
        mIconImageView.setImageResource(currently.getIconID());
        mPrecipValue.setText(currently.getPercipChance() + "");
        mSummaryLabel.setText(currently.getSummary());
        mTimeLabel.setText( "At " + currently.getCurrentTime() + " it will be");
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }
}
