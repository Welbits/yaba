package com.pilasvacias.yaba.screens.lines;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.VolleyLog;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.adapter.pager.WPagerAdapter;
import com.pilasvacias.yaba.core.network.NetworkFragment;
import com.pilasvacias.yaba.core.widget.EmptyView;
import com.pilasvacias.yaba.screens.lines.commonlines.CommonLinesFragment;
import com.pilasvacias.yaba.screens.lines.nocturnos.NocturnosFragment;
import com.pilasvacias.yaba.screens.lines.universitarios.UniversitariosFragment;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by IzanRodrigo on 16/10/13.
 */
public class LinesFragment extends NetworkFragment {

    // Inject views
    @InjectView(R.id.lines_tabStrip)
    PagerSlidingTabStrip tabStrip;
    @InjectView(R.id.lines_viewPager)
    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        VolleyLog.DEBUG = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.lines_fragment, container, false);
        Views.inject(this, rootView);
        configureViewPager();
        return rootView;
    }

    private void configureViewPager() {
        WPagerAdapter.with(getFragmentManager())
                .setFragments(
                        new CommonLinesFragment(),
                        new NocturnosFragment(),
                        new UniversitariosFragment()
                )
                .setTitles(getResources().getStringArray(R.array.lines_tab_titles))
                .setOffscreenLimit(WPagerAdapter.ALL_FRAGMENTS)
                .into(viewPager);
        tabStrip.setViewPager(viewPager);
    }

}
