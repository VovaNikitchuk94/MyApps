package com.example.vova.phonefilter.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vova.phonefilter.R;
import com.example.vova.phonefilter.model.Subscriber;

import java.util.ArrayList;

public class SubscriberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Subscriber> mSubscribers = new ArrayList<>();

    public SubscriberAdapter(ArrayList<Subscriber> list) {
        mSubscribers = list;
    }
    private OnClickSubscriberItem mClickSubscriberItem = null;

    public void setClickSubscriberItem(OnClickSubscriberItem clickItem){
        mClickSubscriberItem = clickItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_subscriber_info, parent, false);
        viewHolder = new SubscriberViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SubscriberViewHolder subscriberViewHolder = (SubscriberViewHolder) holder;
        final Subscriber subscriber = mSubscribers.get(position);
        //TODO change image in future
//        subscriberViewHolder.imageViewContact.setImageResource(R.drawable.test);
        if (subscriber.getSubscriberName() != null) {
            subscriberViewHolder.textViewName.setText(subscriber.getSubscriberName());
        }
        subscriberViewHolder.textViewNumber.setText(subscriber.getSubscriberNumber());
        subscriberViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("vDev", "onClick 1 ->");
                if (mClickSubscriberItem != null) {
                    Log.d("vDev", "onClick 2 ->");
                    mClickSubscriberItem.onClickSubscriberItem(subscriber);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSubscribers.size();
    }

    private class SubscriberViewHolder extends RecyclerView.ViewHolder {
//            implements View.OnClickListener, View.OnLongClickListener {

        ImageView imageViewContact;
        TextView textViewName;
        TextView textViewNumber;

        public SubscriberViewHolder(View itemView) {
            super(itemView);
            imageViewContact = (ImageView) itemView.findViewById(R.id.imageViewContactImageSubscriberItem);
            textViewName = (TextView) itemView.findViewById(R.id.textViewSubscriberNameSubscriberItem);
            textViewNumber = (TextView) itemView.findViewById(R.id.textViewSubscriberNumberSubscriberItem);
//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            Log.d("vDev", "onClick textViewNumber->" + textViewNumber.getText());
//        }
//
//        @Override
//        public boolean onLongClick(View v) {
//            Log.d("vDev", "onLongClick textViewNumber->" + textViewNumber.getText());
//            return false;
//        }
    }

    public interface OnClickSubscriberItem {
        void onClickSubscriberItem(Subscriber mSubscriber);
    }
}
