package com.example.windzlord.brainmaster.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.windzlord.brainmaster.screens.tabs.FragmentWelcome;
import com.example.windzlord.brainmaster.screens.tabs.FragmentPractice;
import com.example.windzlord.brainmaster.screens.tabs.FragmentProfile;
import com.example.windzlord.brainmaster.screens.tabs.FragmentRanking;

/**
 * Created by WindzLord on 11/28/2016.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numberOfTab;

    public PagerAdapter(FragmentManager manager, int numberOfTab) {
        super(manager);
        this.numberOfTab = numberOfTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentWelcome();
            case 1:
                return new FragmentPractice();
            case 2:
                return new FragmentRanking();
            case 3:
                return new FragmentProfile();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTab;
    }
}