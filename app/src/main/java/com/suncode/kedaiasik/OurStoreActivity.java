package com.suncode.kedaiasik;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.suncode.kedaiasik.adapter.MenuAdapter;
import com.suncode.kedaiasik.base.BaseActivity;
import com.suncode.kedaiasik.base.Constant;
import com.suncode.kedaiasik.model.FormMenu;
import com.suncode.kedaiasik.model.Menu;
import com.suncode.kedaiasik.model.Store;

import java.util.ArrayList;
import java.util.List;

public class OurStoreActivity extends BaseActivity {

    private static final String TAG = "StoreActivity";
    private RecyclerView mMenuRecycleview;
    private FloatingActionButton mAddMenuFab;
    private List<Menu> mData;
    private List<String> mDataId;
    private MenuAdapter mAdapter;

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
        setContentView(R.layout.activity_our_store);
        //store name
        setTitleStore();

        //initialize variable
        mMenuRecycleview = findViewById(R.id.recycle_menu);
        mMenuRecycleview.setLayoutManager(layoutManager);

        mAddMenuFab = findViewById(R.id.fab_menu);
        mAddMenuFab.setOnClickListener(v -> openForm());

        mData = new ArrayList<>();
        mDataId = new ArrayList<>();

        setData();
    }

    private void setData() {
        DatabaseReference reference = mDatabase.getReference().child(Constant.STORE).child(getStoreID()).child(Constant.MENU);
        reference.addChildEventListener(childEventListener);

        mAdapter = new MenuAdapter(this, mData, mDataId, (id, menu) -> {
            Intent intent = new Intent(this, FormMenuActivity.class);
            //ngirim object form menu ke frommenuactivity
            FormMenu formMenu = new FormMenu(getStoreID(), id);

            intent.putExtra(Constant.INTENT_TO_FORM_MENU, formMenu);
            startActivity(intent);

        });

        mMenuRecycleview.setAdapter(mAdapter);
    }

    private void openForm() {
        if (isReadStroragePermissionGranted()) {
            Intent intent = new Intent(this, FormMenuActivity.class);
            //ngirim object form menu ke frommenuactivity
            FormMenu formMenu = new FormMenu(getStoreID(), null);

            intent.putExtra(Constant.INTENT_TO_FORM_MENU, formMenu);
            startActivity(intent);
        } else {
            getPermissionReadStorage();
        }
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


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.store_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_history) {
            startActivity(new Intent(getApplicationContext(), HistoryActivity.class).putExtra(Constant.INTENT_TO_HISTORY, getStoreID()));
        }
        return super.onOptionsItemSelected(item);
    }
}