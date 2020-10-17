package com.suncode.kedaiasik.base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class BaseActivity extends AppCompatActivity {

    protected AppCompatActivity mActivity;
    protected FirebaseDatabase mDatabase;
    protected FirebaseAuth mAuth;
    protected String userID;

    //for recycle initialize
    protected LinearLayoutManager layoutManager;
    protected DividerItemDecoration itemDecoration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        //firebase initalize
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null)
            userID = mAuth.getCurrentUser().getUid();

        //for recycle initialize
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
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

    protected boolean isWriteStroragePermissionGranted() {
        //check permission access read storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        else
            return true; // perimisi auto ada apabila api dibawah marsmello
    }

    protected boolean isReadStroragePermissionGranted() {
        //check permission access read storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        else
            return true; // perimisi auto ada apabila api dibawah marsmello
    }

    protected void getPermissionWriteStorage() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.REQUEST_PERMISSION_WRITE_STORAGE);
    }

    protected void getPermissionReadStorage() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQUEST_PERMISSION_READ_STORAGE);
    }

    public static String encodeToBased64(Bitmap imageBitmap) {
        //function convert image bitmap into based64
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();

        return Base64.encodeToString(bytes ,Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String imageBased64) {
        //function convert image based 64 to bitmap
        byte[] decodedByte = Base64.decode(imageBased64, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
