package ru.yandex.shtukarr.bigdigitalstesta.view;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ru.yandex.shtukarr.bigdigitalstesta.LinkModel;
import ru.yandex.shtukarr.bigdigitalstesta.R;

public class LinkListAdapter extends RecyclerView.Adapter<LinkListAdapter.ViewHolder> {
    private List<LinkModel> profileModelList;
    private HistoryScreen historyScreen;

    public LinkListAdapter(final HistoryScreen historyScreen) {
        profileModelList = new ArrayList<>();
        this.historyScreen = historyScreen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinkListAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d("onBindViewHolder", profileModelList.get(position).getUrl());
        switch (profileModelList.get(position).getStatus()) {
            case 1:
                holder.itemView.setBackgroundColor(Color.GREEN);
                break;
            case 2:
                holder.itemView.setBackgroundColor(Color.RED);
                break;
            case 3:
                holder.itemView.setBackgroundColor(Color.GRAY);
                break;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM HH:mm:ss");
        String time = simpleDateFormat.format(profileModelList.get(position).getDate());
        holder.textViewDate.setText(String.valueOf(time));
        holder.textViewLink.setText(profileModelList.get(position).getUrl());
        holder.itemView.setOnClickListener(view -> historyScreen.onItemSelected(profileModelList.get(position)));
    }

    @Override
    public int getItemCount() {
        return profileModelList.size();
    }

    public void setList(List<LinkModel> list) {
        this.profileModelList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate;
        TextView textViewLink;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.date);
            textViewLink = itemView.findViewById(R.id.link);
        }
    }


}
