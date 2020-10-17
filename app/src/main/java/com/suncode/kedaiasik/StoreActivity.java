package com.suncode.kedaiasik;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.suncode.kedaiasik.base.BaseActivity;
import com.suncode.kedaiasik.base.Constant;
import com.suncode.kedaiasik.model.Store;

public class StoreActivity extends BaseActivity {

    private static final String TAG = "StoreActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        //store name
        setTitleStore();
    }

    private void setTitleStore() {
        DatabaseReference reference = mDatabase.getReference().child(Constant.STORE).child(getStoreID());
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

    private String getStoreID() {
        return getIntent().getStringExtra(Constant.INTENT_TO_STORE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}