package com.m.m.moment.memorize.frame.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;

public class AppActivity extends BaseActivity {

    TextView title, tag0, tag1, tag1_info, tag2, tag2_info, tag3, tag3_info, tag4, tag4_info, tag5, tag5_info, tag6, tag6_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        title = findViewById(R.id.title);
        tag0 = findViewById(R.id.tag0);
        tag1 = findViewById(R.id.tag1);
        tag1_info = findViewById(R.id.tag1_info);
        tag2 = findViewById(R.id.tag2);
        tag2_info = findViewById(R.id.tag2_info);
        tag3 = findViewById(R.id.tag3);
        tag3_info = findViewById(R.id.tag3_info);
        tag4 = findViewById(R.id.tag4);
        tag4_info = findViewById(R.id.tag4_info);
        tag5 = findViewById(R.id.tag5);
        tag5_info = findViewById(R.id.tag5_info);
        tag6 = findViewById(R.id.tag6);
        tag6_info = findViewById(R.id.tag6_info);


        //set back button
        Button button = findViewById(R.id.button);


        //setTypeFace;
        title.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag0.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag1.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag1_info.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag2.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag2_info.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag3.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag3_info.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag4.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag4_info.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag5.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag5_info.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag6.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag6_info.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());


        try {
            PackageInfo i = getPackageManager().getPackageInfo(getPackageName(), 0);
            tag1_info.setText(i.versionName);
        } catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);
    }
}
