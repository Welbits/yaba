package com.pilasvacias.yaba.modules.emt.handlers;

import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.network.handlers.SuccessHandler;

/**
 * Created by pablo on 10/16/13.
 */
public abstract class EmtSuccessHandler<T> extends SuccessHandler<EmtData<T>> {

    @Override public abstract void onSuccess(EmtData<T> result);

}
