package com.example.vova.phonefilter.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.example.vova.phonefilter.R;


import io.fabric.sdk.android.Fabric;

public class PhoneFilterActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_phone_filter);
    }
}
