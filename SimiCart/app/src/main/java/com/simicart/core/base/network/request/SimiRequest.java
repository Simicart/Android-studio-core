package com.simicart.core.base.network.request;

import com.simicart.core.base.delegate.RequestCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.network.response.SimiResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class SimiRequest implements Comparable<SimiRequest> {

    public interface Method {
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
        int HEAD = 4;
        int OPTIONS = 5;
        int TRACE = 6;
        int PATCH = 7;
    }

    public enum Priority {
        LOW, NORMAL, HIGH, IMMEDIATE
    }

    public interface TYPEREQUEST {
        int HTTPCLIENT = 0;
        int URL = 1;
    }

    protected int mTypeMethod = Method.POST;
    protected int mTypeRequest = TYPEREQUEST.URL;
    protected String mUrl;
    protected HashMap<String, Object> mHashMapBody;
    protected HashMap<String, String> mHeader;
    protected RequestCallBack mCallBack;
    protected Integer mSequence;
    protected boolean mCancel;
    protected JSONObject mJsonBody;
    protected SimiRequestQueue mRequestQueue;
    protected Priority mCurrentPriority = Priority.NORMAL;
    protected boolean isShowNotify = true;
    protected boolean mShouldCache = false;
    protected String mCacheKey;
    protected boolean isCloud;
    protected boolean isRedirect;
    protected String mUrlRedirect;

    public SimiRequest(String url, RequestCallBack delegate) {
        mUrl = url;
        mCallBack = delegate;
        mHashMapBody = new HashMap<>();
    }

    public SimiRequest(String url, RequestCallBack delegate, int typeMethod) {
        this(url, delegate);
        mTypeMethod = typeMethod;
    }

    public SimiRequest(String url, RequestCallBack delegate, int typeMethod,
                       int typeRequest) {
        this(url, delegate, typeMethod);
        mTypeRequest = typeRequest;
    }

    public SimiResponse parseNetworkResponse(SimiNetworkResponse response) {

        return null;
    }

    public void deliveryCoreResponse(SimiResponse response) {

    }

    public final SimiRequest setSequence(int sequence) {
        mSequence = sequence;
        return this;
    }

    public void finish() {

    }

    public boolean isCancel() {
        return mCancel;
    }

    public void cancel(boolean mCancel) {
        this.mCancel = mCancel;
    }

    public int getTypeMethod() {
        return mTypeMethod;
    }

    public void setTypeMethod(int mTypeMethod) {
        this.mTypeMethod = mTypeMethod;
    }

    public int getTypeRequest() {
        return mTypeRequest;
    }

    public void setTypeRequest(int mTypeRequest) {
        this.mTypeRequest = mTypeRequest;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public void setCacheKey(String cacheKey) {
        mCacheKey = cacheKey;
    }

    public String getCacheKey() {
        boolean isRefreshCart = SimiManager.getIntance().isRereshCart();
        if (mUrl.contains("get_cart") && isRefreshCart) {
            return null;
        }

        return mCacheKey;
    }

    public String getCacheKeyForSaveResponse() {
        return mCacheKey;
    }

    protected void prepareRequest() {
        try {
            Iterator<Entry<String, Object>> iter = mHashMapBody.entrySet()
                    .iterator();
            while (iter.hasNext()) {
                Entry mEntry = (Entry) iter.next();
                mJsonBody.put((String) mEntry.getKey(), mEntry.getValue());
            }
        } catch (JSONException e) {
            mJsonBody = null;
        }
    }

    public void addBody(String key, String value) {
        if (key != null && value != null) {
            mHashMapBody.put(key, value);
        }
    }

    public void addBody(String key, JSONArray value) {
        if (key != null && value != null) {
            mHashMapBody.put(key, value);
        }
    }

    public void addBody(String key, JSONObject value) {
        if (key != null && value != null) {
            mHashMapBody.put(key, value);
        }
    }

    public Priority getPriority() {
        return mCurrentPriority;
    }

    public void setPriority(Priority priority) {
        mCurrentPriority = priority;
    }


    public void setShouldCache(boolean should_cache) {
        mShouldCache = should_cache;
    }

    public boolean isShouldeCache() {
        return mShouldCache;
    }

    public boolean isShowNotify() {
        return isShowNotify;

    }

    public void setShowNotify(boolean isShowNotify) {
        this.isShowNotify = isShowNotify;
    }

    public void setRequestQueue(SimiRequestQueue requestQueue) {
        mRequestQueue = requestQueue;
    }

    public void onStopRequestQueue() {
        mRequestQueue.stop();
    }

    public void onStartRequestQueue() {

        mRequestQueue.start();
    }

    public JSONObject getBody() {
        if (null == mJsonBody) {
            prepareRequest();
        }

        JSONObject json = new JSONObject();

        try {
            json.put("data", mJsonBody);
        } catch (JSONException e) {

        }

        return json;
    }

    public void setBody(JSONObject jsBody) {
        mJsonBody = jsBody;
    }

    public HashMap<String, String> getHeader() {
        return mHeader;
    }

    public void setHeader(HashMap<String, String> header) {
        mHeader = header;
    }

    public void setCloud(boolean is_cloud) {
        isCloud = is_cloud;
    }

    public boolean isCloud() {
        return isCloud;
    }

    public void setRedirect(boolean is_redirect) {
        isRedirect = is_redirect;
    }

    public boolean isRedirect() {
        return this.isRedirect;
    }

    public void setUrlRedirect(String urlRedirect) {
        mUrlRedirect = urlRedirect;
    }

    public String getUrlRedirect() {
        return this.mUrlRedirect;
    }

    @Override
    public int compareTo(SimiRequest another) {
        Priority left = this.getPriority();
        Priority right = another.getPriority();
        int tmp1 = this.mSequence - another.mSequence;
        int tmp2 = right.ordinal() - left.ordinal();
        int result = left == right ? tmp1 : tmp2;
        return result;
    }

}
