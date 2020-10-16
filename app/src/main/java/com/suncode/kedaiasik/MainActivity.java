package com.suncode.kedaiasik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseUser;
import com.suncode.kedaiasik.base.BaseActivity;

public class MainActivity extends BaseActivity {

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
        }
        return super.onOptionsItemSelected(item);
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