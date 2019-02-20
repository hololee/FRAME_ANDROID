package com.m.m.moment.memorize.frame.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.adapter.HelpPagerAdapter;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;

import androidx.viewpager.widget.ViewPager;

public class HelpActivity extends BaseActivity {


    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        pager = findViewById(R.id.pager);

        //set help pager
        HelpPagerAdapter adapter = new HelpPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);


        //set back button
        Button button = findViewById(R.id.button);

        //setTypeface
        TypefaceManager typefaceManager = new TypefaceManager(getApplicationContext());
        button.setTypeface(typefaceManager.getTypeNormal());

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
