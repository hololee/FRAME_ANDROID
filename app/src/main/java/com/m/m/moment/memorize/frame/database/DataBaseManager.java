package com.m.m.moment.memorize.frame.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.m.m.moment.memorize.frame.item.cardListItem;
import com.m.m.moment.memorize.frame.manager.FileManager;

import java.util.ArrayList;


public class DataBaseManager {

    private static final String DatabaseName = "Database";
    private static final String tableCard = "cardList";  //card list.
    private static final String tableOrder = "orderList";  //order list.
    private static final int ver = 1;

    private SqliteHelper helper;
    private SQLiteDatabase db;

    Context mContext;

    public DataBaseManager(Context context) {
        this.helper = new SqliteHelper(context, DatabaseName, null, ver);
        mContext = context;
    }

    public class SqliteHelper extends SQLiteOpenHelper {


        public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //time(ID), img, title, date, content, location
            db.execSQL("create table " + tableCard + " (time_id text, img text, title text, date text, content text, location text)");

            //time, count, item_list(items are seperated by ;)
            db.execSQL("create table " + tableOrder + " (time_id_order text, count text, items_time_id_list text)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    //add card item to list.
    public void addCardItem(long time_id, Uri img, String title, long date, String content, String location) {
        this.db = helper.getWritableDatabase();
        db.execSQL("insert into " + tableCard + " values('" + String.valueOf(time_id) + "', '" + img.toString() + "', '" + title + "', '" + String.valueOf(date) + "', '" + content + "', '" + location + "')");
        db.close();
    }


    //update card item.
    public void updateCardItem(long selected_time_id, Uri img, String title, long date, String content, String location) {
        this.db = helper.getWritableDatabase();
        db.execSQL("update " + tableCard + " set img = '" + img.toString() + "', title = '" + title + "', date = '" + String.valueOf(date) + "', content = '" + content + "', location = '" + location + "'  where time_id = '" + String.valueOf(selected_time_id) + "'");
        db.close();
    }

    //delete card item.
    public void deleteCardItem(long selected_time_id) {
        this.db = helper.getWritableDatabase();
        //delete saved image.
        Cursor cursor = db.rawQuery("select * from " + tableCard + " where time_id = '" + selected_time_id + "'", null);
        cursor.moveToNext();
        new FileManager(mContext).deleteImage(cursor.getString(1));
        cursor.close();
        //erase db data.
        db.execSQL("delete from " + tableCard + " where time_id = '" + selected_time_id + "'");
        db.close();

    }


    //get all card items.
    public ArrayList<cardListItem> getAllCardItemList() {
        this.db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + tableCard, null);
        ArrayList<cardListItem> cardList = new ArrayList<>();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            cardList.add(new cardListItem(Long.parseLong(cursor.getString(0)), Uri.parse(cursor.getString(1)), cursor.getString(2), Long.parseLong(cursor.getString(3)), cursor.getString(4), cursor.getString(5)));
        }

        cursor.close();
        db.close();

        return cardList;

    }

    //get selected item by time_id.
    public cardListItem getCardItem(long time_id) {
        this.db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + tableCard + " where time_id = '" + time_id + "'", null);

        cursor.moveToNext();
        cardListItem selectedItem = new cardListItem(Long.parseLong(cursor.getString(0)), Uri.parse(cursor.getString(1)), cursor.getString(2), Long.parseLong(cursor.getString(3)), cursor.getString(4), cursor.getString(5));

        cursor.close();
        db.close();

        return selectedItem;

    }


    //get ImageUri by time_id.
    public Uri getCardImage(long time_id) {
        this.db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + tableCard + " where time_id = '" + time_id + "'", null);

        cursor.moveToNext();
        Uri cardUri = Uri.parse(cursor.getString(1));
        cursor.close();
        db.close();

        return cardUri;

    }

    /*
    //활동 입력
    public void insertActivityData(String type, String name, String resolve, String tag, String cover_img, String time_id) {
        db.execSQL("insert into " + tableActive + " values('" + type + "', '" + name + "', '" + resolve + "', '" + tag + "', '" + cover_img + "', '" + time_id + "')");
    }

    //글 입력.
    public void insertChildData(String master_id, String title, String img, String content, String align_type, String write_time) {
        db.execSQL("insert into " + tableChild + " values('" + master_id + "', '" + title + "', '" + img + "', '" + content + "', '" + align_type + "', '" + write_time + "')");
    }


    //활동 검색
    public Cursor selectActivityData() {
        Cursor cursor = db.rawQuery("select * from " + tableActive, null);
        return cursor;
    }

    //활동 검색(특정)
    public Cursor selectActivityData(String name) {
        Cursor cursor = db.rawQuery("select * from " + tableActive + " where name = '" + name + "'", null);
        return cursor;
    }

    //글 검색
    public Cursor selectChildData(String master_id) {
        Cursor cursor = db.rawQuery("select * from " + tableChild + " where master_id = '" + master_id + "' order by write_time asc", null);
        return cursor;
    }

    //글 검색(특정)
    public Cursor selectChildData(String master_id, String title) {
        Cursor cursor = db.rawQuery("select * from " + tableChild + " where master_id = '" + master_id + "' and title = '" + title + "'", null);
        return cursor;
    }


    //활동 수정 -> 시간은 아이디이므로 수정 불가.
    public void editActivityData(String old_name, String type, String name, String resolve, String tag, String cover_img) {
        db.execSQL("update " + tableActive + " set type = '" + type + "', name = '" + name + "', resolve = '" + resolve + "', tag = '" + tag + "', cover_img = '" + cover_img + "'  where name = '" + old_name + "'");

    }

    //글 수정 -> master_id 수정 불가
    public void editChildData(String master_id, String old_title, String title, String img, String content, String align_type, String write_time) {
      db.execSQL("update " + tableChild + " set title = '" + title + "', img = '" + img + "', content = '" + content + "', align_type = '" + align_type + "', write_time = '" + write_time + "'  where master_id = '" + master_id + "' and title = '" + old_title + "'");

    }

    //활동 삭제/
    public void deleteActivityData(String name) {
        Cursor cursor = db.rawQuery("select * from " + tableActive + " where name = '" + name + "'", null);
        cursor.moveToNext();
        FileManager.getInstance().deleteImage(mContext, cursor.getString(4));
        cursor.close();
        db.execSQL("delete from " + tableActive + " where name = '" + name + "'");

    }

    //글 삭제
    public void deleteChildData(String master_id, String title) {

        Cursor cursor = db.rawQuery("select * from " + tableChild + " where master_id = '" + master_id + "' and title = '" + title + "'", null);
        cursor.moveToNext();
        FileManager.getInstance().deleteImage(mContext, cursor.getString(2));
        cursor.close();
        db.execSQL("delete from " + tableChild + " where master_id = '" + master_id + "' and title = '" + title + "'");

    }

*/


}
