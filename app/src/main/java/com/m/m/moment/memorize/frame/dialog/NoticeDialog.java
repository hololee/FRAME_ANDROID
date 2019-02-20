package com.m.m.moment.memorize.frame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.m.m.moment.memorize.frame.etc.Application;
import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.manager.SharedPreferenceManager;

public class NoticeDialog extends Dialog {

    Context mContext;
    ImageView noticeView;

    public NoticeDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set background darken.
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        getWindow().setAttributes(params);

        setContentView(R.layout.dialog_notice);

        this.setCancelable(true);

        noticeView = findViewById(R.id.notice);

        //터치시 종료.
        RelativeLayout noticeArea = findViewById(R.id.noticeArea);
        noticeArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        //get notice data from server.
        StringRequest req = new StringRequest(Request.Method.POST, "http://lccandol.cafe24.com/notice.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {

                    SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(mContext);
                    if (sharedPreferenceManager.checkNotice(s) == null) {
                        //처음 가져오는 경우.
                        //공지 설정.
                        Glide.with(mContext).load(s).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(noticeView);
                    } else {
                        //내장공간에 저장된 이미지 가져오기.
                        //공지 설정.
                        Glide.with(mContext).load(sharedPreferenceManager.checkNotice(s)).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(noticeView);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });


        Application.getInstance().addToRequestQueue(req);

    }


}
