package com.simicart.network.request;


import com.simicart.network.callback.RequestCallBack;
import com.simicart.network.common.SimiNetworkCommon;
import com.simicart.network.response.SimiResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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


    protected int mTypeMethod = Method.POST;
    protected JSONObject mJsonPostBody;
    protected String mExtendUrl;
    protected String mBaseUrl;
    protected RequestCallBack mRequestCallBack;
    protected Integer mSequence;
    protected boolean mCancel;
    protected SimiRequestQueue mRequestQueue;
    protected Priority mCurrentPriority = Priority.NORMAL;
    protected boolean isShowNotify = true;
    protected boolean mShouldCache = false;
    protected String mCacheKey;
    protected HashMap<String, Object> mDataBody;
    protected HashMap<String, String> mDataParameter;
    protected HashMap<String, String> mHeader;

    public void setJSONPOSTBody(JSONObject json) {
        mJsonPostBody = json;
    }

    public HashMap<String, String> getHeader() {
        return mHeader;
    }

    public void setHeader(HashMap<String, String> mHeader) {
        this.mHeader = mHeader;
    }


    public HashMap<String, Object> getDataBody() {
        return mDataBody;
    }

    public void setDataBody(HashMap<String, Object> mDataBody) {
        this.mDataBody = mDataBody;
    }

    public HashMap<String, String> getDataParameter() {
        return mDataParameter;
    }

    public void setDataParameter(HashMap<String, String> mDataParameter) {
        this.mDataParameter = mDataParameter;
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
        if (null == mJsonPostBody) {
            mJsonPostBody = prepareRequest();
        }
        return mJsonPostBody;
    }


    public SimiRequest(String url, RequestCallBack callBack) {
        mExtendUrl = url;
        mRequestCallBack = callBack;
    }

    public SimiRequest(String url, RequestCallBack callBack, int typeMethod) {
        this(url, callBack);
        mTypeMethod = typeMethod;
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


    public String getUrl() {


        String url = mBaseUrl + mExtendUrl;

        if (SimiNetworkCommon.isAddParameter) {
            String dataParameter = processDataParameter();
            if (SimiNetworkCommon.validateString(dataParameter)) {
                url = url + "?" + dataParameter;
            }
        }

        return url;
    }


    protected String processDataParameter() {
        if (null != mDataParameter && mDataParameter.size() > 0) {
            Iterator<Map.Entry<String, String>> iterator = mDataParameter.entrySet()
                    .iterator();
            boolean isFirst = true;
            StringBuilder builder = new StringBuilder();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String parameter = getAParameter(entry);
                if (SimiNetworkCommon.validateString(parameter)) {
                    if (isFirst) {
                        isFirst = false;
                        builder.append(parameter);
                    } else {
                        builder.append("&");
                        builder.append(parameter);
                    }
                }
            }
            return builder.toString();
        }

        return "";
    }

    protected String getAParameter(Map.Entry<String, String> entry) {
        String param = "";
        String key = entry.getKey();
        String value = entry.getValue();
        if (SimiNetworkCommon.validateString(key) && SimiNetworkCommon.validateString(value)) {
            param = key + "=" + value;
        }
        return param;
    }

    public void setUrl(String mUrl) {
        this.mExtendUrl = mUrl;
    }

    public void setCacheKey(String cacheKey) {
        mCacheKey = cacheKey;
    }

    public String getCacheKey() {
        boolean isRefreshCart = SimiNetworkCommon.isRefreshCart;


        if (mExtendUrl.contains("get_cart") && isRefreshCart) {
            return null;
        }

        return mCacheKey;
    }

    public String getCacheKeyForSaveResponse() {
        return mCacheKey;
    }

    @SuppressWarnings("rawtypes")
    protected JSONObject prepareRequest() {

        JSONObject json = null;
        try {

            if (mDataBody.size() > 0) {
                json = new JSONObject();
                Iterator<Map.Entry<String, Object>> iter = mDataBody.entrySet()
                        .iterator();
                while (iter.hasNext()) {
                    Map.Entry mEntry = (Map.Entry) iter.next();
                    json.put((String) mEntry.getKey(), mEntry.getValue());
                }
            } else {
                json = null;
            }
        } catch (JSONException e) {
            json = null;
        }
        return json;
    }


    public Priority getPriority() {
        return mCurrentPriority;
    }

    public void setPriority(Priority priority) {
        mCurrentPriority = priority;
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

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public void setBaseUrl(String mBaseUrl) {
        this.mBaseUrl = mBaseUrl;
    }
}
