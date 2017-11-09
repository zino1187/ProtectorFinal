package com.solu.daewha.bluetoothclient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by zino on 2017-11-08.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter{
    Fragment[] fragments=new Fragment[4];

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[0] = new EyeFragment();
        fragments[1] = new MsgFragment();
        fragments[2] = new LedFragment();
        fragments[3] = new SoundFragment();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
