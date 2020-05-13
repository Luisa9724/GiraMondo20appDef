package com.example.giramondo20app;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.example.giramondo20app.Model.AccommodationModel;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapterAccommodationOverview extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private List<String> mPageTitleList;
    private FragmentManager mManager;

    public ViewPagerAdapterAccommodationOverview(FragmentManager manager){
        super(manager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragmentList = new ArrayList<>();
        mPageTitleList = new ArrayList<>();
        mManager = manager;
    }
    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mPageTitleList.add(title);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) { return mFragmentList.get(position); }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mPageTitleList.get(position);
    }

}
