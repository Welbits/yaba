package com.pilasvacias.yaba.modules.playa.builder;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpClientStack;
import com.pilasvacias.yaba.modules.playa.handlers.ErrorHandler;
import com.pilasvacias.yaba.modules.playa.handlers.LoadingHandler;
import com.pilasvacias.yaba.modules.playa.handlers.SuccessHandler;
import com.pilasvacias.yaba.modules.playa.handlers.impl.FakeErrorHandler;
import com.pilasvacias.yaba.modules.playa.handlers.impl.FakeSuccessHandler;
import com.pilasvacias.yaba.modules.playa.models.PlayaRequest;
import com.pilasvacias.yaba.util.L;

import org.apache.http.impl.client.DefaultHttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Pablo Orgaz - 10/22/13 - pabloogc@gmail.com - https://github.com/pabloogc
 * <p/>
 * Subclass this builder like this<p/>
 * <p/>
 * {@code
 * <p/>
 * public class MyBuilder<SUCCESS_HANDLER_TYPE> extends PlayaRequestBuilder<MyBuilder<SUCCESS_HANDLER_TYPE>, SUCCESS_HANDLER_TYPE>{
 * <p/>
 * ...
 * <p/>
 * }
 */
@SuppressWarnings("unchecked")
public abstract class PlayaRequestBuilder
        <
                BUILDER_TYPE extends PlayaRequestBuilder,
                REQUEST_TYPE extends PlayaRequest,
                SUCCESS_DATA_TYPE,
                BODY_TYPE
                > {

    //Request
    protected String url = "";
    protected String baseUrl = "";
    protected int method = Request.Method.GET;
    protected String queryString = "";
    protected String path = "";
    protected HashMap<String, String> queryMap = new HashMap<>();
    protected HashMap<String, String> paramsMap = new HashMap<>();
    protected HashMap<String, String> headers = new HashMap<>();
    protected RetryPolicy retryPolicy = new DefaultRetryPolicy();
    protected BODY_TYPE body;
    protected String bodyContentType;
    //Cache
    protected boolean cacheSkip = false;
    protected boolean cacheResult = true;
    protected long fakeTime = 0L;
    protected long expireTime = 0L;
    protected long refreshTime = 0L;
    protected String cacheKey;
    //Handlers
    protected ErrorHandler errorHandler = new FakeErrorHandler();
    protected SuccessHandler<SUCCESS_DATA_TYPE> successHandler = new FakeSuccessHandler<>();
    protected LoadingHandler loadingHandler;
    //Meta
    protected RequestQueue requestQueue;
    protected Context context;
    protected Object tag;
    //User part
    protected boolean verbose = false;
    protected String loadingMessage;

    public PlayaRequestBuilder(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public BUILDER_TYPE cacheSkip(boolean use) {
        this.cacheSkip = use;
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE url(String url) {
        this.url = url;
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE retryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE query(String key, Object value) {
        queryMap.put(key, String.valueOf(value));
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE header(String key, Object value) {
        headers.put(key, String.valueOf(value));
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE param(String key, Object value) {
        paramsMap.put(key, String.valueOf(value));
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE body(BODY_TYPE body) {
        this.body = body;
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE bodyContentType(String bodyContentType) {
        this.bodyContentType = bodyContentType;
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE path(Object... elements) {
        StringBuilder pathBuilder = new StringBuilder();
        if (!url.endsWith("/"))
            pathBuilder.append("/");
        for (Object e : elements) {
            if (e == null || String.valueOf(e).isEmpty())
                continue;
            pathBuilder.append(String.valueOf(e)).append("/");
        }
        path = pathBuilder.toString();
        return (BUILDER_TYPE) this;
    }

    public BUILDER_TYPE method(int method) {
        this.method = method;
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
    public BUILDER_TYPE success(SuccessHandler<SUCCESS_DATA_TYPE> successHandler) {
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

    public BUILDER_TYPE cacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
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
     * Execute the request
     */
    public REQUEST_TYPE execute() {
        REQUEST_TYPE request = create();
        configureAbstractRequest(request);
        configure(request);
        requestQueue.add(request);

        if (loadingHandler != null)
            loadingHandler.showLoading(loadingMessage);

        return request;
    }

    /**
     * Do not execute this in the UI thread, it will crash.
     * Also normal handlers won't be notified.
     *
     * @return the data, or null if it went wrong.
     */
    public SUCCESS_DATA_TYPE executeSync() {
        REQUEST_TYPE request = create();
        configure(request);
        configureAbstractRequest(request);
        HttpClientStack httpClientStack = new HttpClientStack(new DefaultHttpClient());
        BasicNetwork basicNetwork = new BasicNetwork(httpClientStack);
        try {
            NetworkResponse response = basicNetwork.performRequest(request);
            SUCCESS_DATA_TYPE data = (SUCCESS_DATA_TYPE) request.getParsedData(response);
            if (request.responseIsOk(response, data))
                return data;
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        } catch (VolleyError volleyError) {
            volleyError.printStackTrace();
        }

        return null;
    }

    /**
     * Subclasses of the builder are responsible for creating
     * and executing the request object.
     */
    public abstract void configure(REQUEST_TYPE request);

    /**
     * Subclasses of the builder are responsible for creating
     * and executing the request object.
     */
    public abstract REQUEST_TYPE create();

    private void configureAbstractRequest(REQUEST_TYPE request) {

        errorHandler.setLoadingHandler(loadingHandler);
        successHandler.setLoadingHandler(loadingHandler);

        if (!queryMap.isEmpty()) {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("?");
            Iterator<String> it = queryMap.keySet().iterator();
            try {
                while (it.hasNext()) {
                    String key = it.next();
                    String value = URLEncoder.encode(queryMap.get(key), "utf-8");
                    queryBuilder.append(key).append("=").append(value);
                    if (it.hasNext())
                        queryBuilder.append("&");
                }

                queryString = queryBuilder.toString();
            } catch (UnsupportedEncodingException e) {
                L.og.e("Unable to encode query %s", queryBuilder.toString());
                e.printStackTrace();
            }
        }

        request.setSuccessHandler(successHandler);
        request.setParams(paramsMap.isEmpty() ? null : paramsMap);
        request.setUrl(baseUrl + url + path + queryString);
        request.setMethod(method);
        request.setHeaders(headers);
        request.setTag(tag);
        request.setVerbose(verbose);
        request.setFakeExecutionTime(fakeTime);
        request.setCacheRefreshTime(refreshTime);
        request.setCacheExpireTime(expireTime);
        request.setCacheSkip(cacheSkip);
        request.setShouldCache(cacheResult);
        request.setRetryPolicy(retryPolicy);
        request.setBody(body);
        request.setBodyContentType(bodyContentType);
    }

}
