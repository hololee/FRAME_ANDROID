package com.m.m.moment.memorize.frame.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.database.DataBaseManager;
import com.m.m.moment.memorize.frame.item.cardListItem;
import com.m.m.moment.memorize.frame.manager.DateManager;
import com.m.m.moment.memorize.frame.manager.FileManager;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Calendar;

public class CardEditActivity extends BaseActivity {


    ImageView img;
    EditText title, content;
    TextView date;

    ImageButton location;
    TextView pageEdit, pageDelete;


    String selectedLocation = "null";
    long selectedDate = 0;
    boolean imgSelected = false;
    Uri originalUri;
    Uri resultUri;

    DataBaseManager dbManager;

    long selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);

        img = findViewById(R.id.img);
        location = findViewById(R.id.location);

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        date = findViewById(R.id.date);

        pageEdit = findViewById(R.id.page_edit);
        pageDelete = findViewById(R.id.page_delete);


        //setTypeface
        TypefaceManager typefaceManager = new TypefaceManager(getApplicationContext());
        title.setTypeface(typefaceManager.getTypeNormal());
        content.setTypeface(typefaceManager.getTypeNormal());
        date.setTypeface(typefaceManager.getTypeNormal());
        pageEdit.setTypeface(typefaceManager.getTypeNormal());
        pageDelete.setTypeface(typefaceManager.getTypeNormal());


        //confirm input edittext.
        title.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(title.getWindowToken(), 0);    //hide keyboard
                    return true;
                }
                return false;
            }
        });
        content.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //4번째 줄인경우만.
                if (content.getLayout().getLineForOffset(content.getSelectionStart()) == 3) {
                    //Enter key Action
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(content.getWindowToken(), 0);    //hide keyboard
                        return true;
                    }
                    return false;
                }

                return false;
            }
        });


        dbManager = new DataBaseManager(getApplicationContext());


        //loading from saved data.
        selectedId = getIntent().getLongExtra("selectedId", 0);

        //prevent invalid approach.
        if (selectedId == 0) {
            Toast.makeText(this, getString(R.string.alert_approach_edit), Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);
        }

        cardListItem selectedItem = dbManager.getCardItem(selectedId);

        //set item data.
        imgSelected = true;

        // when save the card, if original uri and resultUri have a same data, img data won't be replaced.
        originalUri = selectedItem.getImg();
        resultUri = selectedItem.getImg();
        img.setImageBitmap(new FileManager(getApplicationContext()).getBitmapFromUri(selectedItem.getImg()));
        title.setText(selectedItem.getTitle());
        selectedDate = selectedItem.getDate();
        date.setText(DateManager.getDateText(selectedItem.getDate()));
        content.setText(selectedItem.getContent());

        //as location is not null.
        if (!selectedItem.getLocation().equals("null")) {

            //set selected icon img.
            location.setImageResource(R.drawable.location_pressed);
            selectedLocation = selectedItem.getLocation();

        }


        //이미지 추가.
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setInitialCropWindowPaddingRatio(0)
                        .setAspectRatio(1000, 1398)
                        .start(CardEditActivity.this);

            }
        });


        //날짜 선택.
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(CardEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month, dayOfMonth);

                        //선택한 시간 설정.
                        selectedDate = cal.getTimeInMillis();
                        date.setText(DateManager.getDateText(year, month, dayOfMonth));

                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE));
                dialog.show();

            }
        });


        //위치 설정.
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //카드 edit
        pageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!imgSelected || title.length() == 0 || selectedDate == 0) {
                    //날짜선택x, 제목입력x, 이미지x 인 경우.
                    Toast.makeText(CardEditActivity.this, getString(R.string.alert_add), Toast.LENGTH_SHORT).show();
                } else {
                    //모든 정보가 잘 들아올경우.

                    //이미지 이동 성공시 save data.
                    FileManager fileManager = new FileManager(CardEditActivity.this);
                    try {

                        //check original img data.
                        if (resultUri.equals(originalUri)) {
                            //if img is not changed.
                            dbManager.updateCardItem(selectedId, originalUri, title.getText().toString(), selectedDate, content.getText().toString(), selectedLocation);

                            //return to ListFragment.
                            finish();
                            overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);
                        } else {
                            //copy image.
                            Uri copyImage = fileManager.copyImageToMM(resultUri, String.valueOf(selectedId) + ".jpg");

                            //save data.
                            dbManager.updateCardItem(selectedId, copyImage, title.getText().toString(), selectedDate, content.getText().toString(), selectedLocation);

                            //return to ListFragment.
                            finish();
                            overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(CardEditActivity.this, getString(R.string.alert_add_error), Toast.LENGTH_SHORT).show();
                    }

                    //데이터 저장.

                }


            }
        });


        //delete card.
        pageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CardEditActivity.this);
                dialogBuilder.setTitle(R.string.dialog_title_card_delete)
                        .setMessage(R.string.dialog_message_card_delete)
                        .setNegativeButton(R.string.dialog_negative_card_delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //cancel.

                            }
                        })
                        .setPositiveButton(R.string.dialog_positive_card_delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //delete card data and image file.
                                dbManager.deleteCardItem(selectedId);
                                Toast.makeText(CardEditActivity.this, getString(R.string.alert_delete_card), Toast.LENGTH_SHORT).show();

                                //return to ListFragment.
                                finish();
                                overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);

                            }
                        });

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();


            }
        });


    }


    //이미지 선택 후 돌아옴.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                //이미지 선택됨.
                imgSelected = true;
                resultUri = result.getUri();

                //이미지 세팅.
                Glide.with(CardEditActivity.this).load(resultUri).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(img);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //오류 발생.
                Exception error = result.getError();
                Toast.makeText(this, "오류가 발생했습니다.\n다시 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);
    }
}
