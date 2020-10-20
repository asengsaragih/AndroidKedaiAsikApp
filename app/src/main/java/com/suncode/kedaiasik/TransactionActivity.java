package com.suncode.kedaiasik;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.suncode.kedaiasik.adapter.OrderAdapter;
import com.suncode.kedaiasik.adapter.TransactionAdapter;
import com.suncode.kedaiasik.base.BaseActivity;
import com.suncode.kedaiasik.base.Constant;
import com.suncode.kedaiasik.model.Menu;
import com.suncode.kedaiasik.model.Store;
import com.suncode.kedaiasik.model.TransactionStore;
import com.suncode.kedaiasik.model.TransactionUser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionActivity extends BaseActivity {

    private static final String TAG = "TransactionActivityTAG";

    private EditText mAddressEdittext;
    private RecyclerView mTransactionRecycleview;
    private TextView mTotalTextview;
    private TextView mSubTotalTextview;
    private RadioGroup mTransactionRadioGroup;
    private RadioButton mCodRadioButton;
    private RadioButton mTransferRadioButton;
    private Button mOrderNowButton;

    private TransactionAdapter mAdapter;

    private List<String> mDataId = new ArrayList<>();
    private List<Menu> mData = new ArrayList<>();

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            for (String s : menuChooseList()) {
                if (s.equals(snapshot.getKey())) {
                    mData.add(snapshot.getValue(Menu.class));
                    mDataId.add(snapshot.getKey());
                    mAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        mAddressEdittext = findViewById(R.id.editText_transaction_address);
        mTransactionRecycleview = findViewById(R.id.recycle_transaction);
        mTransactionRecycleview.setLayoutManager(layoutManager);
        mTotalTextview = findViewById(R.id.textView_transaction_total);
        mSubTotalTextview = findViewById(R.id.textView_transaction_subtotal);
        mTransactionRadioGroup = findViewById(R.id.radioGroup_transaction_payment_methode);
        mCodRadioButton = findViewById(R.id.radioButton_transaction_cod);
        mTransferRadioButton = findViewById(R.id.radioButton_transaction_transfer);
        mOrderNowButton = findViewById(R.id.button_transaction_ordernow);
        mOrderNowButton.setOnClickListener(v -> orderNow());

        mTotalTextview.setText("Total : " + currencyFormat(getIntentData().getTotalOrder()));
        mSubTotalTextview.setText("Subtotal : " + currencyFormat(getIntentData().getTotalOrder() + 5000.0));

        setData();
    }

    private void orderNow() {
        if (TextUtils.isEmpty(mAddressEdittext.getText().toString())) {
            toast("Alamat nya kosong.");
            return;
        }

        TransactionUser user = new TransactionUser(getIntentData().getStoreId(), getIntentData().getDataOrder(), getIntentData().getTotalOrder());
        TransactionStore store = new TransactionStore(userID, mAddressEdittext.getText().toString(), getIntentData().getDataOrder(), getIntentData().getTotalOrder());

        DatabaseReference userTransactionPush = mDatabase.getReference().child(Constant.USER).child(userID).child(Constant.TRANSACTION);
        String keyPushUserTransaction = userTransactionPush.push().getKey();

        assert keyPushUserTransaction != null;
        userTransactionPush.child(keyPushUserTransaction).setValue(user);

        DatabaseReference storeTransactionPush = mDatabase.getReference().child(Constant.STORE).child(getIntentData().getStoreId()).child(Constant.TRANSACTION);
        String keyPushStoreTransaction = storeTransactionPush.push().getKey();

        assert keyPushStoreTransaction != null;
        storeTransactionPush.child(keyPushStoreTransaction).setValue(store);

        toast("Berhasil Mengorder Data");
        finish();
    }

    private List<String> menuChooseList() {
        HashMap<String, Integer> dataMap = getIntentData().getDataOrder();

        return new ArrayList<>(dataMap.keySet());
    }

    private void setData() {
        DatabaseReference reference = mDatabase.getReference().child(Constant.STORE).child(getIntentData().getStoreId()).child(Constant.MENU);
        reference.addChildEventListener(childEventListener);

        mAdapter = new TransactionAdapter(this, mDataId, mData, getIntentData().getDataOrder());
        mTransactionRecycleview.setAdapter(mAdapter);
    }

    private ItemOrder getIntentData() {
        String id = getIntent().getStringExtra(Constant.INTENT_TO_TRANSACTION_ID_STORE);
        HashMap<String, Integer> hashMap = (HashMap<String, Integer>) getIntent().getSerializableExtra(Constant.INTENT_TO_TRANSACTION_HASHMAP);
        Double total = getIntent().getDoubleExtra(Constant.INTENT_TO_TRANSACTION_TOTAL, 0.0);

        return new ItemOrder(id, hashMap, total);
    }

    private String currencyFormat(double total) {
        //convert double total to string currency with symbol
        //ex. $ 5,000.58
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalSeparatorAlwaysShown(false);

        return "Rp " + decimalFormat.format(total);
    }


    private class ItemOrder {
        private String storeId;
        private HashMap<String, Integer> dataOrder;
        private Double totalOrder;

        public ItemOrder(String storeId, HashMap<String, Integer> dataOrder, Double totalOrder) {
            this.storeId = storeId;
            this.dataOrder = dataOrder;
            this.totalOrder = totalOrder;
        }

        public String getStoreId() {
            return storeId;
        }

        public HashMap<String, Integer> getDataOrder() {
            return dataOrder;
        }

        public Double getTotalOrder() {
            return totalOrder;
        }
    }
}