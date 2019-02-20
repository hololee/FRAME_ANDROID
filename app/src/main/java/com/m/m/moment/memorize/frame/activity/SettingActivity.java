package com.m.m.moment.memorize.frame.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.manager.FileManager;
import com.m.m.moment.memorize.frame.manager.SharedPreferenceManager;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;
import com.theartofdev.edmodo.cropper.CropImage;

import androidx.annotation.Nullable;
import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

public class SettingActivity extends BaseActivity {


    private final static int CODE_ADDRESS = 3331;

    ImageButton backButton;

    TextView message, save, name_tag, phone_tag, address_tag, data_text, notice_text, cover_text, help_text, open_text, font_text, cafe_text, app_text, delivery_text;
    TextView backupBtn, restoreBtn, noticeToggleBtn, coverChangeBtn, coverClearBtn, helpButton, openButton, fontButton, cafeButton, appButton, deliveryBtn;
    EditText nameEdit, phoneEdit;
    TextView addressEdit;

    boolean noticeValue;
    SharedPreferenceManager manager;
    FileManager fileManager;

    Uri coverImage;

    boolean dataChanged = false;
    boolean coverChanged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        message = findViewById(R.id.message);
        save = findViewById(R.id.save);
        name_tag = findViewById(R.id.name_tag);
        phone_tag = findViewById(R.id.phone_tag);
        address_tag = findViewById(R.id.address_tag);
        data_text = findViewById(R.id.data_text);
        notice_text = findViewById(R.id.notice_text);
        cover_text = findViewById(R.id.cover_text);
        help_text = findViewById(R.id.help_text);
        open_text = findViewById(R.id.open_text);
        font_text = findViewById(R.id.font_text);
        cafe_text = findViewById(R.id.cafe_text);
        app_text = findViewById(R.id.app_text);
        delivery_text = findViewById(R.id.delivery_text);

        backButton = findViewById(R.id.backBtn);
        backupBtn = findViewById(R.id.backup_button);
        restoreBtn = findViewById(R.id.restore_button);
        noticeToggleBtn = findViewById(R.id.notice_toggle_button);
        coverChangeBtn = findViewById(R.id.cover_change_button);
        coverClearBtn = findViewById(R.id.cover_clear_button);
        nameEdit = findViewById(R.id.name_text);
        phoneEdit = findViewById(R.id.phone_text);
        addressEdit = findViewById(R.id.address_text);
        helpButton = findViewById(R.id.help_button);
        openButton = findViewById(R.id.open_button);
        fontButton = findViewById(R.id.font_button);
        cafeButton = findViewById(R.id.cafe_button);
        appButton = findViewById(R.id.app_button);
        deliveryBtn = findViewById(R.id.delivery_button);

        //set typeface.
        TypefaceManager typefaceManager = new TypefaceManager(getApplicationContext());
        message.setTypeface(typefaceManager.getTypeNormal());
        save.setTypeface(typefaceManager.getTypeNormal());
        name_tag.setTypeface(typefaceManager.getTypeNormal());
        phone_tag.setTypeface(typefaceManager.getTypeNormal());
        address_tag.setTypeface(typefaceManager.getTypeNormal());
        data_text.setTypeface(typefaceManager.getTypeNormal());
        notice_text.setTypeface(typefaceManager.getTypeNormal());
        cover_text.setTypeface(typefaceManager.getTypeNormal());
        help_text.setTypeface(typefaceManager.getTypeNormal());
        open_text.setTypeface(typefaceManager.getTypeNormal());
        font_text.setTypeface(typefaceManager.getTypeNormal());
        cafe_text.setTypeface(typefaceManager.getTypeNormal());
        app_text.setTypeface(typefaceManager.getTypeNormal());
        delivery_text.setTypeface(typefaceManager.getTypeNormal());
        backupBtn.setTypeface(typefaceManager.getTypeNormal());
        restoreBtn.setTypeface(typefaceManager.getTypeNormal());
        noticeToggleBtn.setTypeface(typefaceManager.getTypeNormal());
        coverChangeBtn.setTypeface(typefaceManager.getTypeNormal());
        coverClearBtn.setTypeface(typefaceManager.getTypeNormal());
        nameEdit.setTypeface(typefaceManager.getTypeNormal());
        phoneEdit.setTypeface(typefaceManager.getTypeNormal());
        addressEdit.setTypeface(typefaceManager.getTypeNormal());
        helpButton.setTypeface(typefaceManager.getTypeNormal());
        openButton.setTypeface(typefaceManager.getTypeNormal());
        fontButton.setTypeface(typefaceManager.getTypeNormal());
        cafeButton.setTypeface(typefaceManager.getTypeNormal());
        appButton.setTypeface(typefaceManager.getTypeNormal());
        deliveryBtn.setTypeface(typefaceManager.getTypeNormal());


        manager = new SharedPreferenceManager(SettingActivity.this);
        fileManager = new FileManager(SettingActivity.this);

        //init Data.
        //get notice state.
        noticeValue = manager.getNoticeAlarmState();
        if (noticeValue) {
            //set alarm state.
            noticeToggleBtn.setText(getString(R.string.on));
        } else {
            noticeToggleBtn.setText(getString(R.string.off));
        }

        //get delivery data.
        if (manager.getUserName() != null)
            nameEdit.setText(manager.getUserName());
        if (manager.getUserCallnumber() != null)
            phoneEdit.setText(manager.getUserCallnumber());
        if (manager.getUserAddress() != null)
            addressEdit.setText(manager.getUserAddress());

        //get cover data.
        coverImage = manager.getCoverImg();


        //설정 저장.
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataChanged) {
                    //저장.
                    //save changed data.
                    manager.setUserName(nameEdit.getText().toString());
                    manager.setUserCallnumber(phoneEdit.getText().toString());
                    manager.setUserAddress(addressEdit.getText().toString());

                    //save notice value.
                    manager.setNoticeAlarmState(noticeValue);

                    //save cover img.
                    if (coverChanged) {
                        //이미지 이동 성공시 save data.
                        FileManager fileManager = new FileManager(SettingActivity.this);
                        try {
                            //copy image.
                            Uri copyImage = fileManager.copyCoverToMM(coverImage, "main_cover.jpg");

                            //save data.
                            manager.setCoverImg(copyImage);


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SettingActivity.this, getString(R.string.alert_add_error), Toast.LENGTH_SHORT).show();
                        }
                    }

                    Toast.makeText(SettingActivity.this, getString(R.string.save_text), Toast.LENGTH_SHORT).show();


                    finish();
                    overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);

                } else {

                    Toast.makeText(SettingActivity.this, getString(R.string.no_save), Toast.LENGTH_SHORT).show();

                }

            }
        });

        //set notice state.
        noticeToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //데이터 변경 체크.
                dataChanged = true;

                // notice off.
                if (noticeValue) {
                    noticeValue = false;
                    noticeToggleBtn.setText(getString(R.string.off));
                } else {
                    noticeValue = true;
                    noticeToggleBtn.setText(getString(R.string.on));
                }

            }
        });


        //set cover data.
        coverChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CropImage.activity()
                        .setAspectRatio(4, 6)
                        .setInitialCropWindowPaddingRatio(0)
                        .start(SettingActivity.this);
            }
        });

        //clear cover data.
        coverClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setMessage(getString(R.string.alert_cover_clear_message));
                builder.setPositiveButton(getString(R.string.save_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        manager.clearCoverImg();
                        //파일 삭제.
                        new FileManager(getApplicationContext()).deleteCover();

                        Toast.makeText(SettingActivity.this, getString(R.string.alert_cover_clear), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton(getString(R.string.save_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });


        //데이터 변경 체크.
        nameEdit.addTextChangedListener(new infoChanger());
        phoneEdit.addTextChangedListener(new infoPhoneChanger());

        //주소 설정,.
        addressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SetAddressActivity.class);
                startActivityForResult(intent, CODE_ADDRESS);

            }
        });


        //도움말 버튼.
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent settingIntent = new Intent(SettingActivity.this, HelpActivity.class);
                startActivity(settingIntent);
                overridePendingTransition(R.anim.translate_right_appeared, R.anim.translate_right_disappeared);
            }
        });


        //오픈소스 라이센스.
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Notices notices = new Notices();
                notices.addNotice(new Notice("TedPermission", "https://github.com/ParkSangGwon/TedPermission", "Ted Park", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Glide", "https://github.com/bumptech/glide/blob/master/LICENSE", "Google", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Blurry", "https://github.com/wasabeef/Blurry", "Wasabeef", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("zip4j", "http://www.lingala.net/zip4j/", "Srikanth Reddy Lingala", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("LicensesDialog", "https://github.com/PSDev/LicensesDialog", "Philip Schiffer", new ApacheSoftwareLicense20()));

                new LicensesDialog.Builder(SettingActivity.this)
                        .setTitle("LIST")
                        .setNotices(notices)
                        .setIncludeOwnLicense(true)
                        .build()
                        .show();
            }
        });


        //카페 버튼.
        cafeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cafe.naver.com/framelife"));
                startActivity(intent);
            }
        });


        //베송조회 버튼.
        deliveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.cafe.naver.com/ArticleList.nhn?search.clubid=29609940&search.menuid=17&search.boardtype=L"));
                startActivity(intent);
            }
        });


        //앱 정보 버튼.
        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent settingIntent = new Intent(SettingActivity.this, AppActivity.class);
                startActivity(settingIntent);
                overridePendingTransition(R.anim.translate_right_appeared, R.anim.translate_right_disappeared);
            }
        });


        //폰트 정보 버튼.
        fontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent settingIntent = new Intent(SettingActivity.this, FontActivity.class);
                startActivity(settingIntent);
                overridePendingTransition(R.anim.translate_right_appeared, R.anim.translate_right_disappeared);


            }
        });



        //backup & restore
        backupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //zip MM folder to top.
                fileManager.backupAllData(SettingActivity.this);
            }

        });
        restoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //zip MM folder to top.
                fileManager.restoreAllData(SettingActivity.this);
            }
        });


        //뒤로가기 버튼.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataChanged) {
                    //저장 정보 변경 설정.
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setMessage(getString(R.string.setting_back_text));
                    builder.setPositiveButton(getString(R.string.save_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            //저장.
                            //save changed data.
                            manager.setUserName(nameEdit.getText().toString());
                            manager.setUserCallnumber(phoneEdit.getText().toString());
                            manager.setUserAddress(addressEdit.getText().toString());

                            //save notice value.
                            manager.setNoticeAlarmState(noticeValue);

                            //save cover img.
                            if (coverChanged) {
                                //이미지 이동 성공시 save data.
                                FileManager fileManager = new FileManager(SettingActivity.this);
                                try {
                                    //copy image.
                                    Uri copyImage = fileManager.copyCoverToMM(coverImage, "main_cover.jpg");

                                    //save data.
                                    manager.setCoverImg(copyImage);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(SettingActivity.this, getString(R.string.alert_add_error), Toast.LENGTH_SHORT).show();
                                }
                            }


                            Toast.makeText(SettingActivity.this, getString(R.string.save_text), Toast.LENGTH_SHORT).show();


                            finish();
                            overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);

                        }
                    });
                    builder.setNegativeButton(getString(R.string.save_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);
                        }
                    });
                    builder.setCancelable(false);
                    AlertDialog dial = builder.create();
                    dial.show();

                } else {
                    finish();
                    overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);
                }


            }
        });
    }


    //text 채인져

    class infoChanger implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            dataChanged = true;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class infoPhoneChanger extends PhoneNumberFormattingTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            super.beforeTextChanged(s, start, count, after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            super.onTextChanged(s, start, before, count);
            dataChanged = true;
        }

        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            //커버 이미지 세팅.
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                //데이터 변경 체크.
                dataChanged = true;
                coverChanged = true;

                coverImage = result.getUri();


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //오류 발생.
                Exception error = result.getError();
                Toast.makeText(this, "오류가 발생했습니다.\n다시 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CODE_ADDRESS) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                    //데이터 변경 체크.
                    dataChanged = true;

                    //주소설정.
                    addressEdit.setText(data.getStringExtra("address"));

                } else {
                    Toast.makeText(this, "오류가 발생했습니다.\n다시 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (dataChanged) {
            //저장 정보 변경 설정.
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            builder.setMessage(getString(R.string.setting_back_text));
            builder.setPositiveButton(getString(R.string.save_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    //저장.
                    //save changed data.
                    manager.setUserName(nameEdit.getText().toString());
                    manager.setUserCallnumber(phoneEdit.getText().toString());
                    manager.setUserAddress(addressEdit.getText().toString());

                    //save notice value.
                    manager.setNoticeAlarmState(noticeValue);

                    //save cover img.
                    if (coverChanged) {
                        //이미지 이동 성공시 save data.
                        FileManager fileManager = new FileManager(SettingActivity.this);
                        try {
                            //copy image.
                            Uri copyImage = fileManager.copyCoverToMM(coverImage, "main_cover.jpg");

                            //save data.
                            manager.setCoverImg(copyImage);


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SettingActivity.this, getString(R.string.alert_add_error), Toast.LENGTH_SHORT).show();
                        }
                    }

                    Toast.makeText(SettingActivity.this, getString(R.string.save_text), Toast.LENGTH_SHORT).show();


                    finish();
                    overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);

                }
            });
            builder.setNegativeButton(getString(R.string.save_no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);
                }
            });
            builder.setCancelable(false);
            AlertDialog dial = builder.create();
            dial.show();
        } else {
            finish();
            overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);
        }


    }
}
