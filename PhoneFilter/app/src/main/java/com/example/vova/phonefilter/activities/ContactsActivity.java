package com.example.vova.phonefilter.activities;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.example.vova.phonefilter.R;
import com.example.vova.phonefilter.adapters.ContactAdapter;
import com.example.vova.phonefilter.model.Subscriber;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private FloatingActionButton mFloatingActionButton;
    private RecyclerView mRecyclerViewContacts;

    private ContactAdapter mContactAdapter;
    private ArrayList<Subscriber> mListContacts = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fabAddContactContactsActivity);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mRecyclerViewContacts = (RecyclerView) findViewById(R.id.simpleRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewContacts.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerViewContacts.getContext(),
                layoutManager.getOrientation());
        mRecyclerViewContacts.addItemDecoration(dividerItemDecoration);

        setData();

    }

    private void setData () {
        mListContacts.clear();
        getContacts();
        mContactAdapter = new ContactAdapter(mListContacts);
        mContactAdapter.notifyDataSetChanged();
        mRecyclerViewContacts.setAdapter(mContactAdapter);

    }

    private void getContacts() {

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        String[] projection = new String[] { ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER};

        String selection = ContactsContract.Contacts.DISPLAY_NAME
                + " IS NOT NULL";

        Uri phoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String phoneCONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String PHOTO_URI = ContactsContract.CommonDataKinds.Phone.PHOTO_URI;

        String contactName = null;
        String contactNumber = null;
        String contactPhotoUri = null;

        ContentResolver contentResolver = getContentResolver();
        CursorLoader cursorLoader = new CursorLoader(this, CONTENT_URI, projection, selection, null, null);
//        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        //Запускаем цикл обработчик для каждого контакта:
        if (cursor.getCount() > 0) {

            //Если значение имени и номера контакта больше 0 (то есть они существуют) выбираем
            //их значения в приложение привязываем с соответствующие поля "Имя" и "Номер":
            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex(ID));
                contactName = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                //Получаем имя:
                if (hasPhoneNumber > 0) {
//                    contactName = "Name -> " + name;

//                    output.append("\n Имя: " + name);
                    Cursor phoneCursor = contentResolver.query(phoneCONTENT_URI, null,
                            phoneCONTACT_ID + " = ?", new String[] { contact_id }, null);

                    //get соответствующий ему номер:
                    if (phoneCursor != null) {
                        while (phoneCursor.moveToNext()) {
                            contactNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                            contactPhotoUri = phoneCursor.getString(phoneCursor.getColumnIndex(PHOTO_URI));

    //                        contactNumber = "\n Phone number -> " + contactNumber;
    //                        contactPhotoUri = "\n Contact Uri -> " + contactPhotoUri;
                        }
                    }
                    if (phoneCursor != null) {
                        phoneCursor.close();
                    }
                    mListContacts.add(new Subscriber(contactPhotoUri, contactName, contactNumber, 0));
                }

//                output.append("\n");
//                Log.d("vDev", "output -> \n" + contactName + contactNumber + contactPhotoUri);

            }
            cursor.close();
        }
    }
}
