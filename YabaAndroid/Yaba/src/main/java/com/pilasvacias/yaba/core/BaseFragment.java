package com.pilasvacias.yaba.core;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.pilasvacias.yaba.core.network.NetworkActivity;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
public class BaseFragment extends Fragment {

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().getActivityGraph().inject(this);
    }

    public NetworkActivity getBaseActivity() {
        return (NetworkActivity) getActivity();
    }
}
