package com.simicart.network.request;

import java.util.Map;

import org.json.JSONObject;

import android.support.v4.util.LruCache;

import com.simicart.network.common.*;

/**
 * @author Frank Simi This class will store the response to LruCache. It will be
 *         Cache Level 1.
 */
public class SimiNetworkCacheL1 implements SimiNetworkCache {

    private LruCache<String, JSONObject> mCacheL1;

    public SimiNetworkCacheL1() {
        // we can store 1024 response at the same time.
        int maxSize = 1024 * 1024;
        mCacheL1 = new LruCache<String, JSONObject>(maxSize);
    }

    @Override
    public JSONObject get(String key) {
        String key_md5 = SimiNetworkCommon.md5(key);
        return mCacheL1.get(key_md5);
    }

    @Override
    public void put(String key, JSONObject response) {
        synchronized (mCacheL1) {
            if (null != key && null != response) {
                String key_md5 = SimiNetworkCommon.md5(key);
                if (mCacheL1.get(key_md5) == null) {
                    mCacheL1.put(key_md5, response);
                }
            }
        }

    }

    @Override
    public void clear() {
        mCacheL1.evictAll();
    }

    @Override
    public void remove(String key) {
        synchronized (mCacheL1) {
            String key_md5 = SimiNetworkCommon.md5(key);
            mCacheL1.remove(key_md5);
        }

    }

    /**
     * This method will return a copy of the current cache
     */
    public Map<String, JSONObject> makeCopy() {
        return mCacheL1.snapshot();
    }

}