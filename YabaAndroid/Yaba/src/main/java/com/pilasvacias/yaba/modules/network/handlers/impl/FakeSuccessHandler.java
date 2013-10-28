package com.pilasvacias.yaba.modules.network.handlers.impl;

import com.pilasvacias.yaba.modules.emt.handlers.EmtSuccessHandler;
import com.pilasvacias.yaba.modules.emt.models.EmtData;

/**
 * Created by Pablo Orgaz - 10/27/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class FakeSuccessHandler<T> extends EmtSuccessHandler<T> {
    @Override public void onSuccess(EmtData<T> result) {
    }
}
