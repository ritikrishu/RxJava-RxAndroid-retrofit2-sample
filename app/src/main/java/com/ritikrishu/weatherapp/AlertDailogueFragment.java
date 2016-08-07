package com.ritikrishu.weatherapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by ritikrishu on 25/05/16.
 */
public class AlertDailogueFragment extends DialogFragment {
    public static final String TAG = AlertDailogueFragment.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setTitle(getResources().getString(R.string.error_app))
                                                                        .setMessage(getResources().getString(R.string.network_error))
                                                                        .setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();

        return dialog;
    }
}
