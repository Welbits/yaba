package com.pilasvacias.yaba.screens;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.BaseFragment;
import com.pilasvacias.yaba.core.adapter.pager.WPagerAdapter;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.core.adapter.pager.DepthPageTransformer;
import com.pilasvacias.yaba.core.adapter.pager.ZoomOutPageTransformer;
import com.pilasvacias.yaba.screens.favorites.FavoritesFragment;
import com.pilasvacias.yaba.screens.lines.LinesFragment;

import java.util.Random;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by IzanRodrigo on 14/10/13.
 */
public class MainActivity extends NetworkActivity {
    // Inject views
    @InjectView(R.id.main_tabStrip)
    PagerSlidingTabStrip tabStrip;
    @InjectView(R.id.main_viewPager)
    ViewPager viewPager;
    // Fields
    private String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Views.inject(this);
        configureViewPager();
    }

    private void configureViewPager() {
        titles = getResources().getStringArray(R.array.tab_titles);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(WPagerAdapter
                .with(getSupportFragmentManager())
                .setFragments(
                        new FavoritesFragment(),
                        new LinesFragment(),
                        DummyFragment.newInstance(titles[2]),
                        DummyFragment.newInstance(titles[3])
                )
                .setTitles(titles));
        viewPager.setPageTransformer(true, new Random().nextBoolean()
                ? new DepthPageTransformer()
                : new ZoomOutPageTransformer());
        tabStrip.setViewPager(viewPager);
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
