package com.pilasvacias.yaba.screens;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.common.BaseFragment;
import com.pilasvacias.yaba.common.network.NetworkActivity;
import com.pilasvacias.yaba.common.widget.DepthPageTransformer;
import com.pilasvacias.yaba.common.widget.ViewPagerAdapter;
import com.pilasvacias.yaba.common.widget.ZoomOutPageTransformer;

import java.util.Random;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by IzanRodrigo on 14/10/13.
 */
public class MainActivity extends NetworkActivity {
    // Inject views
    @InjectView(R.id.main_viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Views.inject(this);
        configureViewPager();
        configureTabs();
    }

    private void configureViewPager() {
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(ViewPagerAdapter
                .with(getSupportFragmentManager())
                .setFragments(new Fragment[]{
                        DummyFragment.newInstance("Tab 1"),
                        DummyFragment.newInstance("Tab 2"),
                        DummyFragment.newInstance("Tab 3"),
                        DummyFragment.newInstance("Tab 4")
                }));
        viewPager.setOnPageChangeListener(ViewPagerAdapter.getPageChangeListener(this));
        viewPager.setPageTransformer(true, new Random().nextBoolean() ? new DepthPageTransformer() : new ZoomOutPageTransformer());
    }

    private void configureTabs() {
        // Configure action bar
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle("Tab 1");

        // Add tabs to action bar
        ActionBar.TabListener tabListener = ViewPagerAdapter.getTabListener(viewPager);
        actionBar.addTab(actionBar.newTab()
                .setText("Tab 1")
                .setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab()
                .setText("Tab 2")
                .setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab()
                .setText("Tab 3")
                .setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab()
                .setText("Tab 4")
                .setTabListener(tabListener));
    }

    public static class DummyFragment extends BaseFragment {
        // Constants
        private static final String TITLE_KEY = "title";
        // Inject views
        @InjectView(R.id.dummy_textView)
        TextView textView;
        // Fields
        private String title;

        public static DummyFragment newInstance(String title) {
            DummyFragment fragment = new DummyFragment();
            fragment.title = title;
            return fragment;
        }

        private static int randomColor() {
            Random random = new Random();
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            return Color.argb(255, r, g, b);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_dummy, container, false);
            Views.inject(this, rootView);

            if (savedInstanceState != null) {
                title = savedInstanceState.getString(TITLE_KEY);
            }

            textView.setText(title);
            textView.setBackgroundColor(randomColor());

            return rootView;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putString(TITLE_KEY, title);
        }
    }
}
