package com.pilasvacias.yaba.core.adapter.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.screens.MainActivity;
import com.pilasvacias.yaba.screens.favorites.FavoritesFragment;
import com.pilasvacias.yaba.screens.lines.LinesFragment;
import com.pilasvacias.yaba.screens.nocturnos.NocturnosFragment;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IzanRodrigo on 14/10/13.
 */
public class WPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<CharSequence> titles;

    private WPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public static WPagerAdapter with(FragmentManager fragmentManager) {
        WPagerAdapter viewPagerAdapter = new WPagerAdapter(fragmentManager);
        return viewPagerAdapter;
    }

    public static ViewPager.OnPageChangeListener getPageChangeListener(final ActionBarActivity activity) {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                // Do nothing
            }

            @Override
            public void onPageSelected(int i) {
                ActionBar actionBar = activity.getSupportActionBar();
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

    public WPagerAdapter configureWithTabs(Context context) {
        this.fragments = Tab.getFragments();
        this.titles = Tab.getTitles(context);
        return this;
    }

    public WPagerAdapter setFragments(Fragment... fragments) {
        this.fragments = Arrays.asList(fragments);
        return this;
    }

    public WPagerAdapter setTitles(CharSequence... titles) {
        this.titles = Arrays.asList(titles);
        return this;
    }

    public void into(ViewPager viewPager) {
        viewPager.setAdapter(this);
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

    public enum Tab {
        FAVORITES(R.string.tab_favorites, new FavoritesFragment()),
        LINES(R.string.tab_lines, new LinesFragment()),
        //FIXME: Cambiar el nombre a inglés
        NOCTURNOS(R.string.tab_nocturnos, new NocturnosFragment()),
        FIND(R.string.tab_find, MainActivity.DummyFragment.newInstance(R.string.tab_find));
        private final int titleResource;
        private final Fragment fragment;

        Tab(int titleResource, Fragment fragment) {
            this.titleResource = titleResource;
            this.fragment = fragment;
        }

        public static List<Fragment> getFragments() {
            List<Fragment> list = new LinkedList<Fragment>();
            for (Tab tab : values()) {
                list.add(tab.getFragment());
            }
            return list;
        }

        public static List<CharSequence> getTitles(Context context) {
            List<CharSequence> list = new LinkedList<CharSequence>();
            for (Tab tab : values()) {
                list.add(context.getString(tab.getTitleResource()));
            }
            return list;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public int getTitleResource() {
            return titleResource;
        }
    }

}
