package com.example.vova.phonefilter.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.example.vova.phonefilter.model.Subscriber;
import com.example.vova.phonefilter.model.SubscriberDBWrapper;
import com.example.vova.phonefilter.services.CallWindowService;

import java.lang.reflect.Method;

public class CallReceiver extends BroadcastReceiver {

    private static boolean mIncomingCall = false;
    private static String mIncomingNumber = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("vDev", " CallReceiver onReceive");

        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Log.d("vDev", "CallReceiver EXTRA_STATE_RINGING");
                mIncomingCall = true;
                mIncomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d("vDev", "CallReceiver mIncomingNumber -> " +  mIncomingNumber);

                SubscriberDBWrapper subscriberDBWrapper = new SubscriberDBWrapper(context);
                Subscriber subscriber = subscriberDBWrapper.getSubscriberByNumber(mIncomingNumber);
                if (subscriber != null) {
//                    Log.d("vDev", "CallReceiver subscriber != null!");
//                    String sumNumber = subscriber.getSubscriberNumber();
//                    if (sumNumber.length() >= 10) {
//                        Log.d("vDev", "CallReceiver numberLenght -> " + sumNumber.length());
//                        int numberLenght = sumNumber.length();
//                        sumNumber = sumNumber.substring(numberLenght - 10);
//                        Log.d("vDev", "CallReceiver sumNumber -> " + sumNumber);
//                    }
                    if (mIncomingNumber.contains(subscriber.getSubscriberNumber())) {
                        Log.d("vDev", "CallReceiver mIncomingNumber.contains(subscriber.getSubscriberNumber() -> "
                                + mIncomingNumber.contains(subscriber.getSubscriberNumber()));
                        disconnectPhoneItelephony(context);
                        mIncomingCall = false;
                    }
                }
            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                if (mIncomingCall) {
                    Log.d("vDev", "CallReceiver  OFFHOOK");
                    mIncomingCall = false;
                }
            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                if (mIncomingCall) {
                    Intent callWinIntent = new Intent(context, CallWindowService.class);
                    callWinIntent.putExtra(CallWindowService.KEY_NUMBER_SUBSCRIBER_CALL_WINDOW, mIncomingNumber);
                    context.startService(callWinIntent);
                    Log.d("vDev", "CallReceiver IDLE mIncomingNumber -> " +  mIncomingNumber);

                    SubscriberDBWrapper subscriberDBWrapper = new SubscriberDBWrapper(context);
                    subscriberDBWrapper.addToBlackList(new Subscriber("Unknown", mIncomingNumber, 0));
                    Log.d("vDev", "CallReceiver  IDLE true");
                    mIncomingCall = false;
                }
            }
        }
    }

    //some magic
    private void disconnectPhoneItelephony(Context context) {
        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);
            telephonyService.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
