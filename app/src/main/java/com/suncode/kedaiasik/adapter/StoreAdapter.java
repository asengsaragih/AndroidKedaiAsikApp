package com.suncode.kedaiasik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suncode.kedaiasik.R;
import com.suncode.kedaiasik.model.Store;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreHolder>{
    private Context mContext;
    private List<Store> mData;
    private List<String> mDataId;
    private ClickHandler mHandler;

    public StoreAdapter(Context mContext, List<Store> mData, List<String> mDataId, ClickHandler mHandler) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataId = mDataId;
        this.mHandler = mHandler;
    }

    @NonNull
    @Override
    public StoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_store, parent, false);
        return new StoreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreHolder holder, int position) {
        holder.icon.setText(iconText(mData.get(position).getName()));
        holder.name.setText(mData.get(position).getName());
        holder.time.setText(openCloseTime(mData.get(position).getOpen(), mData.get(position).getClose()));

        holder.itemView.setOnClickListener(v -> mHandler.onItemClicked(mDataId.get(position), mData.get(position)));
    }

    private String openCloseTime(String open, String close) {
        return open + " - " + close;
    }

    private String iconText(String name) {
        //geting first and last name char 2 digit
        return name.replaceAll("^\\s*([a-zA-Z]).*\\s+([a-zA-Z])\\S+$", "$1$2");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class StoreHolder extends RecyclerView.ViewHolder {

        final TextView icon;
        final TextView name;
        final TextView time;

        public StoreHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.textView_list_item_store_icon);
            name = itemView.findViewById(R.id.textView_list_item_store_name);
            time = itemView.findViewById(R.id.textView_list_item_store_open_close);
        }
    }

    public interface ClickHandler {
        void onItemClicked(String id, Store store);
    }
}
