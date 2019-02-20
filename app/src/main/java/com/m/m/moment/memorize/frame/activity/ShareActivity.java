package com.m.m.moment.memorize.frame.activity;

import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.manager.FileManager;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareActivity extends BaseActivity {

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        layout = findViewById(R.id.background);

        ImageView img = findViewById(R.id.img);
        TextView title = findViewById(R.id.title);
        TextView date = findViewById(R.id.date);
        TextView content = findViewById(R.id.content);
        TextView logo = findViewById(R.id.logo);


        //set Typeface.
        TypefaceManager typefaceManager = new TypefaceManager(getApplicationContext());
        title.setTypeface(typefaceManager.getTypeNormal());
        date.setTypeface(typefaceManager.getTypeNormal());
        content.setTypeface(typefaceManager.getTypeNormal());

        logo.setTypeface(typefaceManager.getQuickTypefaceLight());


        //setData

        //resize img.
        Bitmap a = new FileManager(ShareActivity.this).getBitmapFromUri(Uri.parse(getIntent().getStringExtra("img")));
        Bitmap resizedBmp = resizeBitmapImage(a, 700);

        img.setImageBitmap(resizedBmp);

        title.setText(getIntent().getStringExtra("title"));
        date.setText(getIntent().getStringExtra("date"));
        content.setText(getIntent().getStringExtra("content"));


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //create img.
                Bitmap bitmap = createBitmapFromView(layout);
                String filename = "temp.png";
                File file = new File(getFilesDir() + "/", filename);  //Pictures폴더 screenshot.png 파일
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);   //비트맵을 PNG파일로 변환
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file));
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();

        //공유시 종료..
        finish();
    }

    public Bitmap resizeBitmapImage(Bitmap source, int maxResolution) {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if (width > height) {
            if (maxResolution < width) {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        } else {
            if (maxResolution < height) {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }


    public Bitmap createBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return bitmap;
    }
}
