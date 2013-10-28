package com.pilasvacias.yaba.screens.stopinfo;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.modules.emt.pojos.Node;

/**
 * Created by Fede on 24/10/13.
 * pilasVacias
 */
public class StopInfoActivity extends NetworkActivity {


    //
    private Node busStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String busStopClass;
        if (intent.hasExtra("busStop")) {
            busStopClass = getIntent().getStringExtra("busStop");
            Gson gson = new Gson();
            busStop = gson.fromJson(busStopClass, Node.class);
        }

//        setTitle();
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public Node getBusStop() {
        return busStop;
    }

}
