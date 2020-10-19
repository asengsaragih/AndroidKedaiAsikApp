package com.suncode.kedaiasik;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.suncode.kedaiasik.base.BaseActivity;
import com.suncode.kedaiasik.base.Constant;

import java.util.Objects;

public class SigninActivity extends BaseActivity {

    private static final String TAG = "SigninActivity";
    private EditText mEmailEdittext;
    private EditText mPasswordEdittext;
    private Button mSigninButton;
    private TextView mSignupTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        //hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        mEmailEdittext = findViewById(R.id.editText_signin_email);
        mPasswordEdittext = findViewById(R.id.editText_signin_password);
        mSigninButton = findViewById(R.id.button_signin);
        mSignupTextview = findViewById(R.id.textView_signin);

        singUp();

        //signin button clicked
        mSigninButton.setOnClickListener(v -> signIn());
    }

    private void signIn() {
        if (TextUtils.isEmpty(mEmailEdittext.getText().toString()) || TextUtils.isEmpty(mPasswordEdittext.getText().toString())) {
            toast(getString(R.string.err_empty_form));
            return;
        }

        String email = mEmailEdittext.getText().toString();
        String password = mPasswordEdittext.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful())
                        return;

                    //intent to main activity
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    if (e.getMessage() == null)
                        return;

                    if (e.getMessage().equals(Constant.ERR_AUTH_WRONG_EMAIL)) //email tidak terdaftar
                        toast(getString(R.string.err_wrong_email));
                    else if (e.getMessage().equals(Constant.ERR_AUTH_WRONG_PASS)) //password salah
                        toast(getString(R.string.err_wrong_pass));
                });
    }

    private void singUp() {
        //function for signup textview in signin clicked
        mSignupTextview.setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}