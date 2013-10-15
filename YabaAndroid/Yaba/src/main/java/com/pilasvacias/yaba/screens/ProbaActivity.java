package com.pilasvacias.yaba.screens;

import android.os.Bundle;
import android.webkit.WebView;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.common.network.NetworkActivity;
import com.pilasvacias.yaba.modules.emt.models.EmtResult;
import com.pilasvacias.yaba.modules.network.handlers.SuccessHandler;
import com.pilasvacias.yaba.modules.util.Time;

import butterknife.InjectView;
import butterknife.Views;

public class ProbaActivity extends NetworkActivity {

    @InjectView(R.id.text)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proba);
        Views.inject(this);
        createRequest();
    }

    private void createRequest() {
        requestManager
                .beginRequest(GetGroupsResult.class)
                .body("GetGroups")
                .success(new SuccessHandler<GetGroupsResult>() {
                    @Override public void onSuccess(GetGroupsResult data) {

                    }
                })
                .verbose(true)
                .fakeTime(Time.seconds(5))
                .execute();
    }

    private static class GetGroupsResult extends EmtResult {
        //Cosasss naziss
    }

}
