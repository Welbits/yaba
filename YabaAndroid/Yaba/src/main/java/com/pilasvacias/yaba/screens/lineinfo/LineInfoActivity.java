package com.pilasvacias.yaba.screens.lineinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.adapter.pager.WPagerAdapterNative;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.modules.emt.pojos.Line;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Fede on 24/10/13.
 */
public class LineInfoActivity extends NetworkActivity {

    // Constants
    public static final String LINE_KEY = "line";
    // Inject views
    @InjectView(R.id.lines_tabStrip)
    PagerSlidingTabStrip tabStrip;
    @InjectView(R.id.lines_viewPager)
    ViewPager viewPager;
    // Fields
    private Line line;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lines_fragment);
        Views.inject(this);

        Intent intent = getIntent();
        if (intent.hasExtra(LINE_KEY)) {
            String lineString = getIntent().getStringExtra(LINE_KEY);
            Gson gson = new Gson();
            line = gson.fromJson(lineString, Line.class);
        }

        setTitle(getString(R.string.line, line.getLabel()));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        configureViewPager();
    }

    private void configureViewPager() {
        WPagerAdapterNative
                .with(getFragmentManager())
                .setFragments(
                        LineDestinationFragment.newInstance(line, line.getNameA()),
                        LineDestinationFragment.newInstance(line, line.getNameB())
                )
                .setOffscreenLimit(WPagerAdapterNative.ALL_FRAGMENTS)
                .setTitles(line.getNameA(), line.getNameB())
                .into(viewPager);
        tabStrip.setViewPager(viewPager);
    }

    public Line getLine() {
        return line;
    }

}
