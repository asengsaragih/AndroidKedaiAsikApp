package com.suncode.kedaiasik.adapter;

import android.annotation.SuppressLint;
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
import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    private Context mContext;
    private List<Menu> mData;
    private List<String> mDataId;
    private TextView mTotalPriceView;
    private List<String> mListMenuChoose;
    private ClickHandler mHandler;

    private Double mTotalOrder = 0.0;

    public OrderAdapter(Context mContext, List<Menu> mData, List<String> mDataId, TextView mTotalPriceView, List<String> mListMenuChoose, ClickHandler mHandler) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataId = mDataId;
        this.mTotalPriceView = mTotalPriceView;
        this.mListMenuChoose = mListMenuChoose;
        this.mHandler = mHandler;
    }

    /*
     * kelas ini dipanggil di store activity dikarenakan class ini memiliki
     * tombol pluss dan minus untuk order makanan
     */

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_order, parent, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        // set nama
        holder.name.setText(mData.get(position).getName());
        // set harga dengan format
        holder.price.setText(currencyFormat(mData.get(position).getPrice()));
        // set gambar yang udah di covert dari format string ke bitmap format
        Glide.with(mContext)
                .load(decodeBase64(mData.get(position).getImage()))
                .into(holder.image);

        // ini dijalan kan ketika tombol tambah ditekan
        holder.add.setOnClickListener(v -> {
            // buat variable temporary dikarenakan format dari textview itu string
            int qty = Integer.parseInt(holder.qty.getText().toString());

            // set kuantiti dengan kuantiti ditambah 1 dan dibalikin ke format string lagi
            holder.qty.setText(String.valueOf(qty + 1));

            // ini untuk perhitungan total otomatis
            mTotalOrder = mTotalOrder + mData.get(position).getPrice();

            //fungdi untuk mengupdate total textview yang berada di layout
            updateTotalView(mTotalOrder);

            // penambahan id menu ke dalam list ketika tombol add ditekan
            mListMenuChoose.add(mDataId.get(position));

            // pemanggilan clickhandler yang berfungsi ketika button ditambah
            // total keseluruhan dibisa dipanggil diactivity store secara realtime
            mHandler.buttonClicked(mTotalOrder);
        });

        // ini dijalan kan ketika tombol kurang ditekan
        holder.less.setOnClickListener(v -> {
            // buat variable temporary dikarenakan format dari textview itu string
            int qty = Integer.parseInt(holder.qty.getText().toString());


            if (qty == 0) {
                //cek jika kuantitas 0 maka qty list item ngga jadi minus
                holder.qty.setText("0");
            } else {
                holder.qty.setText(String.valueOf(qty - 1));

                // ini untuk perhitungan total otomatis
                mTotalOrder = mTotalOrder - mData.get(position).getPrice();

                //fungdi untuk mengupdate total textview yang berada di layout
                updateTotalView(mTotalOrder);
            }

            // penambahan id menu ke dalam list ketika tombol add ditekan
            mListMenuChoose.remove(mDataId.get(position));

            // pemanggilan clickhandler yang berfungsi ketika button ditambah
            // total keseluruhan dibisa dipanggil diactivity store secara realtime
            mHandler.buttonClicked(mTotalOrder);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @SuppressLint("SetTextI18n")
    private void updateTotalView(Double total) {
        mTotalPriceView.setText(mContext.getString(R.string.total_order) + currencyFormat(total));
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

    static class OrderHolder extends RecyclerView.ViewHolder {

        final TextView name;
        final TextView price;
        final TextView qty;
        final ImageView image;
        final Button add;
        final Button less;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView_list_item_order_title);
            price = itemView.findViewById(R.id.textView_list_item_order_price);
            qty = itemView.findViewById(R.id.textview_list_item_order_quantity);
            image = itemView.findViewById(R.id.imageView_list_item_order_image);
            add = itemView.findViewById(R.id.button_list_item_order_add);
            less = itemView.findViewById(R.id.button_list_item_order_less);
        }
    }

    public interface ClickHandler {
        void buttonClicked(Double total);
    }
}
