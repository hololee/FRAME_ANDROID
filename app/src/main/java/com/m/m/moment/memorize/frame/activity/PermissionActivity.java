package com.m.m.moment.memorize.frame.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.manager.FileManager;
import com.m.m.moment.memorize.frame.manager.SharedPreferenceManager;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;

import java.io.File;
import java.util.List;

public class PermissionActivity extends BaseActivity {


    TextView title, sub_title, permission2, permission2_content, permission1, permission1_content;
    Button button;

    PermissionListener permissionlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        button = findViewById(R.id.per_btn);
        title = findViewById(R.id.title);
        sub_title = findViewById(R.id.sub_title);
        permission2 = findViewById(R.id.permission2);
        permission2_content = findViewById(R.id.permission2_content);
        permission1 = findViewById(R.id.permission1);
        permission1_content = findViewById(R.id.permission1_content);


        //set Typeface.
        TypefaceManager typefaceManager = new TypefaceManager(getApplicationContext());
        button.setTypeface(typefaceManager.getTypeNormal());
        title.setTypeface(typefaceManager.getTypeNormal());
        sub_title.setTypeface(typefaceManager.getTypeNormal());
        permission2.setTypeface(typefaceManager.getTypeNormal());
        permission2_content.setTypeface(typefaceManager.getTypeNormal());
        permission1.setTypeface(typefaceManager.getTypeNormal());
        permission1_content.setTypeface(typefaceManager.getTypeNormal());

        //check permission;
        if (new SharedPreferenceManager(getApplicationContext()).checkPermission()) {
            //퍼미션 허용시,
            goMain();
        }

        //set permission listener.
        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //퍼미션 허용.
                new SharedPreferenceManager(getApplicationContext()).setPermission();

                //go main page.
                goMain();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(PermissionActivity.this, getString(R.string.alert_permission_denied), Toast.LENGTH_SHORT).show();
                finish();
            }
        };


        TedPermission.with(PermissionActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(getString(R.string.denied_message))
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        //퍼미션 체크.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TedPermission.with(PermissionActivity.this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage(getString(R.string.denied_message))
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();

            }
        });
    }


    private void goMain() {

        //go to main page.
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("start", true);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.transperant_show, R.anim.transperant_hide);
    }

}
