package com.m.m.moment.memorize.frame.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.m.m.moment.memorize.frame.database.DataBaseManager;
import com.m.m.moment.memorize.frame.fragment.CardFragment;
import com.m.m.moment.memorize.frame.fragment.ListFragment;
import com.m.m.moment.memorize.frame.fragment.MainFragment;
import com.m.m.moment.memorize.frame.item.cardListItem;

import java.util.ArrayList;

import static com.m.m.moment.memorize.frame.activity.MainActivity.PAGE_LIST;
import static com.m.m.moment.memorize.frame.activity.MainActivity.PAGE_MAIN;

public class PagerAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    DataBaseManager dataBaseManager;

    ArrayList<cardListItem> arrayList;

    public PagerAdapter(Context context, FragmentManager fm) {
        super(fm);

        mContext = context;
        //getting data from card info database and setting.
        this.dataBaseManager = new DataBaseManager(context);
        arrayList = dataBaseManager.getAllCardItemList();

    }

    @Override
    public Fragment getItem(int i) {

        CardFragment cardFragment = new CardFragment(); //other positions
        MainFragment mainFragment = new MainFragment(); //position 1 page
        ListFragment listFragment = new ListFragment(); //postiion 0 page


        //리스트, 메인, 카드 순으로 배치.
        if (i == PAGE_LIST) {
            listFragment.initPage(i);
            return listFragment;
        } else if (i == PAGE_MAIN) {
            mainFragment.initPage(i);
            return mainFragment;
        } else {
            cardFragment.initPage(arrayList.get(i - 2), i, arrayList.size());// because of list and main.
            return cardFragment;
        }

    }


    @Override
    public int getCount() {
        //include list and main page.
        return arrayList.size() + 2;// because of list and main.
    }
}
