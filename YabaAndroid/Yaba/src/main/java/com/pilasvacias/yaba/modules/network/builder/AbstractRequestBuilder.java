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
 * public class MyBuilder<SUCCESS_TYPE> extends AbstractRequestBuilder<MyBuilder<SUCCESS_TYPE>, SUCCESS_TYPE>{
 * <p/>
 * ...
 * <p/>
 * }
 */
@SuppressWarnings("unchecked")
public abstract class AbstractRequestBuilder
        <BUILDER_TYPE extends AbstractRequestBuilder<BUILDER_TYPE, REQUEST_TYPE, SUCCESS_TYPE>, REQUEST_TYPE, SUCCESS_TYPE> {

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


    /**
     * @param cacheResult whether this request should be cached or not.
     */
    public BUILDER_TYPE cacheResult(boolean cacheResult) {
        this.cacheResult = cacheResult;
        return (BUILDER_TYPE) this;
    }

    /**
     * @param successHandler the listener of the request
     */
    public BUILDER_TYPE success(SuccessHandler<SUCCESS_TYPE> successHandler) {
        this.successHandler = successHandler;
        return (BUILDER_TYPE) this;
    }

    /**
     * A fake waiting time simulating a long parse or long request. Useful
     * for debuggin configuration changes while loading happens. This action
     * is disabled in production.
     *
     * @param fakeTime time in milliseconds
     */
    public BUILDER_TYPE fakeTime(long fakeTime) {
        this.fakeTime = fakeTime;
        return (BUILDER_TYPE) this;
    }

    /**
     * Cache time with no  auto refresh.
     *
     * @param cacheTime time in millis to cache the result (no soft cache)
     */
    public BUILDER_TYPE cacheTime(long cacheTime) {
        return cacheTime(cacheTime, cacheTime);
    }

    /**
     * Cache time with refresh. If refresh time has passed the cache will be updated
     * in background and the old cache value will be returned.
     *
     * @param refreshTime time in millis when the cache is valid and needs no refresh
     * @param expireTime  time in millis when the cache is invalid and must use network
     */
    public BUILDER_TYPE cacheTime(long refreshTime, long expireTime) {
        this.refreshTime = refreshTime;
        this.expireTime = expireTime;
        return (BUILDER_TYPE) this;
    }

    /**
     * @param loadingHandler The loading handler for this request
     */
    public BUILDER_TYPE loading(LoadingHandler loadingHandler) {
        this.loadingHandler = loadingHandler;
        return (BUILDER_TYPE) this;
    }

    /**
     * @param ignore Whether this request will show loading feedback or not
     */
    public BUILDER_TYPE ignoreLoading(boolean ignore) {
        this.ignoreLoading = ignore;
        return (BUILDER_TYPE) this;
    }

    /**
     * @param ignore Whether this request will use default error handling or ignore them
     */
    public BUILDER_TYPE ignoreErrors(boolean ignore) {
        this.ignoreErrors = ignore;
        return (BUILDER_TYPE) this;
    }

    /**
     * @param tag the tag identifying this request. Used for cancelling.
     */
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

    /**
     * A default error handler for the request is given if not ignoring errors
     * and the error handler is null.
     *
     * @param errorHandler the error handler for this request.
     */
    public BUILDER_TYPE error(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return (BUILDER_TYPE) this;
    }

    /**
     * @param loadingMessage The loading message that should appear while loading.
     */
    public BUILDER_TYPE loadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
        return (BUILDER_TYPE) this;
    }

    /**
     * Execute the request. Subclasses of the builder are responsible for creating
     * and executing the request object.
     */
    public abstract void execute();

    /**
     * Execute the request. Subclasses of the builder are responsible for creating
     * and executing the request object.
     */
    public abstract REQUEST_TYPE create();

}
