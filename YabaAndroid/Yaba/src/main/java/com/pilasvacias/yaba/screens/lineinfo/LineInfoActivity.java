package com.pilasvacias.yaba.screens.lineinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.adapter.pager.WPagerAdapter;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.modules.emt.pojos.Line;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Fede on 24/10/13.
 */
public class LineInfoActivity extends NetworkActivity {

    // Inject views
    @InjectView(R.id.lines_tabStrip)
    PagerSlidingTabStrip tabStrip;
    @InjectView(R.id.lines_viewPager)
    ViewPager viewPager;
    //
    private Line line;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lines_fragment);
        Views.inject(this);

        Intent intent = getIntent();
        String lineClass;
        if (intent.hasExtra("line")) {
            lineClass = getIntent().getStringExtra("line");
            Gson gson = new Gson();
            line = gson.fromJson(lineClass, Line.class);
        }

        setTitle(getString(R.string.line,  line.Label));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        configureViewPager();
    }

    private void configureViewPager() {
        WPagerAdapter
                .with(getFragmentManager())
                .setFragments(
                        new LineDestinationFragment(line.NameA),
                        new LineDestinationFragment(line.NameB)
                )
                .setOffscreenLimit(2)
                .setTitles(line.NameA, line.NameB)
                .into(viewPager);
        tabStrip.setViewPager(viewPager);
    }

    public Line getLine() {
        return line;
    }

}
