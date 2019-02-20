package com.m.m.moment.memorize.frame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.m.m.moment.memorize.frame.R;

public class ShowImageDialog extends Dialog {

    Context mContext;
    ImageView noticeView;

    Uri ImageURi;
    int mResource = 0;


    public ShowImageDialog(Context context, Uri Image) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mContext = context;
        ImageURi = Image;
    }

    public ShowImageDialog(Context context, int resource) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mContext = context;
        mResource = resource;
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


        if (mResource != 0) {
            Glide.with(mContext).load(mResource).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(noticeView);
        } else {
            Glide.with(mContext).load(ImageURi).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(noticeView);
        }

        //터치시 종료.
        RelativeLayout noticeArea = findViewById(R.id.noticeArea);
        noticeArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
