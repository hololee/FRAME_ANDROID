package com.m.m.moment.memorize.frame.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SharedPreferenceManager {

    Context mContext;
    SharedPreferences preferences;

    public SharedPreferenceManager(Context context) {
        this.mContext = context;
        preferences = context.getSharedPreferences("mainPre", Context.MODE_PRIVATE);

    }


    //공지사항 알람 여부.
    public boolean getNoticeAlarmState() {
        return preferences.getBoolean("noticeAlarm", true);

    }

    public void setNoticeAlarmState(boolean state) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("noticeAlarm", state);
        editor.apply();
    }


    //초기 설명 보기.
    public boolean isFirstUsing() {
        return preferences.getBoolean("first", true);
    }

    public void setFirstUsedCheck() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("first", false);
        editor.apply();
    }


    //사용자 이름.
    public String getUserName() {
        return preferences.getString("name", null);

    }

    public void setUserName(String name) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.apply();
    }


    //사용자 전화번호.
    public String getUserCallnumber() {
        return preferences.getString("number", null);

    }

    public void setUserCallnumber(String number) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("number", number);
        editor.apply();
    }


    //사용자 주소.
    public String getUserAddress() {
        return preferences.getString("address", null);

    }

    public void setUserAddress(String address) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("address", address);
        editor.apply();
    }

    //cover image.
    public Uri getCoverImg() {

        //if image had been saved
        if (preferences.getString("cover", null) != null)
            return Uri.parse(preferences.getString("cover", null));
        else { //no image.
            return null;
        }
    }

    public void setCoverImg(Uri uri) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cover", uri.toString());
        editor.apply();
    }

    public void clearCoverImg() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("cover");
        editor.apply();
    }

    public boolean checkPermission() {

        //퍼미션 허용시,
        return preferences.getBoolean("permission", false);

    }

    public void setPermission() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("permission", true);
        editor.apply();

    }


    public Uri checkNotice(final String filePathName) {


        //기존 공지와 같은 공지인 경우.
        if (preferences.getString("notice", "null").equals(filePathName)) {
            //내부 저장공간에 있는 공지 사진을 띄운다.

            Uri uri = new FileManager(mContext).getNoticeUri();

            if (uri != null) {
                //이미지가 존재하면.
                Log.d("notice", "internal");
                return uri;
            } else {
                //내장 이미지가 사라진경우.
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //이미지 저장.
                        new FileManager(mContext).saveNoticeBitmap(getBitmapFromURL(filePathName));
                        //이미지 주소 저장.
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("notice", filePathName);
                        editor.apply();


                    }
                }).start();

                return null;
            }

        } else {
            //다른 공지일 경우.
            // 내부저장 공간으로 공지사진을 옮긴 후에 내부저장공간에 있는 공지사진을 띄운다.

            new Thread(new Runnable() {
                @Override
                public void run() {

                    //이미지 저장.
                    new FileManager(mContext).saveNoticeBitmap(getBitmapFromURL(filePathName));
                    //이미지 주소 저장.
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("notice", filePathName);
                    editor.apply();


                }
            }).start();


            return null;
        }
    }


    private static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
