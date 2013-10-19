package com.pilasvacias.yaba.screens.nocturnos;

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
import com.pilasvacias.yaba.screens.lines.Line;
import com.pilasvacias.yaba.screens.lines.LinesAdapter;
import com.pilasvacias.yaba.util.Date;
import com.pilasvacias.yaba.util.L;
import com.pilasvacias.yaba.util.Time;
import com.pilasvacias.yaba.util.ToastUtils;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by IzanRodrigo on 16/10/13.
 */
public class NocturnosFragment extends BaseFragment {

    // Constants
    private static final String ITEMS_KEY = "items";
    // Inject views
    @InjectView(R.id.simple_list_listView)
    ListView listView;
    // Fields
    private LinesAdapter adapter;
    private ActionMode actionMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_simple_list, container, false);
        Views.inject(this, rootView);

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
                    getBaseActivity().startSupportActionMode(new ItemModeCallback(adapter.getItem(position)));
                }
                return true;
            }
        });
        listView.setAdapter(adapter);

        return rootView;
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
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
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
        getBaseActivity().getRequestManager()
                .beginRequest(Line.class)
                .body(new GetListLines())
                .success(new EmtSuccessHandler<Line>() {
                    @Override
                    public void onSuccess(final EmtData<Line> result) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        L.og.d("result =>\n %s", gson.toJson(result));
                        for (Line line : result.getPayload()) {
                            if (line.Label.startsWith("N")) {
                                adapter.add(line);
                            }
                        }
                    }
                })
                .verbose(true)
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
                    MenuItemCompat.getActionProvider(shareItem);
            mShareActionProvider.setShareIntent(getShareIntent(item));

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            NocturnosFragment.this.actionMode = actionMode;
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            NocturnosFragment.this.actionMode = null;
        }
    }

    private static Intent getShareIntent(Line item) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, item.toString());
        return intent;
    }
}
