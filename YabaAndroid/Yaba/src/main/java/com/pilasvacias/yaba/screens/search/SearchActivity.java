package com.pilasvacias.yaba.screens.search;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.core.widget.EmptyView;
import com.pilasvacias.yaba.modules.emt.handlers.EmtSuccessHandler;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.emt.pojos.Node;
import com.pilasvacias.yaba.util.L;
import com.pilasvacias.yaba.util.Time;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by IzanRodrigo on 25/10/13.
 */
public class SearchActivity extends NetworkActivity implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    // Constants
    private static final long SEARCH_DELAY = Time.millis(2000);
    // Inject views
    @InjectView(R.id.search_listView)
    ListView listView;
    // Fields
    private String query = "";
    private Long elapsedTime = 0L;
    private SearchView searchView;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            search(query);
        }
    };
    private ArrayAdapter<Node> arrayAdapter;
    private EmtData<Node> nodes;

    public static long getSearchDelay() {
        //TODO: Tweak this value to avoid wasting bandwidth
        return SEARCH_DELAY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        Views.inject(this);

        configureListView();

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        handleIntent(intent);
    }

    private void configureListView() {
        EmptyView.makeText(R.string.empty_search).into(listView);
        arrayAdapter = new ArrayAdapter<Node>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Because this activity has set launchMode="singleTop", the system calls this method
        // to deliver the intent if this activity is currently the foreground activity when
        // invoked again (when the user executes a search from this activity, we don't create
        // a new instance of this activity, so the system delivers the search intent here)
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            query = intent.getStringExtra(SearchManager.QUERY);
            runnable.run();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        // Configure SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchMenuItem.expandActionView();
        searchMenuItem.setOnActionExpandListener(this);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchableInfo);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);

        return true;
    }

    private void search(String query) {
        searchView.setQuery(query, false);

        Node.GetNodesLines body = new Node.GetNodesLines();
        body.setNodes(query.split("\\s+"), false);
        L.og.d(body.getNodesAsString());

        getRequestManager().cancelAllRequests();
        getRequestManager().beginRequest(Node.class)
                .body(body)
                .success(new EmtSuccessHandler<Node>() {
                    @Override public void onSuccess(EmtData<Node> result) {
                        nodes = result;
                        arrayAdapter.clear();
                        arrayAdapter.addAll(result.getPayload());
                    }
                })
                .ignoreErrors(true)
                .ignoreLoading(true)
                .cacheResult(false)
                .cacheSkip(true)
                .execute();
    }

    /**
     * Handle search when user click search button.
     *
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        this.query = query;
        runnable.run();
        return true;
    }

    /**
     * Handle search while user enters text.
     * Delay search {@code SEARCH_DELAY} millis.
     * If user change search query after delay pass,
     * previous search is cancelled, and another is delayed.
     *
     * @param newText
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            arrayAdapter.clear();
            return false;
        }
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    /**
     * Finish SearchActivity when close SearchView
     *
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        finish();
        return false;
    }
}
