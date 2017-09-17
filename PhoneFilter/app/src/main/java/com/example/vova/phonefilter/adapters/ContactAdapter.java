package com.example.vova.phonefilter.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vova.phonefilter.R;
import com.example.vova.phonefilter.model.Subscriber;

import java.io.IOException;
import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Subscriber> mContactNames = new ArrayList<>();

    private Context mContext;

    public ContactAdapter(ArrayList<Subscriber> contactNames) {
        mContactNames = contactNames;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        mContext = parent.getContext();

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_contact_info, parent, false);
        viewHolder = new ContactHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ContactHolder contactHolder = (ContactHolder) holder;
        final Subscriber contact = mContactNames.get(position);
        if (contact.getStringPhotoUri() != null) {
            try {
                contactHolder.mImageViewContact.setVisibility(View.VISIBLE);
                contactHolder.mTextViewShortNumber.setVisibility(View.INVISIBLE);
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(mContext.getContentResolver(),
                                Uri.parse(contact.getStringPhotoUri()));
                contactHolder.mImageViewContact.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            contactHolder.mTextViewShortNumber.setVisibility(View.VISIBLE);
            contactHolder.mImageViewContact.setVisibility(View.INVISIBLE);
            contactHolder.mTextViewShortNumber.setText(contact.getSubscriberNumber().substring(0, 2));
        }
        contactHolder.mTextViewName.setText(contact.getSubscriberName());
        contactHolder.mTextViewNumber.setText(contact.getSubscriberNumber());
        contactHolder.mCheckBoxAddContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(mContext, "Check!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mContactNames.size();
    }

    private class ContactHolder extends RecyclerView.ViewHolder {

        private ImageView mImageViewContact;
        private TextView mTextViewShortNumber;
        private TextView mTextViewName;
        private TextView mTextViewNumber;
        private CheckBox mCheckBoxAddContact;

        ContactHolder(View itemView) {
            super(itemView);

            mImageViewContact = (ImageView) itemView.findViewById(R.id.imageViewContactImageContactItem);
            mTextViewShortNumber = (TextView) itemView.findViewById(R.id.textViewShortContactNumberContactItem);
            mTextViewName = (TextView) itemView.findViewById(R.id.textViewContactNameContactItem);
            mTextViewNumber = (TextView) itemView.findViewById(R.id.textViewContactNumberContactItem);
            mCheckBoxAddContact = (CheckBox) itemView.findViewById(R.id.checkboxContactAddContactItem);
        }
    }
}
