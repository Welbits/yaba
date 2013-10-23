package com.pilasvacias.yaba.modules.network.builder;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.pilasvacias.yaba.modules.network.handlers.ErrorHandler;
import com.pilasvacias.yaba.modules.network.handlers.LoadingHandler;
import com.pilasvacias.yaba.modules.network.handlers.SuccessHandler;

/**
 * Created by Pablo Orgaz - 10/22/13 - pabloogc@gmail.com - https://github.com/pabloogc
 * <p/>
 * Subclass this builder like this<p/>
 * <p/>
 * {@code
 * <p/>
 * public class MyBuilder extends AbstractRequestBuilder<MyBuilder>{
 * <p/>
 * ...
 * <p/>
 * }
 */
@SuppressWarnings("unchecked")
public abstract class AbstractRequestBuilder<BUILDER_TYPE, SUCCESS_TYPE> {

    //Cache
    protected boolean cacheSkip = false;
    protected boolean cacheResult = true;
    protected long fakeTime = 0L;
    protected long expireTime = 0L;
    protected long refreshTime = 0L;

    //Handlers
    protected ErrorHandler errorHandler;
    protected LoadingHandler loadingHandler;
    protected SuccessHandler<SUCCESS_TYPE> successHandler;

    //Meta
    protected RequestQueue requestQueue;
    protected Context context;
    protected Object tag;

    //User part
    protected boolean verbose = false;
    protected boolean ignoreLoading = false;
    protected boolean ignoreErrors = false;
    protected String loadingMessage;

    public AbstractRequestBuilder(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }


    public BUILDER_TYPE cacheSkip(boolean use) {
        this.cacheSkip = use;
        return (BUILDER_TYPE) this;
    }


    public BUILDER_TYPE cacheResult(boolean cacheResult) {
        this.cacheResult = cacheResult;
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE success(SuccessHandler<SUCCESS_TYPE> successHandler) {
        this.successHandler = successHandler;
        return (BUILDER_TYPE) this;
    }

    /**
     * A fake waiting time simulating a long parse or long request. Useful
     * for debuggin configuration changes while loading happens. This action
     * is disabled in production.
     *
     * @param fakeTime
     * @return
     */
    public BUILDER_TYPE fakeTime(long fakeTime) {
        this.fakeTime = fakeTime;
        return (BUILDER_TYPE) this;
    }

    /**
     * Cache time with no  auto refresh.
     *
     * @param cacheTime time to cache.
     * @return
     */
    public BUILDER_TYPE cacheTime(long cacheTime) {
        return cacheTime(cacheTime, cacheTime);
    }

    /**
     * Cache time with refresh. If refresh time has passed the cache will be updated
     * in background and the old cache value will be returned.
     *
     * @param refreshTime
     * @param expireTime
     * @return
     */
    public BUILDER_TYPE cacheTime(long refreshTime, long expireTime) {
        this.refreshTime = refreshTime;
        this.expireTime = expireTime;
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE loading(LoadingHandler loadingHandler) {
        this.loadingHandler = loadingHandler;
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE ignoreLoading(boolean ignore) {
        this.ignoreLoading = ignore;
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE ignoreErrors(boolean ignore) {
        this.ignoreErrors = ignore;
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE tag(Object tag) {
        this.tag = tag;
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE verbose(boolean verbose) {
        this.verbose = verbose;
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE context(Context context) {
        this.context = context;
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE error(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE loadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
        return (BUILDER_TYPE) this;
    }

    public abstract void execute();

}
