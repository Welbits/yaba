package com.pilasvacias.yaba.screens;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.adapter.WBaseAdapter;
import com.pilasvacias.yaba.core.adapter.pager.WPagerAdapter;
import com.pilasvacias.yaba.core.experimental.MagicTurn;
import com.pilasvacias.yaba.core.experimental.Save;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.screens.lines.LinesFragment;

import java.util.Arrays;
import java.util.Random;

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
    private String[] titles;
    private String title;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayoutAdapter drawerLayoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Views.inject(this);
        registerBus();
        titles = getResources().getStringArray(R.array.drawer_sections);
        configureViewPager();
        configureDrawerLayout();

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    private void configureViewPager() {
        WPagerAdapter.with(getSupportFragmentManager())
                .setFragments(
                        DummyFragment.newInstance(titles[0]),
                        new LinesFragment(),
                        DummyFragment.newInstance(titles[2]),
                        DummyFragment.newInstance(titles[3])
                )
                .setTitles(getResources().getStringArray(R.array.lines_tab_titles))
                .setOffscreenLimit(WPagerAdapter.ALL_FRAGMENTS)
                .into(viewPager);
    }

    private void configureDrawerLayout() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        drawerLayoutAdapter = new DrawerLayoutAdapter(this, R.layout.drawer_list_item);
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
                        supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                        ActionBar actionBar = getSupportActionBar();
                        actionBar.setTitle(title);
                    }

                    public void onDrawerOpened(View drawerView) {
                        supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                        getSupportActionBar().setTitle(R.string.app_name);
                    }
                };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    public void selectItem(int position) {
        supportInvalidateOptionsMenu();
        viewPager.setCurrentItem(position, false);
        drawerList.setItemChecked(position, true);
        setTitle(titles[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        if (drawerOpen) {
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
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title.toString();
        getSupportActionBar().setTitle(title);
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
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
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

    public static class DummyFragment extends Fragment {
        // Constants
        private static final String TITLE_KEY = "title";
        // Inject views
        @InjectView(R.id.dummy_textView)
        TextView textView;
        // Fields
        private String title;

        public DummyFragment() {
            super();
        }

        public static DummyFragment newInstance(String title) {
            DummyFragment dummyFragment = new DummyFragment();
            Bundle args = new Bundle();
            args.putString(TITLE_KEY, title);
            dummyFragment.setArguments(args);
            return dummyFragment;
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
            super.onCreateView(inflater, container, savedInstanceState);
            View rootView = inflater.inflate(R.layout.fragment_dummy, container, false);
            Views.inject(this, rootView);
            title = getArguments().getString(TITLE_KEY);
            textView.setText(title);
            textView.setBackgroundColor(randomColor());
            return rootView;
        }
    }
}
