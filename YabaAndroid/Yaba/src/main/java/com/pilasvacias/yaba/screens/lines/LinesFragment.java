package com.pilasvacias.yaba.screens.lines;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.BaseFragment;
import com.pilasvacias.yaba.core.widget.EmptyView;
import com.pilasvacias.yaba.modules.emt.handlers.EmtSuccessHandler;
import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.util.L;
import com.pilasvacias.yaba.util.Time;
import com.pilasvacias.yaba.util.ToastUtils;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by IzanRodrigo on 16/10/13.
 */
public class LinesFragment extends BaseFragment {

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_simple_list, container, false);
        Views.inject(this, rootView);

        adapter = new LinesAdapter(getBaseActivity(), R.layout.simple_list_item);

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
                    getBaseActivity().startSupportActionMode(new ItemModeCallback(adapter.getItem(position)));
                }
                return true;
            }
        });
        listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            listView.onRestoreInstanceState(savedInstanceState.getParcelable(ITEMS_KEY));
        } else {
            loadLines();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ITEMS_KEY, listView.onSaveInstanceState());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.favorites, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchViewAction = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchViewAction.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                ToastUtils.showShort(getBaseActivity(), item.getTitle());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadLines() {
        getBaseActivity().getRequestManager()
                .beginRequest(Line.class)
                .body(new GetListLines())
                .success(new EmtSuccessHandler<Line>() {
                    @Override
                    public void onSuccess(final EmtData<Line> result) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        L.og.d("result =>\n %s", gson.toJson(result));
                        adapter.addAll(result.getPayload());
                    }
                })
                .verbose(true)
                .cacheTime(Time.minutes(1.5))
                .execute();
    }

    public static class GetListLines extends EmtBody {
        String SelectDate = "19-8-2013";
        String Lines = "145|90|1";
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
            int menuResource = R.menu.cab_favorites;
            menuInflater.inflate(menuResource, menu);

            MenuItem shareItem = menu.findItem(R.id.action_share);
            mShareActionProvider = (ShareActionProvider)
                    MenuItemCompat.getActionProvider(shareItem);
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
            switch (menuItem.getItemId()) {
                case R.id.action_delete:
                    adapter.remove(item);
                    actionMode.finish();
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            LinesFragment.this.actionMode = null;
        }
    }
}
