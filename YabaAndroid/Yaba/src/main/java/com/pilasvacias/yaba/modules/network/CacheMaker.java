package com.pilasvacias.yaba.modules.network;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;

/**
 * Created by pablo on 10/12/13.
 * welvi-android
 */
public abstract class CacheMaker {

    public static Cache.Entry generateCache(NetworkResponse response, long cacheRefreshTime, long cacheExpireTime) {
        final long now = System.currentTimeMillis();

        //When softExpire time is hit it will give the cached data but perform the request
        final long softExpire = now + cacheRefreshTime;
        //When ttl time is hit it will force the request
        final long ttl = now + cacheExpireTime;

        if (cacheRefreshTime > cacheExpireTime)
            throw new IllegalArgumentException("cacheRefreshTime must be smaller than cacheExpireTime");

        Cache.Entry entry = new Cache.Entry();


        entry.data = response.data;
        entry.etag = response.headers.get("ETag");
        entry.softTtl = softExpire;
        entry.ttl = ttl;
        entry.serverDate = now;
        entry.responseHeaders = response.headers;

        return entry;
    }


}
