package com.pilasvacias.yaba.screens;

import android.os.Bundle;

import com.android.volley.VolleyLog;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.experimental.MagicTurn;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.core.preferences.JsonPreferences;
import com.pilasvacias.yaba.core.preferences.YabaPreferences;
import com.pilasvacias.yaba.modules.emt.handlers.EmtSuccessHandler;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.emt.pojos.Line;
import com.pilasvacias.yaba.modules.emt.requests.GetListLines;
import com.pilasvacias.yaba.util.Time;
import com.pilasvacias.yaba.util.WToast;

public class ProbaActivity extends NetworkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proba);
        MagicTurn.restore(this, savedInstanceState);

        JsonPreferences.get(this).setPreferenceObject(YabaPreferences.STRING_PREFERENCE, "HOLA");
        String pref = JsonPreferences.get(this).getPreferenceObject(YabaPreferences.STRING_PREFERENCE);

        VolleyLog.DEBUG = true;
    }

    @Override protected void onResume() {
        super.onResume();
        createRequest();
    }

    private void createRequest() {
        getRequestManager().beginRequest(Line.class)
                .body(new GetListLines())
                .success(new EmtSuccessHandler<Line>() {
                    @Override public void onSuccess(EmtData<Line> result) {
                        WToast.showShort(ProbaActivity.this, result.getPayload().get(0));
                    }
                })
                .fakeTime(Time.seconds(10))
                .execute();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MagicTurn.save(this, outState);
    }


}
