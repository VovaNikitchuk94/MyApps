package com.example.vova.phonefilter.activities;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class PhoneFilterActivity extends AppCompatActivity implements SubscriberAdapter.OnClickSubscriberItem,
        View.OnClickListener {

    private RecyclerView mRecyclerViewBlackList;
    private TextView mTextViewEmptyListInfo;
    private FloatingActionButton mFloatingActionButtonAdd;

    private ArrayList<Subscriber> mSubscribersList = new ArrayList<>();

    private SubscriberAdapter mAdapter;

//    private CountDownTimer mCountDownTimer;
//    private TextView mTextView1;
    private Button mButton1;
//    private Button mButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());
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

        mRecyclerViewBlackList = (RecyclerView) findViewById(R.id.simpleRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewBlackList.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerViewBlackList.getContext(),
                layoutManager.getOrientation());
        mRecyclerViewBlackList.addItemDecoration(dividerItemDecoration);

        getData();

        mButton1 = (Button) findViewById(R.id.button1);
        mButton1.setOnClickListener(this);
    }

    private void getData() {
        mSubscribersList.clear();
        SubscriberDBWrapper subscriberDBWrapper = new SubscriberDBWrapper(getApplicationContext());
        mSubscribersList = subscriberDBWrapper.getAllBlockedSubscribers(1);
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
        mSubscribersList.addAll(dbWrapper.getAllBlockedSubscribers(1));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                Intent intent = new Intent(this, ContactsActivity.class);
                startActivity(intent);
                break;
        }
    }


}
