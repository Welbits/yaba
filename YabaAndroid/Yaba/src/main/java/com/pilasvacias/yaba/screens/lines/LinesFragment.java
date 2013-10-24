package com.pilasvacias.yaba.screens.lines;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.android.volley.VolleyLog;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.adapter.WArrayAdapter;
import com.pilasvacias.yaba.core.network.NetworkFragment;
import com.pilasvacias.yaba.core.widget.EmptyView;
import com.pilasvacias.yaba.modules.emt.handlers.EmtSuccessHandler;
import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.util.Date;
import com.pilasvacias.yaba.util.L;
import com.pilasvacias.yaba.util.Time;
import com.pilasvacias.yaba.util.ToastUtils;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by IzanRodrigo on 16/10/13.
 */
public class LinesFragment extends NetworkFragment {

    // Constants
    private static final String ITEMS_KEY = "items";
    // Inject views
    @InjectView(R.id.simple_list_listView)
    ListView listView;
    // Fields
    private LinesAdapter adapter;
    private ActionMode actionMode;

    private static Intent getShareIntent(Line item) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, item.toString());
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        VolleyLog.DEBUG = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_simple_list, container, false);
        Views.inject(this, rootView);
        configureListView();
        configureDropdown();
        return rootView;
    }

    private void configureDropdown() {
        ActionBar actionBar = getBaseActivity().getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        final DropdownAdapter dropdownAdapter = new DropdownAdapter(getBaseActivity(),
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < 5; i++) {
            dropdownAdapter.add("Dummy " + i);
        }
        actionBar.setListNavigationCallbacks(dropdownAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int i, long l) {
                String item = dropdownAdapter.getItem(i);
                ToastUtils.showShort(getBaseActivity(), item);
                return false;
            }
        });
    }

    private void configureListView() {
        adapter = new LinesAdapter(getBaseActivity(), R.layout.list_item_line);

        listView.setEmptyView(EmptyView.makeText(listView, R.string.empty_list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (actionMode == null) {
                    Line item = adapter.getItem(position);
                    ToastUtils.showShort(getBaseActivity(), item);
                } else {
                    actionMode.finish();
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (actionMode == null) {
                    getBaseActivity().startActionMode(new ItemModeCallback(adapter.getItem(position)));
                }
                return true;
            }
        });
        listView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerBus();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadLines();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.lines, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

    public void loadLines() {
        getRequestManager()
                .beginRequest(Line.class)
                .body(new GetListLines())
                .success(new EmtSuccessHandler<Line>() {
                    @Override
                    public void onSuccess(final EmtData<Line> result) {
                        L.og.d("result => %s", 1);
                        for (Line line : result.getPayload()) {
                            if (!line.Label.startsWith("N")) {
                                adapter.add(line);
                            }
                        }
                    }
                })
                .cacheSkip(true)
                .cacheTime(Time.days(1D))
                .execute();
    }

    public static class GetListLines extends EmtBody {
        String SelectDate = Date.getToday();
        String Lines = ""; //Todas las líneas
    }

    private class ItemModeCallback implements ActionMode.Callback {

        private ShareActionProvider mShareActionProvider;
        private Line item;

        public ItemModeCallback(Line item) {
            this.item = item;
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater menuInflater = actionMode.getMenuInflater();
            int menuResource = R.menu.cab_lines;
            menuInflater.inflate(menuResource, menu);

            MenuItem shareItem = menu.findItem(R.id.action_share);
            mShareActionProvider = (ShareActionProvider)
                    shareItem.getActionProvider();
            mShareActionProvider.setShareIntent(getShareIntent(item));

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            LinesFragment.this.actionMode = actionMode;
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            LinesFragment.this.actionMode = null;
        }
    }

    public class DropdownAdapter extends WArrayAdapter<String, DropdownAdapter.ViewHolder> {

        public DropdownAdapter(Context context, int layoutResource) {
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

        public class ViewHolder {
            // Inject views
            @InjectView(android.R.id.text1)
            public TextView textView;

            ViewHolder(View view) {
                Views.inject(this, view);
            }
        }
    }
}
