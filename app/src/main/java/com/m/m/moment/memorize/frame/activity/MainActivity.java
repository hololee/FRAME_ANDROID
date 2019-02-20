package com.m.m.moment.memorize.frame.activity;

import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.manager.SharedPreferenceManager;
import com.m.m.moment.memorize.frame.adapter.PagerAdapter;
import com.m.m.moment.memorize.frame.dialog.NoticeDialog;

public class MainActivity extends BaseActivity {

    public final static int PAGE_LIST = 1;
    public final static int PAGE_MAIN = 0;
    static ViewPager pager;
    PagerAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();

        //after edit or delete.
        invalidatePager();

        //only app start.
        if (getIntent().getBooleanExtra("start", false)) {

            //0번째 페이지가 메인페이지가 되어야한다.
            pager.setCurrentItem(PAGE_MAIN);
            getIntent().removeExtra("start");

        }
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //앱 중심이 되는 페이저.
        pager = findViewById(R.id.pager);
        invalidatePager();

        //페이지 이동 감지.
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        //공지사항 표시.
        //if alarm is allowed
        if (new SharedPreferenceManager(getApplicationContext()).getNoticeAlarmState()) {
            //show notice dialog.
            NoticeDialog dialog = new NoticeDialog(MainActivity.this);
            dialog.show();

        }

        //처음 사용자 도움말표시.
        if (new SharedPreferenceManager(MainActivity.this).isFirstUsing()) {
            //처음 사용하는경우. 사용함으로 체크.
            new SharedPreferenceManager(MainActivity.this).setFirstUsedCheck();

            //도움말 보기.
            Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);


        }


    }


    //invalidate pager.
    public void invalidatePager() {
        if (pager != null) {
            //invalidate data set.
            adapter = new PagerAdapter(MainActivity.this, getSupportFragmentManager());
            pager.setAdapter(adapter);

        }

    }


    //move to selected card page.
    public static void moveToSelectedPage(int position) {
        pager.setCurrentItem(position);
    }


    @Override
    public void onBackPressed() {

        //메인 페이지 경우 앱 종료.
        if (pager.getCurrentItem() == PAGE_MAIN) {
            finish();
        } else if (pager.getCurrentItem() == PAGE_LIST) {
            //list 페이지경우 main 페이지로 이동.
            pager.setCurrentItem(PAGE_MAIN);

        } else {//list 페이지, main 페이지가 아닌 경우 list 페이지로 이동.
            pager.setCurrentItem(PAGE_LIST);

        }
    }
}
