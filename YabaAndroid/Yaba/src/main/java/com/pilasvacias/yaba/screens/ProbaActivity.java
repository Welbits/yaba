package com.pilasvacias.yaba.screens;

import android.os.Bundle;

import com.android.volley.VolleyLog;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.experimental.MagicTurn;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.modules.sync.util.SyncUtils;

import butterknife.OnClick;
import butterknife.Views;

public class ProbaActivity extends NetworkActivity {

//    EmtDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proba);
        MagicTurn.restore(this, savedInstanceState);
        VolleyLog.DEBUG = true;
        Views.inject(this);

        SyncUtils.createSyncAccount(this);

    }

    @Override protected void onResume() {
        super.onResume();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MagicTurn.save(this, outState);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        //OpenHelperManager.releaseHelper();
        //dbHelper = null;
    }

    @OnClick(R.id.button)
    public void doThings() {

    }
}
