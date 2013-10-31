package com.pilasvacias.yaba.screens;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.experimental.MagicTurn;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.modules.network.handlers.ErrorHandler;
import com.pilasvacias.yaba.modules.network.handlers.SuccessHandler;
import com.pilasvacias.yaba.modules.network.toolbox.GsonRequestBuilder;
import com.pilasvacias.yaba.modules.network.toolbox.User;
import com.pilasvacias.yaba.util.WToast;

public class ProbaActivity extends NetworkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proba);
        MagicTurn.restore(this, savedInstanceState);

        VolleyLog.DEBUG = true;

        GsonRequestBuilder<User.RestResponse> builder = new GsonRequestBuilder<User.RestResponse>(getRequestQueue());
        builder.url("https://www.welvi.com/welvi/account/login")
                .method(Request.Method.POST)
                .error(new ErrorHandler() {
                    @Override public void handleError(VolleyError error) {
                        WToast.showShort(getActivity(), "error");
                    }
                })
                .success(new SuccessHandler<User.RestResponse>() {
                    @Override public void onSuccess(User.RestResponse result) {
                        WToast.showShort(getActivity(), result);
                    }
                })
                .token(User.RestResponse.class)
                .param("j_username", "pabloogc@gmail.com")
                .param("j_password", "minivac")
                .param("clientType", "ANDROID")
                .param("clientVersion", "2.0.10")
                .param("application", "WELVI")
                .execute();
    }

    @Override protected void onResume() {
        super.onResume();
        createRequest();
    }

    private void createRequest() {
//        getRequestManager().beginRequest(Line.class)
//                .body(new GetListLines())
//                .success(new EmtSuccessHandler<Line>() {
//                    @Override public void onSuccess(EmtData<Line> result) {
//                        WToast.showShort(ProbaActivity.this, result.getPayload().get(0));
//                    }
//                })
//                .fakeTime(Time.seconds(10))
//                .execute();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MagicTurn.save(this, outState);
    }


}
