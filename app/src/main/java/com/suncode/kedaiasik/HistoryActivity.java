package com.suncode.kedaiasik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.suncode.kedaiasik.base.BaseActivity;
import com.suncode.kedaiasik.base.Constant;

public class HistoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    private String getStoreId() {
        return getIntent().getStringExtra(Constant.INTENT_TO_HISTORY);
    }
}