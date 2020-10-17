package com.suncode.kedaiasik;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.suncode.kedaiasik.base.BaseActivity;
import com.suncode.kedaiasik.base.Constant;
import com.suncode.kedaiasik.model.Store;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class RegisterStoreActivity extends BaseActivity {

    private EditText mNameEdittext;
    private EditText mOpenTimeEdittext;
    private EditText mCloseTimeEdittext;
    private Button mRegisterStoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_store);

        mNameEdittext = findViewById(R.id.editText_register_store_name);
        mOpenTimeEdittext = findViewById(R.id.editText_register_store_open);
        mCloseTimeEdittext = findViewById(R.id.editText_register_store_close);
        mRegisterStoreButton = findViewById(R.id.button_register_store);

        mRegisterStoreButton.setOnClickListener(v -> createStore());

        //pick open and close time
        timePicker(mOpenTimeEdittext);
        timePicker(mCloseTimeEdittext);
    }

    private void createStore() {
        if (isEditTextEmpty(mNameEdittext) || isEditTextEmpty(mOpenTimeEdittext) || isEditTextEmpty(mCloseTimeEdittext)) {
            toast(getString(R.string.err_empty_form));
            return;
        }

        String name = mNameEdittext.getText().toString();
        String open = mOpenTimeEdittext.getText().toString();
        String close = mCloseTimeEdittext.getText().toString();

        DatabaseReference reference = mDatabase.getReference().child(Constant.STORE);
        String storeID = reference.push().getKey();

        Store store = new Store(name, open, close);

        reference.child(storeID).setValue(store)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful())
                        return;

                    updateStoreIdInUserRoot(storeID);
                })
                .addOnFailureListener(e -> {
                    toast(e.getMessage());
                });
    }

    private void updateStoreIdInUserRoot(String storeId) {
        DatabaseReference reference = mDatabase.getReference().child(Constant.USER).child(userID).child(Constant.STORE_ID);
        reference.setValue(storeId)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful())
                        return;

                    toast("Berhasil membuat Toko");
                    finish();
                })
                .addOnFailureListener(e -> {
                    toast(e.getMessage());
                });
    }

    private void timePicker(EditText editText) {
        //get calendar
        Calendar calendar = Calendar.getInstance();

        //time pattren
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        //listener
        TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            //set new date on edittext
            editText.setText(simpleDateFormat.format(calendar.getTime()));
        };

        //execute / open time picker dialog
        editText.setOnClickListener(v -> {
            new TimePickerDialog(RegisterStoreActivity.this, listener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        });
    }
}