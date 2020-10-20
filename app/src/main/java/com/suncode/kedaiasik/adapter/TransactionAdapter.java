package com.suncode.kedaiasik.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.suncode.kedaiasik.R;
import com.suncode.kedaiasik.model.Menu;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {

    private Context mContext;
    private List<String> mDataId;
    private List<Menu> mData;
    private HashMap<String, Integer> mMenuChoose;

    public TransactionAdapter(Context mContext, List<String> mDataId, List<Menu> mData, HashMap<String, Integer> mMenuChoose) {
        this.mContext = mContext;
        this.mDataId = mDataId;
        this.mData = mData;
        this.mMenuChoose = mMenuChoose;
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_order, parent, false);
        return new TransactionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        holder.title.setText(mData.get(position).getName());
        holder.price.setText(currencyFormat(mData.get(position).getPrice()));

        for (String key : mMenuChoose.keySet()) {
            if (mDataId.get(position).equals(key)) {
                holder.qty.setText(String.valueOf(mMenuChoose.get(key)));
            }
        }

        Glide.with(mContext)
                .load(decodeBase64(mData.get(position).getImage()))
                .into(holder.image);
    }

    private static Bitmap decodeBase64(String imageBased64) {
        //function convert image based 64 to bitmap
        byte[] decodedByte = Base64.decode(imageBased64, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private String currencyFormat(double total) {
        //convert double total to string currency with symbol
        //ex. $ 5,000.58
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalSeparatorAlwaysShown(false);

        return "Rp " + decimalFormat.format(total);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class TransactionHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView price;
        final ImageView image;
        final TextView qty;
        Button add;
        Button less;

        public TransactionHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textView_list_item_order_title);
            price = itemView.findViewById(R.id.textView_list_item_order_price);
            qty = itemView.findViewById(R.id.textview_list_item_order_quantity);
            image = itemView.findViewById(R.id.imageView_list_item_order_image);

            add = itemView.findViewById(R.id.button_list_item_order_add);
            add.setVisibility(View.GONE);

            less = itemView.findViewById(R.id.button_list_item_order_less);
            less.setVisibility(View.GONE);
        }
    }
}
