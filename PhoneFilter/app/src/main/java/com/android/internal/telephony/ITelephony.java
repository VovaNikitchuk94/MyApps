package com.android.internal.telephony;

public interface ITelephony {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                    double aDouble, String aString);
    boolean endCall();
    void silenceRinger();
    void answerRingingCall();
}
