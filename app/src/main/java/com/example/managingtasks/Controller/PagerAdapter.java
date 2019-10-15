package com.example.managingtasks.Controller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class PagerAdapter extends FragmentStatePagerAdapter {

    private String userID;


    public PagerAdapter(FragmentManager fm, String name) {
        super(fm);
        userID = name;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {

        return TabFragment.newInstance(position, userID);

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "TODO";

            case 1:
                return "DOING";
            case 2:
                return "DONE";
            default:
                return null;
        }
    }

}