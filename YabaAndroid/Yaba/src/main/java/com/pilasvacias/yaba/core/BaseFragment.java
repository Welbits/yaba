package com.pilasvacias.yaba.core;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
public class BaseFragment extends Fragment {

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().getActivityGraph().inject(this);
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
