package com.suncode.kedaiasik;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suncode.kedaiasik.base.BaseActivity;
import com.suncode.kedaiasik.base.Constant;
import com.suncode.kedaiasik.model.User;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivityTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //session user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, SigninActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signout:
                //open dialog signout
                dialogSignOut();
                break;
            case R.id.action_store:
                //check store
                openStore();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openStore() {
        DatabaseReference reference = mDatabase.getReference().child(Constant.USER).child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //validate snapshot
                if (!snapshot.exists())
                    return;

                //read snapshot value and retrieve into object
                User user = snapshot.getValue(User.class);

                //check object
                if (user == null)
                    return;

                if (user.getStoreID() == null)
                    startActivity(new Intent(getApplicationContext(), RegisterStoreActivity.class)); //intent to register store
                else
                    startActivity(new Intent(getApplicationContext(), StoreActivity.class).putExtra(Constant.INTENT_TO_STORE, user.getStoreID())); //intent to store

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error: " + error.getCode());
                Log.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "Error: " + error.getDetails());
            }
        });
    }

    private void dialogSignOut() {
        AlertDialog.Builder builder = dialogBuilder(getString(R.string.signout_dialog_title), getString(R.string.signout_dialog_message));
        builder.setPositiveButton(getString(R.string.signout), (dialog, which) -> {
            //signout firebase
            mAuth.signOut();

            //intent to new
            startActivity(new Intent(this, SigninActivity.class));
            finish();
        });
        builder.setNegativeButton(getString(R.string.cancle), (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}