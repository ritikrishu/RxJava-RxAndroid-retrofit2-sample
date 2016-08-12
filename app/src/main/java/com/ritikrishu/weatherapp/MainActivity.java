/*
    This is not the best implementation but it is simple and yet Reactive ;)
 */

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
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
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
    CompositeSubscription cs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mSummaryLabel.setText("Fetching current data...");
        cs = new CompositeSubscription();
        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNetworkOpp();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        doNetworkOpp();
    }

    Observable mainObservable = null;


    private void doNetworkOpp() {
        refreshing = true;
        decideProgress();
        if (mainObservable == null) {
            mainObservable = Observable.defer(new Func0<Observable<CurrentWeather.currently>>() {
                @Override
                public Observable<CurrentWeather.currently> call() {
                    try {
                        return Observable.just(true)
                                /*just to emit an stream to start doing work like network connectivity check on a worker thread
                                                                                                                     **Yes Rx is awesome in handling concurrency :)
                                */
                                .filter(new Func1<Boolean, Boolean>() {
                                    @Override
                                    public Boolean call(Boolean aBoolean) {
                                        return isNetworkAvailable();
                                    }
                                }).flatMap(new Func1<Boolean, Observable<CurrentWeather.currently>>() {
                                    @Override
                                    public Observable<CurrentWeather.currently> call(Boolean aBoolean) {
                                        return Service.hitRetro().currentWeather("28.524579", "77.206615")
                                                .flatMap(new Func1<CurrentWeather, Observable<CurrentWeather.currently>>() {
                                                    @Override
                                                    public Observable<CurrentWeather.currently> call(CurrentWeather currentWeather) {
                                                        return Observable.just(currentWeather.getCurrently());
                                                    }
                                                });
                                    }
                                });
                    } catch (Exception e) {
                        return Observable.error(e);
                    }
                }
            })
                    /*
                        all filters, flatmaps and other operators processing is done on IO scheduler reducing the work on UI thread
                        even if we wouldn't have used retrofit2 and done network call in operator function, IO thread would have handled it perfectly
                     */
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());//all observers that we pass in .subscribe() function will be executed on Main UI Thread
        }
        cs.add(
                mainObservable.subscribe(
                        new Action1<CurrentWeather.currently>() {
                            @Override
                            public void call(CurrentWeather.currently currently) {
                                updateDisplay(currently);
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // always override on error, or there might be a scenario where you app will blow up
                            }
                        },
                        new Action0() {
                            @Override
                            public void call() {
                                //hide progress on complete
                                refreshing = false;
                                decideProgress();
                            }
                        }));
    }

    //wraped in function to change value of non final variable from anonymous class function
    private void toggleRefrest() {
        refreshing = !refreshing;
    }

    //show hide progressbar
    private void decideProgress() {
        if (refreshing) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    //update UI
    private void updateDisplay(CurrentWeather.currently currently) {
        mTemperatureLabel.setText(currently.getTemperature() + "");
        mHumidityValue.setText(currently.getHumidity() + "%");
        mIconImageView.setImageResource(currently.getIconID());
        mPrecipValue.setText(currently.getPercipChance() + "");
        mSummaryLabel.setText(currently.getSummary());
        mTimeLabel.setText("At " + currently.getCurrentTime() + " it will be");
    }

    //check if device has internet connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //un subscribe from all subscriptions made
        cs.unsubscribe();
    }
}
