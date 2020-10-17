package com.suncode.kedaiasik.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {

    protected AppCompatActivity mActivity;
    protected FirebaseDatabase mDatabase;
    protected FirebaseAuth mAuth;
    protected String userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        //firebase initalize
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null)
            userID = mAuth.getCurrentUser().getUid();
    }

    protected void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected AlertDialog.Builder dialogBuilder(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        return builder;
    }

    protected boolean isEditTextEmpty(EditText editText) {
        //function for check edittext value
        return TextUtils.isEmpty(editText.getText().toString());
    }
}
