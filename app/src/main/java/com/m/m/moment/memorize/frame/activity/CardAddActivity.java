package com.m.m.moment.memorize.frame.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
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
import com.m.m.moment.memorize.frame.database.DataBaseManager;
import com.m.m.moment.memorize.frame.dialog.LoadingDialog;
import com.m.m.moment.memorize.frame.manager.DateManager;
import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.manager.FileManager;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Calendar;

public class CardAddActivity extends BaseActivity {

    private static final long MIN_CLICK_INTERVAL = 600; //중복 버튼 클릭 유효 시간.
    private long mLastClickTime;

    ImageView img, img_add_icon;
    EditText title, content;
    TextView date;

    ImageButton location;
    TextView pageAdd;


    String selectedLocation = "null";
    long selectedDate = 0;
    boolean imgSelected = false;
    Uri resultUri;

    DataBaseManager dbManager;
    TypefaceManager typefaceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_add);

        img = findViewById(R.id.img);
        img_add_icon = findViewById(R.id.img_add_icon);
        location = findViewById(R.id.location);

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        date = findViewById(R.id.date);

        pageAdd = findViewById(R.id.page_add);

        typefaceManager = new TypefaceManager(getApplicationContext());

        //폰트 설정.
        title.setTypeface(typefaceManager.getTypeNormal());
        date.setTypeface(typefaceManager.getTypeNormal());
        content.setTypeface(typefaceManager.getTypeNormal());
        pageAdd.setTypeface(typefaceManager.getTypeNormal());


        //기본 시간 설정.
        Calendar cal = Calendar.getInstance();
        selectedDate = cal.getTimeInMillis();
        date.setText(DateManager.getDateText(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));


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


        //이미지 추가.
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setInitialCropWindowPaddingRatio(0)
                        .setAspectRatio(1000, 1398)
                        .start(CardAddActivity.this);

            }
        });


        //날짜 선택.
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(CardAddActivity.this, new DatePickerDialog.OnDateSetListener() {
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


        //카드 추가.
        pageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentClickTime = SystemClock.uptimeMillis();
                long elapsedTime = currentClickTime - mLastClickTime;
                mLastClickTime = currentClickTime;


                //버튼 한번 누른 상태이면 다시 버튼 못누르게
                // 중복 클릭
                if (elapsedTime <= MIN_CLICK_INTERVAL) {

                    Log.d("over", "double down");

                } else {


                    if (!imgSelected || title.length() == 0 || selectedDate == 0) {
                        //날짜선택x, 제목입력x, 이미지x 인 경우.

                        //알림.
                        Toast.makeText(CardAddActivity.this, getString(R.string.alert_add), Toast.LENGTH_SHORT).show();
                    } else {
                        //모든 정보가 잘 들아올경우.
                        //set input time as id.
                        long inputTime = Calendar.getInstance().getTimeInMillis();

                        //이미지 이동 성공시 save data.
                        FileManager fileManager = new FileManager(CardAddActivity.this);
                        try {
                            //copy image.
                            Uri copyImage = fileManager.copyImageToMM(resultUri, String.valueOf(inputTime) + ".jpg");

                            //save data.
                            dbManager = new DataBaseManager(getApplicationContext());
                            dbManager.addCardItem(inputTime, copyImage, title.getText().toString(), selectedDate, content.getText().toString(), selectedLocation);


                            //return to ListFragment.
                            finish();
                            overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);


                        } catch (Exception e) {
                            e.printStackTrace();

                            //알림.
                            Toast.makeText(CardAddActivity.this, getString(R.string.alert_add_error), Toast.LENGTH_SHORT).show();
                        }


                    }


                }


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
                Glide.with(CardAddActivity.this).load(resultUri).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(img);
                img_add_icon.setVisibility(View.GONE);


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
