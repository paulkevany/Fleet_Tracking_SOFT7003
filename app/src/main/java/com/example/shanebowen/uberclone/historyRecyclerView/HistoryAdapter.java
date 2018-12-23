package com.example.shanebowen.uberclone.historyRecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shanebowen.uberclone.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolders> {
    private List<HistoryObject> itemList;
    private Context context;

    public HistoryAdapter(List<HistoryObject> itemList, Context context){
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutView.setLayoutParams(layoutParams);
        HistoryViewHolders historyViewHolders = new HistoryViewHolders(layoutView);
        return historyViewHolders;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolders historyViewHolders, int i) {

        historyViewHolders.rideId.setText(itemList.get(i).getRideId());

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
