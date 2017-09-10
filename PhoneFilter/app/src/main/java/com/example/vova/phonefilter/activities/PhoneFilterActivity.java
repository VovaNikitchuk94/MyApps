package com.example.vova.phonefilter.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.example.vova.phonefilter.BuildConfig;
import com.example.vova.phonefilter.R;
import com.example.vova.phonefilter.adapters.SubscriberAdapter;
import com.example.vova.phonefilter.model.Subscriber;
import com.example.vova.phonefilter.model.SubscriberDBWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.fabric.sdk.android.Fabric;

public class PhoneFilterActivity extends AppCompatActivity implements SubscriberAdapter.OnClickSubscriberItem{

    private RecyclerView mRecyclerViewBlackList;
    private TextView mTextViewEmptyListInfo;
    private FloatingActionButton mFloatingActionButtonAdd;

    private ArrayList<Subscriber> mSubscribersList = new ArrayList<>();

    private SubscriberAdapter mAdapter;

//    private CountDownTimer mCountDownTimer;
//    private TextView mTextView1;
//    private Button mButton1;
//    private Button mButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());
        setContentView(R.layout.activity_phone_filter);

        mTextViewEmptyListInfo = (TextView) findViewById(R.id.textViewInfoActivityPhoneFilter);

        mFloatingActionButtonAdd = (FloatingActionButton) findViewById(R.id.fabAddNewNumberToBlackListActivityPhoneFilter);
        mFloatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("vDev", "PhoneFilterActivity onCreate onClick -> ");
                openDialogInputNumber();
            }
        });

        mRecyclerViewBlackList = (RecyclerView) findViewById(R.id.recyclerViewBlackListActivityPhoneFilter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewBlackList.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerViewBlackList.getContext(),
                layoutManager.getOrientation());
        mRecyclerViewBlackList.addItemDecoration(dividerItemDecoration);

        getData();


//        mButton1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("vDev", "PhoneFilterActivity onCreate onClick mButton1 -> ");
//
//                mCountDownTimer = new CountDownTimer(30000, 1000) {
//
//                    public void onTick(long millisUntilFinished) {
//                        mTextView1.setText("seconds remaining: " + millisUntilFinished / 1000);
//                    }
//
//                    public void onFinish() {
//                        mTextView1.setText("done!");
//                    }
//                }.start();
//
//            }
//        });
//
//        mButton2 = (Button) findViewById(R.id.button2);
//        mButton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("vDev", "PhoneFilterActivity onCreate onClick mButton2 -> ");
//
//                mCountDownTimer.cancel();
//            }
//        });

    }

    private void getData() {
        mSubscribersList.clear();
        SubscriberDBWrapper subscriberDBWrapper = new SubscriberDBWrapper(getApplicationContext());
        mSubscribersList = subscriberDBWrapper.getAllSubscribers();
        if (mSubscribersList.isEmpty()) {
            mTextViewEmptyListInfo.setVisibility(View.VISIBLE);
            mRecyclerViewBlackList.setVisibility(View.GONE);
        } else {
            mTextViewEmptyListInfo.setVisibility(View.GONE);
            mRecyclerViewBlackList.setVisibility(View.VISIBLE);
        }

        mAdapter = new SubscriberAdapter(mSubscribersList);
        mAdapter.setClickSubscriberItem(PhoneFilterActivity.this);
        mAdapter.notifyDataSetChanged();
        mRecyclerViewBlackList.setAdapter(mAdapter);
    }

    private void openDialogInputNumber() {

        LayoutInflater layoutInflater = LayoutInflater.from(PhoneFilterActivity.this);
        View subView = layoutInflater.inflate(R.layout.dialog_input_number, null);
        final EditText subEditText = (EditText) subView.findViewById(R.id.editTextInputNumberActivityPhoneFilter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.textResInputNumber)
                .setCancelable(false)
                .setView(subView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("vDev", "PhoneFilterActivity onCreate onClick PositiveButton -> ");
                        String inputNumber = subEditText.getText().toString();
                        SubscriberDBWrapper subscriberDBWrapper = new SubscriberDBWrapper(PhoneFilterActivity.this);
                        subscriberDBWrapper.addToBlackList(new Subscriber(inputNumber, 1));

                        updateList();
//                        Toast.makeText(PhoneFilterActivity.this, inputNumber, Toast.LENGTH_SHORT).show();
                        Log.d("vDev", "PhoneFilterActivity onCreate onClick PositiveButton text -> " + inputNumber);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d("vDev", "PhoneFilterActivity onCreate onClick NegativeButton -> ");
                        Toast.makeText(PhoneFilterActivity.this, "Cancel", Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    private void updateList() {
        mSubscribersList.clear();
        SubscriberDBWrapper dbWrapper = new SubscriberDBWrapper(PhoneFilterActivity.this);
        mSubscribersList.addAll(dbWrapper.getAllSubscribers());
        if (!mSubscribersList.isEmpty()) {
            mTextViewEmptyListInfo.setVisibility(View.GONE);
            mRecyclerViewBlackList.setVisibility(View.VISIBLE);
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClickSubscriberItem(Subscriber mSubscriber) {
        Log.d("vDev", "PhoneFilterActivity onClickSubscriberItem 3 -> ");
    }
}
