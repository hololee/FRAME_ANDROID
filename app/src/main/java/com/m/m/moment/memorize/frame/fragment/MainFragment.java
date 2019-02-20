package com.m.m.moment.memorize.frame.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import jp.wasabeef.blurry.Blurry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.m.m.moment.memorize.frame.activity.SettingActivity;
import com.m.m.moment.memorize.frame.manager.FileManager;
import com.m.m.moment.memorize.frame.manager.SharedPreferenceManager;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;
import com.m.m.moment.memorize.frame.R;

public class MainFragment extends Fragment {

    int pageNumber;
    Context mContext;

    ImageView mainCover;
    ImageButton btn_setting;

    TextView logo, logoSub;

    RelativeLayout bottomBar;

    TypefaceManager typefaceManager;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View main = inflater.inflate(R.layout.fragment_main, container, false);

        mContext = getActivity().getApplicationContext();

        typefaceManager = new TypefaceManager(mContext);
        mainCover = main.findViewById(R.id.main_cover);

        logo = main.findViewById(R.id.logo);
        logoSub = main.findViewById(R.id.logo_sub);

        bottomBar = main.findViewById(R.id.bottom_bar);

        //button placed on top
        btn_setting = main.findViewById(R.id.setting);

        //버튼 설정.
        btn_setting.setOnClickListener(new OnTopButtonClickListener());

        //폰트설정.
        logo.setTypeface(typefaceManager.getQuickTypefaceLight());
        logoSub.setTypeface(typefaceManager.getQuickTypefaceLight());


        //set Cover image.
        SharedPreferenceManager manager = new SharedPreferenceManager(mContext);
        if (manager.getCoverImg() != null) {
            //had a custom cover.
            //Glide.with(mContext).load(manager.getCoverImg()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(mainCover);

            logo.setTextColor(Color.WHITE);
            logoSub.setTextColor(Color.WHITE);

            //블러 적용 배경.
            Blurry.with(mContext).radius(15).color(Color.parseColor("#55000000")).sampling(4).from(new FileManager(mContext).getBitmapFromUri(manager.getCoverImg())).into(mainCover);


            //설정 버튼 변경.
            btn_setting.setImageResource(R.drawable.selector_setting_white);


        } else {
            //original cover.
            Glide.with(mContext).load(R.drawable.original_cover).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(mainCover);
        }


        return main;
    }


    public class OnTopButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {


                case R.id.setting:
                    //설정 페이지로 이동.
                    Intent settingIntent = new Intent(mContext, SettingActivity.class);
                    startActivity(settingIntent);
                    getActivity().overridePendingTransition(R.anim.translate_right_appeared, R.anim.translate_right_disappeared);
                    break;


            }
        }
    }


    public void initPage(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
