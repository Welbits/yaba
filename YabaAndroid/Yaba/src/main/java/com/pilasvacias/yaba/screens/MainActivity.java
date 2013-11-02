package com.pilasvacias.yaba.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.adapter.pager.WPagerAdapterNative;
import com.pilasvacias.yaba.core.debug.DummyFragment;
import com.pilasvacias.yaba.core.drawer.DrawerViewPager;
import com.pilasvacias.yaba.core.drawer.WDrawerLayoutActivityNative;
import com.pilasvacias.yaba.screens.alerts.AlertsFragment;
import com.pilasvacias.yaba.screens.lines.LinesFragment;
import com.pilasvacias.yaba.screens.search.NfcScanActivity;
import com.pilasvacias.yaba.screens.search.SearchActivity;
import com.pilasvacias.yaba.screens.settings.SettingsActivity;

/**
 * Created by Ogirdor Nazi on 14/10/13.
 */
public class MainActivity extends WDrawerLayoutActivityNative {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(R.layout.main_activity, savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected boolean handleOptionItemsSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.action_nfc_scan:
                startActivity(new Intent(this, NfcScanActivity.class));
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void handleDrawerItemClicked(int position) { }

    @Override
    protected ViewBuilder configureViews() {
        return new ViewBuilder()
                .setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout))
                .setDrawerList((ListView) findViewById(R.id.drawer_listView))
                .setViewPager((DrawerViewPager) findViewById(R.id.main_viewPager));
    }

    @Override
    protected DataBuilder configureDrawerLayout() {
        String[] titles = getResources().getStringArray(R.array.drawer_sections);
        return new DataBuilder()
                .setAppTitle(getString(R.string.app_name))
                .setDefaultSelectedTab(Tab.FAVORITES.ordinal())
                .setDrawerOpenedTextResource(R.string.drawer_open)
                .setDrawerClosedTextResource(R.string.drawer_close)
                .setDrawerItemResource(R.layout.drawer_list_item)
                .setDrawerSectionsArray(titles)
                .setDrawerShadowResource(R.drawable.drawer_shadow)
                .setDrawerToggleIconResource(R.drawable.ic_drawer)
                .setPagerAdapter(createPagerAdapter(titles));
    }

    private WPagerAdapterNative createPagerAdapter(String[] titles) {
        return WPagerAdapterNative.with(getFragmentManager())
                .setOffscreenLimit(WPagerAdapterNative.ALL_FRAGMENTS)
                .setFragments(
                        DummyFragment.newInstance(titles[0]),
                        new LinesFragment(),
                        new AlertsFragment(),
                        DummyFragment.newInstance(titles[3]),
                        DummyFragment.newInstance(titles[4])
                );
    }

    private enum Tab {FAVORITES, LINES, ALERTS, MAP, TICKET}
}
