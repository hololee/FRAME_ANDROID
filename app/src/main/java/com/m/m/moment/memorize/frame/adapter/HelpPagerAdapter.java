package com.m.m.moment.memorize.frame.adapter;

import com.m.m.moment.memorize.frame.fragment.HelpFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class HelpPagerAdapter extends FragmentStatePagerAdapter {

    public HelpPagerAdapter(FragmentManager fm) {
        super(fm);


    }

    @Override
    public Fragment getItem(int i) {

        HelpFragment helpFragment = new HelpFragment(); //full of positions
        helpFragment.initPage(i);

        return helpFragment;

    }


    @Override
    public int getCount() {

        return 6;
    }
}
