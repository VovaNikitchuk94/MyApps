package com.example.vova.phonefilter.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.vova.phonefilter.R;

public class CallWindowService extends Service implements SeekBar.OnSeekBarChangeListener,
        View.OnClickListener {

    private static WindowManager windowManager;
    private ViewGroup windowLayout;
    private TextView mTextViewName, mTextViewNumber, mTextViewCurrencyTimer,
            mTextCancel, mTextIgnore, mTextViewIgnoreAndSMS;
    private SeekBar mSeekBarduwnTimer;

    private int mMinutesTime;

    public int getMinutesTime() {
        return mMinutesTime;
    }

    public void setMinutesTime(int minutesTime) {
        mMinutesTime = minutesTime;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("vDev", "CallWindowService  onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.d("vDev", "CallWindowService  onCreate");
        super.onCreate();

        showWindow(this);
    }

    @Override
    public void onDestroy() {
        Log.d("vDev", "CallWindowService  onDestroy");

//        closeWindow();
        super.onDestroy();
    }

    @Override
    public boolean stopService(Intent name) {
        Log.d("vDev", "CallWindowService  stopService");

        return super.stopService(name);
    }

    private void showWindow(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP;

        windowLayout = (ViewGroup) layoutInflater.inflate(R.layout.window_after_call, null);

        workWithViews();

        windowManager.addView(windowLayout, params);
    }

    private void workWithViews() {
        //find views
        mTextViewName = (TextView) windowLayout.findViewById(R.id.textViewContactNameWindowAfterCall);
        mTextViewNumber = (TextView) windowLayout.findViewById(R.id.textViewContactNumberWindowAfterCall);
        mTextViewCurrencyTimer = (TextView) windowLayout.findViewById(R.id.textViewTimerCurrencyTextWindowAfterCall);
        mTextCancel = (TextView) windowLayout.findViewById(R.id.textViewCancelTextWindowAfterCall);
        mTextIgnore = (TextView) windowLayout.findViewById(R.id.textViewOnlyIgnoreTextWindowAfterCall);
        mTextViewIgnoreAndSMS = (TextView) windowLayout.findViewById(R.id.textViewIgnoreAndSendSMSTextWindowAfterCall);
        mSeekBarduwnTimer = (SeekBar) windowLayout.findViewById(R.id.seekBarMinutesDownTimerWindowAfterCall);

        mSeekBarduwnTimer.setOnSeekBarChangeListener(this);
        mTextCancel.setOnClickListener(this);
        mTextIgnore.setOnClickListener(this);
        mTextViewIgnoreAndSMS.setOnClickListener(this);
    }

    private void closeWindow() {
        if (windowLayout != null) {
            windowManager.removeView(windowLayout);
            windowLayout = null;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d("vDev", "CallWindowService  setOnSeekBarChangeListener onProgressChanged -> " + seekBar.getProgress());
        mTextViewCurrencyTimer.setText(String.valueOf(seekBar.getProgress()) + " min");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.d("vDev", "CallWindowService  setOnSeekBarChangeListener onStartTrackingTouch -> " + seekBar.getProgress());

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d("vDev", "CallWindowService  setOnSeekBarChangeListener onStopTrackingTouch -> " + seekBar.getProgress());
        setMinutesTime(seekBar.getProgress());
        mTextViewCurrencyTimer.setText(String.valueOf(seekBar.getProgress()) + " min");
    }

    @Override
    public void onClick(View v) {
        Intent blockCallServiceIntent;
        switch (v.getId()) {
            case R.id.textViewCancelTextWindowAfterCall:
                Log.d("vDev", "CallWindowService  onClick(View v");
                closeWindow();
                stopSelf();
                break;
            case R.id.textViewOnlyIgnoreTextWindowAfterCall:
                Log.d("vDev", "CallWindowService  textViewOnlyIgnoreTextWindowAfterCall -> " );
                if (!(getMinutesTime() == 0)) {
                    blockCallServiceIntent = new Intent(this, BlockCallService.class);
                    blockCallServiceIntent.putExtra(BlockCallService.KEY_MINUTES_TIMER, getMinutesTime());
                    getApplicationContext().startService(blockCallServiceIntent);
                }
//                getApplicationContext().startService(new Intent(this, BlockCallService.class));
                closeWindow();
                stopSelf();
                break;
            case R.id.textViewIgnoreAndSendSMSTextWindowAfterCall:
                Log.d("vDev", "CallWindowService  textViewIgnoreAndSendSMSTextWindowAfterCall -> ");
                closeWindow();
                stopSelf();
                break;
            default:
                closeWindow();
                stopSelf();
                break;
        }
    }
}
