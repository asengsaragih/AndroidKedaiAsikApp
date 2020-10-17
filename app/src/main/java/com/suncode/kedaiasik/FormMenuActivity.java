package com.suncode.kedaiasik;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.cottacush.android.currencyedittext.CurrencyEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.suncode.kedaiasik.base.BaseActivity;
import com.suncode.kedaiasik.base.Constant;
import com.suncode.kedaiasik.model.FormMenu;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class FormMenuActivity extends BaseActivity {

    private static final String TAG = "FormMenuActivityTAG";

    private String mBase64ImageString = null;
    private EditText mNameEdittext;
    private CurrencyEditText mPriceEdittext;
    private RadioGroup mCategoryRadioGroup;
    private RadioButton mFoodRadioButton;
    private RadioButton mDrinkRadioButton;
    private ImageView mPictureImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_menu);

        mNameEdittext = findViewById(R.id.editText_form_menu_name);
        mPriceEdittext = findViewById(R.id.editText_form_menu_price);
        mCategoryRadioGroup = findViewById(R.id.radioGroup_form_menu_category);
        mFoodRadioButton = findViewById(R.id.radioButton_form_menu_food);
        mDrinkRadioButton = findViewById(R.id.radioButton_form_menu_drink);

        //function pick image from external storage
        mPictureImageview = findViewById(R.id.imageView_form_menu_image);
        mPictureImageview.setOnClickListener(v -> pickImageFromStorage());

        //check old data
        oldData();
    }

    private void oldData() {
        //function for editing
        if (getFormMenuData().getMenuId() == null)
            return;

        DatabaseReference reference = mDatabase.getReference().child(Constant.STORE).child(getFormMenuData().getStoreId()).child(Constant.MENU).child(getFormMenuData().getMenuId());
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                com.suncode.kedaiasik.model.Menu menu = snapshot.getValue(com.suncode.kedaiasik.model.Menu.class);

                if (menu == null)
                    return;

                mNameEdittext.setText(menu.getName());
                mPriceEdittext.setText(currencyFormat(menu.getPrice()), TextView.BufferType.EDITABLE);

                Glide.with(FormMenuActivity.this)
                        .load(decodeBase64(menu.getImage()))
                        .into(mPictureImageview);

                if(menu.getType() == Constant.MENU_CATEGORY_FOOD)
                    mCategoryRadioGroup.check(mFoodRadioButton.getId());
                else if (menu.getType() == Constant.MENU_CATEGORY_DRINK)
                    mCategoryRadioGroup.check(mDrinkRadioButton.getId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void pickImageFromStorage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        Intent samsungIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        samsungIntent.setType("image/jpg");
        samsungIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooserIntent;

        if (getPackageManager().resolveActivity(samsungIntent, 0) != null) {
            chooserIntent = Intent.createChooser(samsungIntent, "Pilih Gambar");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
        } else {
            chooserIntent = Intent.createChooser(intent, "Pilih Gambar");
        }
        try {
            startActivityForResult(chooserIntent, Constant.REQUEST_PICK_IMAGE);
        } catch (android.content.ActivityNotFoundException e) {
            toast("Install file manager");
        }
    }

    private FormMenu getFormMenuData() {
        return (FormMenu) getIntent().getSerializableExtra(Constant.INTENT_TO_FORM_MENU);
    }

    private void saveMenu() {
        if (isEditTextEmpty(mNameEdittext) || isEditTextEmpty(mPriceEdittext) || mBase64ImageString == null) {
            toast(getString(R.string.err_empty_form));
            return;
        }

        String storeId = getFormMenuData().getStoreId();
        String menuId = getFormMenuData().getMenuId();

        DatabaseReference reference;

        String name = mNameEdittext.getText().toString();
        double price = mPriceEdittext.getNumericValue();
        String image = mBase64ImageString;
        int category = getCategoryMenu();

        com.suncode.kedaiasik.model.Menu menu = new com.suncode.kedaiasik.model.Menu(name, price, image, category);

        //data menuid kemungkinan null di validasi disininya
        //validasi menu idnya
        if (menuId != null)
            reference = mDatabase.getReference().child(Constant.STORE).child(storeId).child(Constant.MENU).child(menuId);
        else
            reference = mDatabase.getReference().child(Constant.STORE).child(storeId).child(Constant.MENU).push();

        reference.setValue(menu)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful())
                        return;

                    toast("Berhasil");
                    finish();
                })
                .addOnFailureListener(e -> toast(e.getMessage()));
    }

    private String currencyFormat(double total) {
        //convert double total to string currency with symbol
        //ex. $ 5,000.58
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalSeparatorAlwaysShown(false);

        return "Rp " + decimalFormat.format(total);
    }

    private int getCategoryMenu() {
        // methode for getting category in radio group
        int id = mCategoryRadioGroup.getCheckedRadioButtonId();
        RadioButton button = findViewById(id);

        if (button.getText().toString().equals(getString(R.string.drink)))
            return Constant.MENU_CATEGORY_DRINK;
        else if (button.getText().toString().equals(getString(R.string.food)))
            return Constant.MENU_CATEGORY_FOOD;
        else
            return 0;
    }

    private void deleteMenu() {
        AlertDialog.Builder builder = dialogBuilder("Hapus Menu", "Yakin ingin menghapus menu ini?");
        builder.setPositiveButton("Hapus", (dialog, which) -> {
            DatabaseReference reference = mDatabase.getReference().child(Constant.STORE).child(getFormMenuData().getStoreId()).child(Constant.MENU).child(getFormMenuData().getMenuId());
            reference.removeValue()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful())
                            return;

                        finish();
                    })
                    .addOnFailureListener(e -> toast(e.getMessage()));
        });
        builder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //validate result code
        if (resultCode != RESULT_OK) return;
        //validate data
        if (data == null) return;
        //validate image data
        if (data.getData() == null) return;

        if (requestCode == Constant.REQUEST_PICK_IMAGE) {
            //get uri from intent
            Uri imageUri = data.getData();

            //insert into try catch for validate corrupt file
            try {
                //insert uri into input stream
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                //insert inputstream into bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                //convert bitmap into based64
                mBase64ImageString = encodeToBased64(bitmap);

                Glide.with(getApplicationContext())
                        .load(bitmap)
                        .into(mPictureImageview);

            } catch (FileNotFoundException e) {
                toast(e.getMessage());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (getFormMenuData().getMenuId() == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                saveMenu();
                break;
            case R.id.action_delete:
                deleteMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}