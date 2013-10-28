package com.pilasvacias.yaba.screens;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.adapter.WBaseAdapter;
import com.pilasvacias.yaba.core.adapter.pager.WPagerAdapter;
import com.pilasvacias.yaba.core.debug.DummyFragment;
import com.pilasvacias.yaba.core.experimental.MagicTurn;
import com.pilasvacias.yaba.core.experimental.Save;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.modules.emt.persistence.EmtUpdateService;
import com.pilasvacias.yaba.screens.alerts.AlertsFragment;
import com.pilasvacias.yaba.screens.lines.LinesFragment;
import com.pilasvacias.yaba.screens.search.NfcScanActivity;
import com.pilasvacias.yaba.screens.search.SearchActivity;
import com.pilasvacias.yaba.screens.settings.SettingsActivity;

import java.util.Arrays;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Ogirdor Nazi on 14/10/13.
 */
public class MainActivity extends NetworkActivity {
    // Inject views
    @InjectView(R.id.main_viewPager)
    ViewPager viewPager;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.drawer_listView)
    ListView drawerList;
    // Fields
    @Save
    private String[] titles;
    @Save
    private String title;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Views.inject(this);
        registerBus();
        MagicTurn.restore(this, savedInstanceState);

        if (savedInstanceState == null) {
            titles = getResources().getStringArray(R.array.drawer_sections);
        }

        configureViewPager();
        configureDrawerLayout();

        if (savedInstanceState == null) {
            selectItem(Tab.FAVORITES.ordinal());
        }
        setTitle(title);

        Intent intent = new Intent(this, EmtUpdateService.class);
        intent.setAction(EmtUpdateService.ACTION_UPDATE);
        startService(intent);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MagicTurn.save(this, outState);
    }

    private void configureViewPager() {
        WPagerAdapter.with(getFragmentManager())
                .setFragments(
                        DummyFragment.newInstance(titles[0]),
                        new LinesFragment(),
                        new AlertsFragment(),
                        DummyFragment.newInstance(titles[3]),
                        DummyFragment.newInstance(titles[4])
                )
                .setTitles(titles)
                .setOffscreenLimit(WPagerAdapter.ALL_FRAGMENTS)
                .into(viewPager);
    }

    private void configureDrawerLayout() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        DrawerLayoutAdapter drawerLayoutAdapter = new DrawerLayoutAdapter(this, R.layout.drawer_list_item);
        drawerLayoutAdapter.addAll(Arrays.asList(titles));

        drawerList.setAdapter(drawerLayoutAdapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        drawerList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        drawerToggle = new

                ActionBarDrawerToggle(this,
                        drawerLayout,
                        R.drawable.ic_drawer,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close) {
                    public void onDrawerClosed(View view) {
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                        setTitle(title);
                    }

                    public void onDrawerOpened(View drawerView) {
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                        getActionBar().setTitle(getString(R.string.app_name));
                    }
                };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    public void selectItem(int position) {
        invalidateOptionsMenu();
        viewPager.setCurrentItem(position, false);
        drawerList.setItemChecked(position, true);
        setTitle(titles[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    // Called whenever we call invalidateOptionsMenu()
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        if (drawerOpen) {
            getActionBar().setTitle(getString(R.string.app_name));
            menu.clear();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title.toString();
        getActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private enum Tab {
        FAVORITES, LINES, MAP, TICKET
    }

    static final class DrawerLayoutAdapter extends WBaseAdapter<String, DrawerLayoutAdapter.ViewHolder> {
        public DrawerLayoutAdapter(Context context, int layoutResource) {
            super(context, layoutResource);
        }

        @Override
        protected void changeView(String item, ViewHolder viewHolder) {
            viewHolder.textView.setText(item);
        }

        @Override
        protected ViewHolder createViewHolder(View view) {
            return new ViewHolder(view);
        }

        static final class ViewHolder {
            @InjectView(R.id.drawer_listItem_textView)
            TextView textView;

            public ViewHolder(View view) {
                Views.inject(this, view);
            }
        }
    }
}
