package com.m.m.moment.memorize.frame.etc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.manager.FileManager;
import com.m.m.moment.memorize.frame.manager.SharedPreferenceManager;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.wasabeef.blurry.Blurry;

public class Maker {

    Context mCotext;


    public Maker(Context context) {

        this.mCotext = context;


    }


    //create cover Image
    public String makeCover() {
        SharedPreferenceManager manager = new SharedPreferenceManager(mCotext);
        if (manager.getCoverImg() != null) {
            //커버 이미지 블러처리 하고 로고 박고 이미지 저장.

            ImageView imageView = new ImageView(mCotext);
            Blurry.with(mCotext).radius(15).color(Color.parseColor("#55000000")).sampling(4).from(new FileManager(mCotext).getBitmapFromUri(manager.getCoverImg())).into(imageView);

            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap coverImage = drawable.getBitmap();

            //이미지 세팅.
            Bitmap bm = Bitmap.createBitmap(1600, 2400, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bm);
            c.drawColor(Color.parseColor("#ffffff"));

            //setTypeface.
            Paint LogoPaint = new Paint();
            LogoPaint.setAntiAlias(true);
            LogoPaint.setColor(Color.parseColor("#ffffff"));
            LogoPaint.setTextSize(144);
            LogoPaint.setTypeface(new TypefaceManager(mCotext).getQuickTypefaceLight());

            Paint SubPaint = new Paint();
            SubPaint.setAntiAlias(true);
            SubPaint.setColor(Color.parseColor("#ffffff"));
            SubPaint.setTextSize(28);
            SubPaint.setTypeface(new TypefaceManager(mCotext).getQuickTypefaceLight());

            //set Image.
            c.drawBitmap(coverImage, null, new RectF(0, 0, 1600, 2400), new Paint());
            c.drawText("FRAME", 575.5f, 1150f, LogoPaint);
            c.drawText("your life in frame", 690, 1214.5f, SubPaint);


            c.drawBitmap(bm, 0, 0, new Paint());

            String filename = "cover_upload.jpg";
            File file = new File(mCotext.getFilesDir() + "/MM/upload", filename);  // MM/upload

            //루트 확인.
            String filePath = mCotext.getFilesDir() + "/MM/upload";
            File filec = new File(filePath);
            if (!filec.exists())
                filec.mkdirs();

            FileOutputStream os = null;
            try {
                os = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, os);   //비트맵을 PNG파일로 변환
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file.getPath();
        } else {
            //original cover.

            //이미지 세팅.
            Bitmap bm = Bitmap.createBitmap(1600, 2400, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bm);
            c.drawColor(Color.parseColor("#ffffff"));

            //setTypeface.
            Paint LogoPaint = new Paint();
            LogoPaint.setAntiAlias(true);
            LogoPaint.setColor(Color.parseColor("#373230"));
            LogoPaint.setTextSize(144);
            LogoPaint.setTypeface(new TypefaceManager(mCotext).getQuickTypefaceLight());

            Paint SubPaint = new Paint();
            SubPaint.setAntiAlias(true);
            SubPaint.setColor(Color.parseColor("#373230"));
            SubPaint.setTextSize(28);
            SubPaint.setTypeface(new TypefaceManager(mCotext).getQuickTypefaceLight());

            //set Image.
            Paint backPaint = new Paint();
            backPaint.setColor(Color.parseColor("#ffffff"));

            c.drawRect(0, 0, 1600, 2400, backPaint);
            c.drawText("FRAME", 575.5f, 1150f, LogoPaint);
            c.drawText("your life in frame", 690, 1214.5f, SubPaint);


            c.drawBitmap(bm, 0, 0, new Paint());

            String filename = "cover_upload.jpg";
            File file = new File(mCotext.getFilesDir() + "/MM/upload", filename);  // MM/upload

            //루트 확인.
            String filePath = mCotext.getFilesDir() + "/MM/upload";
            File filec = new File(filePath);
            if (!filec.exists())
                filec.mkdirs();

            FileOutputStream os = null;
            try {
                os = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, os);   //비트맵을 PNG파일로 변환
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return file.getPath();
        }
    }


    //create card image.
    public String makeCard(Long ID, Uri image, String title, String date, String content, int number) {

        Bitmap cardImage = new FileManager(mCotext).getBitmapFromUri(image);

        Bitmap bm = Bitmap.createBitmap(1600, 2400, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        c.drawColor(Color.parseColor("#ffffff"));

        //set Image.
        Paint imgPaint = new Paint();
        imgPaint.setColor(Color.parseColor("#F7F7F7"));


        //setTypeface.
        Paint titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setColor(Color.parseColor("#565555"));
        titlePaint.setTextSize(72);
        titlePaint.setTypeface(new TypefaceManager(mCotext).getTypeBold());

        Paint datePaint = new Paint();
        datePaint.setAntiAlias(true);
        datePaint.setColor(Color.parseColor("#565555"));
        datePaint.setTextSize(40);
        datePaint.setTypeface(new TypefaceManager(mCotext).getTypeBold());

        Paint contentPaint = new Paint();
        contentPaint.setAntiAlias(true);
        contentPaint.setColor(Color.parseColor("#38322F"));
        contentPaint.setTextAlign(Paint.Align.RIGHT);
        contentPaint.setTextSize(40);
        contentPaint.setTypeface(new TypefaceManager(mCotext).getTypeBold());

        //line color
        Paint linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#565555"));

        //img background.
        //c.drawRect(51.38f, 51.38f, 1539.38f, 2131.38f, imgPaint);

        //set Image.
        c.drawBitmap(cardImage, null, new RectF(51.38f, 51.38f, 1539.38f, 2131.38f), new Paint());

        //title position x: 54.4; y = 2228.68;
        c.drawText(title, 54.4f, 2228.68f, titlePaint);
        c.drawLine(54.4f, 2293.02f, 239.812f, 2293.02f, linePaint);
        c.drawText(date, 54.4f, 2346.899f, datePaint);

        float x = 1545.6f;
        float y = 2197.68f;
        int currentLine = 1;
        for (String line : content.split("\n")) {
            if (currentLine <= 4) {
                c.drawText(line, x, y, contentPaint);
                y += contentPaint.descent() - contentPaint.ascent();
                currentLine += 1;
            }

        }


        c.drawBitmap(bm, 0, 0, new Paint());

        String filename = ID + "_" + number + "_upload.jpg";
        File file = new File(mCotext.getFilesDir() + "/MM/upload", filename);  // MM/upload

        //루트 확인.
        String filePath = mCotext.getFilesDir() + "/MM/upload";
        File filec = new File(filePath);
        if (!filec.exists())
            filec.mkdirs();

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, os);   //비트맵을 PNG파일로 변환
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return file.getPath();
    }

}
