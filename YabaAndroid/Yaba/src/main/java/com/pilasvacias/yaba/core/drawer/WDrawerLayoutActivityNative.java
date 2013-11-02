package com.pilasvacias.yaba.core.drawer;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pilasvacias.yaba.core.adapter.pager.WPagerAdapterNative;
import com.pilasvacias.yaba.core.network.NetworkActivity;

import static android.view.ViewGroup.LayoutParams;

/**
 * Created by Izan Rodrigo on 30/10/13.
 */
public abstract class WDrawerLayoutActivityNative extends NetworkActivity {

    // Constants
    private static final int EMPTY = 0;
    private static final String DRAWER_SECTIONS_KEY = "drawer_sections";
    private static final String TITLE_KEY = "title";
    // Internal
    private ActionBarDrawerToggle drawerToggle;
    private String title;
    // ViewBuilder
    private DrawerViewPager viewPager;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    // DataBuilder
    private String appTitle;
    private String[] drawerSectionsArray;
    private int drawerSectionsArrayResource;
    private int defaultSelectedTab;
    private int drawerItemResource;
    private Drawable drawerShadowDrawable;
    private int drawerShadowResource;
    private int drawerToggleIconResource;
    private int drawerOpenedTextResource;
    private int drawerClosedTextResource;
    private WPagerAdapterNative pagerAdapter;
    private PagerCallback pagerCallback;

    /**
     * Call to super.onCreate and setContentView with layoutResID.
     *
     * @param layoutResID        Content view layout resource.
     * @param savedInstanceState
     */
    protected void onCreate(int layoutResID, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);
        restoreInstanceState(savedInstanceState);

        setUp(configureViews(), configureDrawerLayout());

        if (savedInstanceState == null) {
            if (drawerSectionsArray == null) {
                drawerSectionsArray = getResources().getStringArray(drawerSectionsArrayResource);
            }
        }

        createDrawerLayout();

        if (savedInstanceState == null) {
            selectItem(defaultSelectedTab);
        }
        setTitle(title);
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            drawerSectionsArray = savedInstanceState.getStringArray(DRAWER_SECTIONS_KEY);
            title = savedInstanceState.getString(TITLE_KEY);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (drawerSectionsArray != null) {
            outState.putStringArray(DRAWER_SECTIONS_KEY, drawerSectionsArray);
        }
        if (title != null) {
            outState.putString(TITLE_KEY, title);
        }
    }

    /**
     * Get fields from builders.
     * Also creates ViewPager programmatically if user
     * not include in the activity layout.
     *
     * @param viewBuilder Builder with views.
     * @param dataBuilder Builder with data.
     */
    private void setUp(ViewBuilder viewBuilder, DataBuilder dataBuilder) {
        // ViewBuilder
        this.viewPager = viewBuilder.viewPager;
        this.drawerLayout = viewBuilder.drawerLayout;
        this.drawerList = viewBuilder.drawerList;

        // If user not include view pager in activity layout
        if (viewPager == null) {
            viewPager = new DrawerViewPager(this);
            viewPager.setId(0x1000); // ViewPager needs an id
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            viewPager.setLayoutParams(params);
            drawerLayout.addView(viewPager, 0);
        }

        // DataBuilder
        this.pagerAdapter = dataBuilder.pagerAdapter;
        this.appTitle = dataBuilder.appTitle;
        this.drawerSectionsArray = dataBuilder.drawerSectionsArray;
        this.drawerSectionsArrayResource = dataBuilder.drawerSectionsArrayResource;
        this.defaultSelectedTab = dataBuilder.defaultSelectedTab;
        this.drawerShadowResource = dataBuilder.drawerShadowResource;
        this.drawerShadowDrawable = dataBuilder.drawerShadowDrawable;
        this.drawerItemResource = dataBuilder.drawerItemResource;
        this.drawerToggleIconResource = dataBuilder.drawerToggleIconResource;
        this.drawerOpenedTextResource = dataBuilder.drawerOpenedTextResource;
        this.drawerClosedTextResource = dataBuilder.drawerClosedTextResource;
        this.pagerCallback = dataBuilder.pagerCallback;
    }

    /**
     * Create a DrawerLayout with the data read from builders.
     */
    private void createDrawerLayout() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (drawerShadowDrawable == null) {
            drawerShadowDrawable = getResources().getDrawable(drawerShadowResource);
        }
        drawerLayout.setDrawerShadow(drawerShadowDrawable, GravityCompat.START);

        if (pagerCallback != null) {
            pagerCallback.configureViewPager(viewPager);
        } else {
            pagerAdapter.into(viewPager);
        }

        drawerList.setAdapter(new ArrayAdapter<String>(this, drawerItemResource, drawerSectionsArray));
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
                        drawerToggleIconResource,
                        drawerOpenedTextResource,
                        drawerClosedTextResource) {

                    public void onDrawerClosed(View view) {
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                        setTitle(title);
                    }

                    public void onDrawerOpened(View drawerView) {
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                        getActionBar().setTitle(appTitle);
                    }
                };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    /**
     * Change current ViewPager item, and closes the DrawerLayout.
     *
     * @param position
     */
    public void selectItem(int position) {
        invalidateOptionsMenu();
        viewPager.setCurrentItem(position, false);
        drawerList.setItemChecked(position, true);
        setTitle(drawerSectionsArray[position]);
        drawerLayout.closeDrawer(drawerList);
        handleDrawerItemClicked(position);
    }

    // Called whenever we call invalidateOptionsMenu()
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        if (drawerOpen) {
            getActionBar().setTitle(appTitle);
            menu.clear();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return handleOptionItemsSelected(item);
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

    //////////////////////
    // Abstract methods //
    //////////////////////

    /**
     * Handle option item selected (if user use options menu).
     *
     * @param item Selected item.
     * @return
     */
    protected abstract boolean handleOptionItemsSelected(MenuItem item);

    /**
     * Handle drawer item clicked.
     *
     * @param position Drawer item clicked position.
     * @return
     */
    protected abstract void handleDrawerItemClicked(int position);

    /**
     * @return ViewBuilder that stores views needed to create DrawerLayout.
     */
    protected abstract ViewBuilder configureViews();

    /**
     * @return DataBuilder that stores data needed to create DrawerLayout.
     */
    protected abstract DataBuilder configureDrawerLayout();

    //////////////////////
    // Internal classes //
    //////////////////////

    /**
     * Callback to configure view pager.
     */
    public interface PagerCallback {
        void configureViewPager(DrawerViewPager viewPager);
    }

    /**
     * Builder that stores views needed to create DrawerLayout.
     */
    public static class ViewBuilder {
        private DrawerViewPager viewPager;
        private DrawerLayout drawerLayout;
        private ListView drawerList;

        public ViewBuilder setViewPager(DrawerViewPager viewPager) {
            this.viewPager = viewPager;
            return this;
        }

        public ViewBuilder setDrawerLayout(DrawerLayout drawerLayout) {
            this.drawerLayout = drawerLayout;
            return this;
        }

        public ViewBuilder setDrawerList(ListView drawerList) {
            this.drawerList = drawerList;
            return this;
        }
    }

    /**
     * Builder that stores data needed to create DrawerLayout.
     */
    public static class DataBuilder {
        private String appTitle;
        private String[] drawerSectionsArray;
        private int drawerSectionsArrayResource;
        private int defaultSelectedTab;
        private int drawerItemResource;
        private Drawable drawerShadowDrawable;
        private int drawerShadowResource;
        private int drawerToggleIconResource;
        private int drawerOpenedTextResource;
        private int drawerClosedTextResource;
        private WPagerAdapterNative pagerAdapter;
        private PagerCallback pagerCallback;

        public DataBuilder setAppTitle(String appTitle) {
            this.appTitle = appTitle;
            return this;
        }

        public DataBuilder setDrawerSectionsArray(String[] drawerSectionsArray) {
            this.drawerSectionsArray = drawerSectionsArray;
            return this;
        }

        public DataBuilder setDrawerSectionsArrayResource(int drawerSectionsArrayResource) {
            this.drawerSectionsArrayResource = drawerSectionsArrayResource;
            return this;
        }

        public DataBuilder setDefaultSelectedTab(int defaultSelectedTab) {
            this.defaultSelectedTab = defaultSelectedTab;
            return this;
        }

        public DataBuilder setDrawerShadowDrawable(Drawable drawerShadowDrawable) {
            this.drawerShadowDrawable = drawerShadowDrawable;
            return this;
        }

        public DataBuilder setDrawerShadowResource(int drawerShadowResource) {
            this.drawerShadowResource = drawerShadowResource;
            return this;
        }

        public DataBuilder setDrawerItemResource(int drawerItemResource) {
            this.drawerItemResource = drawerItemResource;
            return this;
        }

        public DataBuilder setDrawerToggleIconResource(int drawerToggleIconResource) {
            this.drawerToggleIconResource = drawerToggleIconResource;
            return this;
        }

        public DataBuilder setDrawerOpenedTextResource(int drawerOpenedTextResource) {
            this.drawerOpenedTextResource = drawerOpenedTextResource;
            return this;
        }

        public DataBuilder setDrawerClosedTextResource(int drawerClosedTextResource) {
            this.drawerClosedTextResource = drawerClosedTextResource;
            return this;
        }

        public DataBuilder setPagerAdapter(WPagerAdapterNative pagerAdapter) {
            this.pagerAdapter = pagerAdapter;
            return this;
        }

        public DataBuilder setPagerCallback(PagerCallback pagerCallback) {
            this.pagerCallback = pagerCallback;
            return this;
        }
    }
}
