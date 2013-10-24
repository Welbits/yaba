package com.pilasvacias.yaba.core.adapter.pager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IzanRodrigo on 14/10/13.
 */
public class WPagerAdapter extends FragmentPagerAdapter {

    /**
     * Disable offscreen limit (default value).
     */
    public static final int NO_OFFSCREEN_LIMIT = -1;
    /**
     * Set offscreen limit to number of added fragments.
     */
    public static final int ALL_FRAGMENTS = -2;
    // Fields
    private List<? extends Fragment> fragments;
    private List<? extends CharSequence> titles;
    private int offscreenLimit = NO_OFFSCREEN_LIMIT;
    private ViewPager.PageTransformer pageTransformer;
    private boolean reverseDrawingOrder;

    private WPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Creates an empty pager adapter.
     * @param fragmentManager
     * @return
     */
    public static WPagerAdapter with(FragmentManager fragmentManager) {
        WPagerAdapter viewPagerAdapter = new WPagerAdapter(fragmentManager);
        return viewPagerAdapter;
    }

    /**
     * Set fragments to the adapter.
     * @param fragments
     * @return
     */
    public WPagerAdapter setFragments(List<? extends Fragment> fragments) {
        this.fragments = fragments;
        return this;
    }

    /**
     * Set fragments to the adapter.
     * @param fragments
     * @return
     */
    public WPagerAdapter setFragments(Fragment... fragments) {
        this.fragments = Arrays.asList(fragments);
        return this;
    }

    /**
     * Set titles to the adapter.
     * @param titles
     * @return
     */
    public WPagerAdapter setTitles(List<? extends CharSequence> titles) {
        this.titles = titles;
        return this;
    }

    /**
     * Set titles to the adapter.
     * @param titles
     * @return
     */
    public WPagerAdapter setTitles(CharSequence... titles) {
        this.titles = Arrays.asList(titles);
        return this;
    }

    /**
     * Set offscreen limit (disabled by default).
     * @param offscreenLimit
     * @return
     */
    public WPagerAdapter setOffscreenLimit(int offscreenLimit) {
        this.offscreenLimit = offscreenLimit;
        return this;
    }

    /**
     * Set a page transformer to the adapter.
     * @param reverseDrawingOrder
     * @param pageTransformer
     * @return
     */
    public WPagerAdapter setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer pageTransformer) {
        this.reverseDrawingOrder = reverseDrawingOrder;
        this.pageTransformer = pageTransformer;
        return this;
    }

    /**
     * Set adapter to the view pager.
     * @param viewPager
     */
    public void into(ViewPager viewPager) {
        viewPager.setAdapter(this);
        if (offscreenLimit != NO_OFFSCREEN_LIMIT) {
            if (offscreenLimit == ALL_FRAGMENTS) {
                offscreenLimit = fragments.size();
            }
            viewPager.setOffscreenPageLimit(offscreenLimit);
        }
        if (pageTransformer != null) {
            viewPager.setPageTransformer(reverseDrawingOrder, pageTransformer);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null) {
            return titles.get(position);
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    /**
     * Returns a simple page change listener that
     * change tab selected when change the page.
     * Optionally change ActionBar title.
     * @param activity
     * @param changeTitle
     * @return
     */
    public static ViewPager.OnPageChangeListener getSimplePageChangeListener(
            final Activity activity, final boolean changeTitle) {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                // Do nothing
            }

            @Override
            public void onPageSelected(int i) {
                ActionBar actionBar = activity.getActionBar();
                actionBar.setSelectedNavigationItem(i);
                if (changeTitle) {
                    actionBar.setTitle(actionBar.getTabAt(i).getText());
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                // Do nothing
            }
        };
    }

    /**
     * Returns a simple tab listener that changes
     * selected page when click in a tab.
     * @param viewPager
     * @return
     */
    public static ActionBar.TabListener getSimpleTabListener(final ViewPager viewPager) {
        return new ActionBar.TabListener() {
            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // Do nothing
            }

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // Do nothing
            }
        };
    }
}