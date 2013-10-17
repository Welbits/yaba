package com.pilasvacias.yaba.core.adapter.pager;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by IzanRodrigo on 14/10/13.
 */
public class WPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;
    private CharSequence[] titles;

    private WPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public static WPagerAdapter with(FragmentManager fragmentManager) {
        WPagerAdapter viewPagerAdapter = new WPagerAdapter(fragmentManager);
        return viewPagerAdapter;
    }

    public WPagerAdapter setFragments(Fragment... fragments) {
        this.fragments = fragments;
        return this;
    }

    public WPagerAdapter setTitles(CharSequence... titles) {
        this.titles = titles;
        return this;
    }

    public static ViewPager.OnPageChangeListener getPageChangeListener(final FragmentActivity activity) {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                // Do nothing
            }

            @Override
            public void onPageSelected(int i) {
                ActionBar actionBar = activity.getActionBar();
                actionBar.setSelectedNavigationItem(i);
                actionBar.setTitle(actionBar.getTabAt(i).getText());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                // Do nothing
            }
        };
    }

    public static ActionBar.TabListener getTabListener(final ViewPager viewPager) {
        return new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                // Do nothing
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                // Do nothing
            }
        };
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null) {
            return titles[position];
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
