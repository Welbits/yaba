package com.pilasvacias.yaba.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import dagger.Lazy;
import timber.log.Timber;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
public class BaseFragment extends Fragment {

    @Inject Lazy<Timber> timberLazy;

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().getActivityGraph().inject(this);
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    Timber getTimer() {
        return timberLazy.get();
    }
}
