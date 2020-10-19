package com.suncode.kedaiasik;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.suncode.kedaiasik.base.BaseActivity;
import com.suncode.kedaiasik.base.Constant;
import com.suncode.kedaiasik.model.User;

import java.util.Objects;

public class SignupActivity extends BaseActivity {

    private static final String TAG = "SignupActivity";
    private EditText mNameEdittext;
    private EditText mPhoneEdittext;
    private EditText mEmailEdittext;
    private EditText mPasswordEdittext;
    private EditText mRePasswordEdittext;

    private Button mSignupButton;
    private TextView mSigninTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        mNameEdittext = findViewById(R.id.editText_signup_name);
        mPhoneEdittext = findViewById(R.id.editText_signup_phone);
        mEmailEdittext = findViewById(R.id.editText_signup_email);
        mPasswordEdittext = findViewById(R.id.editText_signup_password);
        mRePasswordEdittext = findViewById(R.id.editText_signup_repassword);

        mSignupButton = findViewById(R.id.button_signup);
        mSigninTextview = findViewById(R.id.textView_signup);

        //signup textview in signin clicked
        signIn();

        //button signup clicked
        mSignupButton.setOnClickListener(v -> signUp());

    }

    private void signIn() {
        //function for signup textview in signin clicked
        mSigninTextview.setOnClickListener(v -> {
            startActivity(new Intent(this, SigninActivity.class));
        });
    }

    private void signUp() {
        //validate if edittext empty
        if (isEditTextEmpty(mNameEdittext) || isEditTextEmpty(mPhoneEdittext) || isEditTextEmpty(mEmailEdittext) || isEditTextEmpty(mPasswordEdittext) || isEditTextEmpty(mRePasswordEdittext)) {
            toast(getString(R.string.err_empty_form));
            return;
        }

        //initliaze data
        String name = mNameEdittext.getText().toString();
        String phone = mPhoneEdittext.getText().toString();
        String email = mEmailEdittext.getText().toString();
        String password = mPasswordEdittext.getText().toString();
        String rePassword = mRePasswordEdittext.getText().toString();

        //check password
        //if password not equals repassword, RETURN
        if (!password.equals(rePassword)) {
            toast(getString(R.string.err_pass_not_equals));
            return;
        }

        //check password again
        //if password length less then 5 char return
        if (password.length() <= 5) {
            toast(getString(R.string.err_pass_length));
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    //check task if task error show toast
                    if (!task.isSuccessful()) {
                        return;
                    }

                    FirebaseUser user = mAuth.getCurrentUser();
                    String uId = Objects.requireNonNull(user).getUid();

                    //create user in firebase
                    createUser(uId, name, phone, email);
                })
                .addOnFailureListener(e -> {
                    if (e.getMessage() == null)
                        return;

                    //validate error when creating user
                    if (e.getMessage().equals(Constant.ERR_AUTH_BAD_EMAIL))
                        toast(getString(R.string.err_bad_email));
                    else if (e.getMessage().equals(Constant.ERR_AUTH_EMAIL_USED))
                        toast(getString(R.string.err_email_used));
                    else
                        toast(e.getMessage());
                });
    }

    private void createUser(String uid, String name, String phone, String email) {
        //tentukan data ingin dimasukkan dimana. sejenis initialize awal
        DatabaseReference reference = mDatabase.getReference().child(Constant.USER).child(uid);

        //insert parameter to object
        User user = new User(name, phone, email);

        //insert data
        reference.setValue(user)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful())
                        return;

                    toast(getString(R.string.user_created));

                    //intent to main activity
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    toast(e.getMessage());
                });
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