package com.pilasvacias.yaba.common.network;

import com.pilasvacias.yaba.common.BaseActivity;
import com.pilasvacias.yaba.modules.soap.SoapModule;

import java.util.List;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
public class NetworkActivity extends BaseActivity {

    @Override protected List<Object> getModules() {
        List<Object> modules = super.getModules();
        modules.add(new SoapModule());
        return modules;
    }
}
