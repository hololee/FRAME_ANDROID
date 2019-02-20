package com.m.m.moment.memorize.frame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
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
import com.bumptech.glide.request.target.Target;
import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.etc.Application;
import com.m.m.moment.memorize.frame.manager.SharedPreferenceManager;

public class LoadingDialog extends Dialog {

    Context mContext;
    ImageView loading;

    public LoadingDialog(Context context) {
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

        setContentView(R.layout.dialog_loading);

        loading = findViewById(R.id.loading);

        //취소 금지.
        this.setCancelable(false);


        // gif image.
        Glide.with(mContext).asGif().load(R.drawable.loading).apply(new RequestOptions().fitCenter().override(Target.SIZE_ORIGINAL).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(loading);


    }
}
