package com.ritikrishu.weatherapp;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ritikrishu on 25/05/16.
 */
public class CurrentWeather {
    @SerializedName("currently")
    currently mCurrently;
    public class currently {
        @SerializedName("time")
        private long mTime ;
        @SerializedName("summary")
        private String mSummary;
        @SerializedName("icon")
        private String mIcon;
        @SerializedName("precipProbability")
        private double mPercipChance;
        @SerializedName("temperature")
        private double mTemperature;
        @SerializedName("humidity")
        private double mHumidity;

        currently(){
            mCurrently = this;
        }
        public String getTimeZone() {
            return mTimeZone;
        }

        public void setTimeZone(String timeZone) {
            mTimeZone = timeZone;
        }

        private String mTimeZone;

        public int getHumidity() {
            return (int) Math.round(mHumidity * 100);
        }

        public void setHumidity(double humidity) {
            mHumidity = humidity;
        }

        public int getTemperature() {
            return (int) Math.round((mTemperature - 32) * 5 / 9);
        }

        public void setTemperature(double temperature) {
            mTemperature = temperature;
        }

        public double getPercipChance() {
            return mPercipChance;
        }

        public void setPercipChance(double percipChance) {
            mPercipChance = percipChance;
        }

        public String getCurrentTime() {
            SimpleDateFormat format = (SimpleDateFormat)DateFormat.getTimeInstance();
            return format.format(new Date(mTime * 1000));
        }

        public void setTime(long time) {
            mTime = time;
        }

        public String getSummary() {
            return mSummary;
        }

        public void setSummary(String summary) {
            mSummary = summary;
        }

        public String getIcon() {
            return mIcon;
        }

        public void setIcon(String icon) {
            mIcon = icon;
        }

        public int getIconID() {
            int iconId = R.drawable.clear_day;
            ;
            if (mIcon.equals("clear-day")) {
                iconId = R.drawable.clear_day;
            } else if (mIcon.equals("clear-night")) {
                iconId = R.drawable.clear_night;
            } else if (mIcon.equals("rain")) {
                iconId = R.drawable.rain;
            } else if (mIcon.equals("snow")) {
                iconId = R.drawable.snow;
            } else if (mIcon.equals("sleet")) {
                iconId = R.drawable.sleet;
            } else if (mIcon.equals("wind")) {
                iconId = R.drawable.wind;
            } else if (mIcon.equals("fog")) {
                iconId = R.drawable.fog;
            } else if (mIcon.equals("cloudy")) {
                iconId = R.drawable.cloudy;
            } else if (mIcon.equals("partly-cloudy-day")) {
                iconId = R.drawable.partly_cloudy;
            } else if (mIcon.equals("partly-cloudy-night")) {
                iconId = R.drawable.cloudy_night;
            }
            return iconId;
        }
    }
    public currently getCurrently(){
        return mCurrently;
    }
}
