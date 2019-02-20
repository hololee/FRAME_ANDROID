package com.m.m.moment.memorize.frame.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;

public class FontActivity extends BaseActivity {
    TextView title, tag0, tag1, tag1_info, tag2, tag2_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font);

        title = findViewById(R.id.title);
        tag0 = findViewById(R.id.font_tag0);
        tag1 = findViewById(R.id.font_tag1);
        tag1_info = findViewById(R.id.font_tag1_info);
        tag2 = findViewById(R.id.font_tag2);
        tag2_info = findViewById(R.id.font_tag2_info);


        //set back button
        Button button = findViewById(R.id.button);


        //setTypeFace;
        title.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag0.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag1.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag1_info.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag2.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());
        tag2_info.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());


        tag1_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.fontsquirrel.com/license/quicksand"));
                startActivity(intent);

            }
        });
        tag2_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hangeul.naver.com"));
                startActivity(intent);
            }
        });


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