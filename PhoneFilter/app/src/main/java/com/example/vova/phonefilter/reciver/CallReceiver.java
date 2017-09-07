package com.example.vova.phonefilter.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.example.vova.phonefilter.services.CallWindowService;

import java.lang.reflect.Method;

public class CallReceiver extends BroadcastReceiver {

    public static final String MY_NUMBER = "+380993183634";

    private static boolean mIncomingCall = false;
    private static String mIncomingNumber = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {

            String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            //
            if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                mIncomingCall = true;
                mIncomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

//                Log.d("My", "CallReceiver  RINGING");
//                showWindow(context, mIncomingNumber);


//                if (mIncomingNumber.equals(MY_NUMBER)) {
//                    Log.d("My", "CallReceiver mIncomingNumber.equals(MY_NUMBER) -> " + mIncomingNumber.equals(MY_NUMBER));
//                    disconnectPhoneItelephony(context);
//                }

                context.startService(new Intent(context, CallWindowService.class));

            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

                if (mIncomingCall) {
//                    closeWindow();
//                    Log.d("My", "CallReceiver  OFFHOOK");
                    mIncomingCall = false;
                }
            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                if (mIncomingCall) {
//                    closeWindow();
//                    Log.d("My", "CallReceiver  IDLE");
                    mIncomingCall = false;
                }
            }
        }
    }

    private void disconnectPhoneItelephony(Context context)
    {
        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        try
        {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);
            telephonyService.endCall();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
