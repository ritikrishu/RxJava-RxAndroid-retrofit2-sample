package com.ritikrishu.weatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ritikrishu.weatherapp.API.Service;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    CurrentWeather mCurrentWeather;
    @BindView(R.id.timeLabel)
    TextView mTimeLabel;
    @BindView(R.id.temperatureLabel)
    TextView mTemperatureLabel;
    @BindView(R.id.humidityValue)
    TextView mHumidityValue;
    @BindView(R.id.precipValue)
    TextView mPrecipValue;
    @BindView(R.id.summaryLabel)
    TextView mSummaryLabel;
    @BindView(R.id.iconImageView)
    ImageView mIconImageView;
    @BindView(R.id.refresh)
    ImageView mRefreshImageView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private boolean refreshing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        decideProgress();
//        retrofitRequest();
//        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                refreshing = true;
//                decideProgress();
//                updateDisplay();
//                mSummaryLabel.setText("Fetching current data...");
//                retrofitRequest();
//            }
//        });
        refreshing = true;
        decideProgress();
        mSummaryLabel.setText("Fetching current data...");
        //WeatherService weatherService = retrofit.create(WeatherService.class);
        
        Service.hitRetro().currentWeather("28.524579", "77.206615")
        .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<CurrentWeather, Observable<CurrentWeather.currently>>() {
                    @Override
                    public Observable<CurrentWeather.currently> call(CurrentWeather currentWeather) {
                        return Observable.just(currentWeather.getCurrently());
                    }
                })
                .subscribe(new Action1<CurrentWeather.currently>() {
                    @Override
                    public void call(CurrentWeather.currently currently) {
                        refreshing = false;
                        decideProgress();
                        updateDisplay(currently);
                    }
                });


    }

//    private void retrofitRequest() {
//
//        final String lat = "28.524579";
//        final String lon = "77.206615";
//        Service.hitRetro().currentWeather(lat, lon).enqueue(new retrofit2.Callback<CurrentWeather>() {
//            @Override
//            public void onResponse(retrofit2.Call<CurrentWeather> call, retrofit2.Response<CurrentWeather> response) {
//                refreshing = false;
//                decideProgress();
//                try {
//                    if (response.isSuccessful()) {
//                        mCurrentWeather = response.body();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                updateDisplay();
//                            }
//                        });
//                    } else {
//                        new AlertDailogueFragment().show(getSupportFragmentManager(), AlertDailogueFragment.TAG);
//                    }
//                }
//
//                catch (Exception e){
//
//                }
//            }
//
//            @Override
//            public void onFailure(retrofit2.Call<CurrentWeather> call, Throwable t) {
//                refreshing = false;
//                decideProgress();
//                new AlertDailogueFragment().show(getSupportFragmentManager(), AlertDailogueFragment.TAG);
//            }
//        });
//    }


    private void decideProgress() {
        if (refreshing) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }


    private void updateDisplay(CurrentWeather.currently currently) {
        mTemperatureLabel.setText(currently.getTemperature() + "");
        mHumidityValue.setText(currently.getHumidity() + "%");
        mIconImageView.setImageResource(currently.getIconID());
        mPrecipValue.setText(currently.getPercipChance() + "");
        mSummaryLabel.setText(currently.getSummary());
        mTimeLabel.setText("At " + currently.getCurrentTime() + " it will be");
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
