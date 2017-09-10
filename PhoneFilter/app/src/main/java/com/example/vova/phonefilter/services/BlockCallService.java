package com.example.vova.phonefilter.services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class BlockCallService extends Service {

    public static final String KEY_MINUTES_TIMER = "KEY_MINUTES_TIMER";
    private int mMinutesTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("vDev", "BlockCallService  onStartCommand");
        mMinutesTimer = intent.getIntExtra(KEY_MINUTES_TIMER, 1);
        workTimer(mMinutesTimer);
        Log.d("vDev", "BlockCallService  onStartCommand mMinutesTimer -> " + mMinutesTimer);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("vDev", "BlockCallService  onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("vDev", "BlockCallService  onDestroy");
    }

    private boolean workTimer(int minutes) {

        int timeInSec = minutes * 60 * 1000;
        CountDownTimer mCountDownTimer = new CountDownTimer(timeInSec, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.d("vDev", "BlockCallService workTimer onTick -> " + millisUntilFinished/1000);
            }

            public void onFinish() {
                Log.d("vDev", "BlockCallService  workTimer onFinish ->");

            }
        }.start();



        return false;
    }
}
