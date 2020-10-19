package com.suncode.kedaiasik;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.suncode.kedaiasik.adapter.OrderAdapter;
import com.suncode.kedaiasik.base.BaseActivity;
import com.suncode.kedaiasik.base.Constant;
import com.suncode.kedaiasik.model.Menu;
import com.suncode.kedaiasik.model.Store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class StoreActivity extends BaseActivity {
    private static final String TAG = "StoreActivityTAG";

    private RecyclerView mStoreRecycleview;
    private TextView mTotalTextview;
    private Button mOrderButton;

    private OrderAdapter mAdapter;

    private List<String> mDataId;
    private List<Menu> mData;

    //dan inti variable temp untuk pengiriman seluruh menu yang di pesan ke transaction activiry
    private List<String> mListMenuChoose;

    //ini variable temp untuk pengiriman total ke transaction activiry
    private Double mTotalOrder = 0.0;

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            mData.add(snapshot.getValue(Menu.class));
            mDataId.add(snapshot.getKey());
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            int pos = mDataId.indexOf(snapshot.getKey());
            mData.set(pos, snapshot.getValue(Menu.class));
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            int pos = mDataId.indexOf(snapshot.getKey());
            mDataId.remove(pos);
            mData.remove(pos);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        setTitleStore();

        mStoreRecycleview = findViewById(R.id.recycle_store);
        mStoreRecycleview.setLayoutManager(layoutManager);

        mTotalTextview = findViewById(R.id.textView_total);
        mOrderButton = findViewById(R.id.button_order);

        mDataId = new ArrayList<>();
        mData = new ArrayList<>();

        //penampung list makanan atau minuman yang dipilih
        mListMenuChoose = new ArrayList<>();

        setData();

        //ketika button pesan sekarang di klik
        mOrderButton.setOnClickListener(v -> orderMenu());
    }

    private void orderMenu() {
        //validasi dulu, jika yang dipesan kosong batalkan fungsi
        if (mListMenuChoose.size() == 0) {
            toast("Pilih salah satu makanan atau minuman");
            return;
        }

        // nah kan disini seluruh orderan yang diambil masuk kedalam list
        // dari list yang duplicate itu dihitung dulu yang kembar ada berapa
        // setelah dihitung langsung dimasukkan kedalam hashmap
        // karena firebase realtime support hashmap, maka dari itu pakai hashmap

        HashMap<String, Integer> hashMap = new HashMap<>();

        for (String idMenu : mListMenuChoose) {
            hashMap.put(idMenu, Collections.frequency(mListMenuChoose, idMenu));
        }

        Intent intent = new Intent(this, TransactionActivity.class);
        intent.putExtra(Constant.INTENT_TO_TRANSACTION_HASHMAP, hashMap);
        intent.putExtra(Constant.INTENT_TO_TRANSACTION_TOTAL, mTotalOrder);
        startActivity(intent);
    }

    private void setData() {
        DatabaseReference reference = mDatabase.getReference().child(Constant.STORE).child(getStoreId()).child(Constant.MENU);
        reference.addChildEventListener(childEventListener);

        mAdapter = new OrderAdapter(getApplicationContext(), mData, mDataId, mTotalTextview, mListMenuChoose, total -> {
            mTotalOrder = total;
        });

        mStoreRecycleview.setAdapter(mAdapter);
    }

    private String getStoreId() {
        return getIntent().getStringExtra(Constant.INTENT_TO_ORDER_ID);
    }

    private void setTitleStore() {
        DatabaseReference reference = mDatabase.getReference().child(Constant.STORE).child(getStoreId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //validate snapshoot
                if (!snapshot.exists())
                    return;

                //insert into object
                Store store = snapshot.getValue(Store.class);

                //check store
                if (store == null)
                    return;

                //set title
                setTitle(store.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });
    }
}