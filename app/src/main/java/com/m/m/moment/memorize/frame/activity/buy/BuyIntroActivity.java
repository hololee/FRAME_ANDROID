package com.m.m.moment.memorize.frame.activity.buy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.activity.BaseActivity;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;

public class BuyIntroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_intro);

        ImageView intro1 = findViewById(R.id.intro1);


        //set intro_img image.
        Glide.with(BuyIntroActivity.this).load(R.drawable.intro_img).apply(new RequestOptions().override(1080,6986).centerInside().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(intro1);



        Button button = findViewById(R.id.button);
        //setTypeface.
        button.setTypeface(new TypefaceManager(getApplicationContext()).getTypeNormal());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //move to buy.
                Intent intent = new Intent(getApplicationContext(), BuyActivity.class);
                startActivity(intent);
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
