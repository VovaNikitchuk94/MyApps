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
import com.example.vova.phonefilter.model.Subscriber;
import com.example.vova.phonefilter.model.SubscriberDBWrapper;

public class CallWindowService extends Service implements SeekBar.OnSeekBarChangeListener,
        View.OnClickListener {

    private static WindowManager windowManager;
    private ViewGroup windowLayout;
    private TextView mTextViewName, mTextViewNumber, mTextViewCurrencyTimer,
            mTextCancel, mTextIgnore, mTextViewIgnoreAndSMS;
    private SeekBar mSeekBarduwnTimer;

    private int mMinutesTime;
    private String mSubNumber = null;
    private String mSubName = null;
    private Subscriber mSubscriber = null;

    public static final String KEY_NUMBER_SUBSCRIBER_CALL_WINDOW = "KEY_NUMBER_SUBSCRIBER_CALL_WINDOW";

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
        mSubNumber = intent.getStringExtra(KEY_NUMBER_SUBSCRIBER_CALL_WINDOW);
        Log.d("vDev", "CallWindowService  onStartCommand mSubNumber -> " + mSubNumber);

        getSubInfo();
//        showWindow(this);
        initViews();
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
        windowManager.addView(windowLayout, params);
    }

    private void initViews() {
        //find views
        mTextViewName = (TextView) windowLayout.findViewById(R.id.textViewContactNameWindowAfterCall);
        mTextViewNumber = (TextView) windowLayout.findViewById(R.id.textViewContactNumberWindowAfterCall);
        mTextViewCurrencyTimer = (TextView) windowLayout.findViewById(R.id.textViewTimerCurrencyTextWindowAfterCall);
        mTextCancel = (TextView) windowLayout.findViewById(R.id.textViewCancelTextWindowAfterCall);
        mTextIgnore = (TextView) windowLayout.findViewById(R.id.textViewOnlyIgnoreTextWindowAfterCall);
        mTextViewIgnoreAndSMS = (TextView) windowLayout.findViewById(R.id.textViewIgnoreAndSendSMSTextWindowAfterCall);
        mSeekBarduwnTimer = (SeekBar) windowLayout.findViewById(R.id.seekBarMinutesDownTimerWindowAfterCall);

        if (mSubName != null) {
            mTextViewName.setText(mSubName);
        } else {
            mTextViewName.setText("Unknown subscriber");
        }
        mTextViewNumber.setText(mSubNumber);
        Log.d("vDev", "CallWindowService  mSubName -> " + mSubName);
        Log.d("vDev", "CallWindowService  subNumber -> " + mSubNumber);

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

    private void getSubInfo() {
        SubscriberDBWrapper subscriberDBWrapper = new SubscriberDBWrapper(getApplicationContext());
        if (mSubNumber != null) {
            mSubscriber = subscriberDBWrapper.getSubscriberByNumber(mSubNumber);
        }
        if (mSubscriber != null) {
            mSubName = mSubscriber.getSubscriberName();
            mSubNumber = mSubscriber.getSubscriberNumber();
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
        SubscriberDBWrapper subscriberDBWrapper = new SubscriberDBWrapper(getApplicationContext());
        Subscriber subscriber = null;
        if (mSubNumber != null) {
            subscriber = subscriberDBWrapper.getSubscriberByNumber(mSubNumber);
            subscriber.setIsBlackListAdded(1);
        }
        switch (v.getId()) {
            case R.id.textViewCancelTextWindowAfterCall:
                Log.d("vDev", "CallWindowService  onClick(View v");
                closeWindow();
                stopSelf();
                break;
            case R.id.textViewOnlyIgnoreTextWindowAfterCall:
                Log.d("vDev", "CallWindowService  textViewOnlyIgnoreTextWindowAfterCall -> " );
                if (!(getMinutesTime() == 0) && subscriber != null) {
                    subscriberDBWrapper.updateSubscriber(subscriber);
                    blockCallServiceIntent = new Intent(this, BlockCallService.class);
                    blockCallServiceIntent.putExtra(BlockCallService.KEY_ID_SUBSCRIBER_BLOCK_CALL,
                            subscriber.getId());
                    blockCallServiceIntent.putExtra(BlockCallService.KEY_MINUTES_TIMER_BLOCK_CALL,
                            getMinutesTime());
                    getApplicationContext().startService(blockCallServiceIntent);
                }
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
