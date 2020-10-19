package com.suncode.kedaiasik.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.suncode.kedaiasik.R;
import com.suncode.kedaiasik.model.Menu;

import java.text.DecimalFormat;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {
    private Context mContext;
    private List<Menu> mData;
    private List<String> mDataId;
    private ClickHandler mHandler;

    public MenuAdapter(Context mContext, List<Menu> mData, List<String> mDataId, ClickHandler mHandler) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataId = mDataId;
        this.mHandler = mHandler;
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_menu, parent, false);
        return new MenuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
        holder.name.setText(mData.get(position).getName());
        holder.price.setText(currencyFormat(mData.get(position).getPrice()));

        Glide.with(mContext)
                .load(decodeBase64(mData.get(position).getImage()))
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> mHandler.onItemClicked(mDataId.get(position), mData.get(position)));
    }

    @Override
    public int getItemCount() {
        return mData.size();
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

    static class MenuHolder extends RecyclerView.ViewHolder {

        final ImageView image;
        final TextView name;
        final TextView price;

        public MenuHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageView_list_item_menu_image);
            name = itemView.findViewById(R.id.textView_list_item_menu_name);
            price = itemView.findViewById(R.id.textView_list_item_menu_price);
        }
    }

    public interface ClickHandler {
        void onItemClicked(String id, Menu menu);
    }
}
