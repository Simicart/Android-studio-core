package com.simicart.core.base.model;


import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.delegate.RequestCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.network.request.SimiJSONRequest;
import com.simicart.core.base.network.request.SimiRequest;
import com.simicart.core.base.network.request.SimiRequest.Priority;
import com.simicart.core.base.network.response.SimiResponse;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class SimiModel {

    protected ModelSuccessCallBack mModelSuccessCallBack;
    protected ModelFailCallBack mModelFailCallBack;
    protected RequestCallBack mRequestCallBack;
    protected SimiCollection collection = null;
    protected String mUrlAction;
    protected boolean isShowNotify = true;
    protected HashMap<String, Object> mHashMapBody;
    protected SimiRequest mRequest;
    protected Priority mCurrentPriority = Priority.NORMAL;
    protected JSONObject mJSON;
    protected boolean enableCache = false;
    protected boolean isCloud;
    protected JSONObject mJSONBodyEntity;
    protected HashMap<String, String> mHeader;
    protected int mTypeMethod = SimiRequest.Method.POST;


    public SimiModel() {
        mHashMapBody = new HashMap<>();
        mHeader = new HashMap<>();
    }

    public void request() {
        initRequest();
        if (enableCache) {
            String cache_key = createCacheKey();
            mRequest.setCacheKey(cache_key);
            getDataFromCache();
        } else {
            SimiManager.getIntance().getRequestQueue().add(mRequest);
        }
    }

    protected void initRequest() {
        initDelegate();
        setUrlAction();
        setShowNotifi();
        setEnableCache();
        setTypeMethod();
        setCloud();
        mRequest = new SimiJSONRequest(mUrlAction, mRequestCallBack);
        mRequest.setPriority(mCurrentPriority);
        mRequest.setShowNotify(isShowNotify);
        mRequest.setShouldCache(enableCache);
        mRequest.setCloud(isCloud);
        mRequest.setTypeMethod(mTypeMethod);
        if (null != mJSONBodyEntity) {
            if (DataPreferences.isSignInComplete()
                    && (!mUrlAction.equals(Constants.SIGN_IN) || !mUrlAction
                    .equals(Constants.SIGN_OUT))) {
                String email = DataPreferences.getEmail();
                String pass_word = DataPreferences.getPassword();
                try {
                    mJSONBodyEntity.put(Constants.USER_EMAIL, email);
                    mJSONBodyEntity.put(Constants.USER_PASSWORD, pass_word);
                } catch (JSONException e) {

                }

            }
            mRequest.setBody(mJSONBodyEntity);
        } else {
            createBodyEntity();
        }
        mRequest.setHeader(mHeader);
    }

    protected String createCacheKey() {
        String class_name = this.getClass().getName();
        String cache_key = class_name + mUrlAction;
        String post_body = mRequest.getBody().toString();
        if (Utils.validateString(post_body)) {
            cache_key = cache_key + post_body;
        }
        return cache_key;
    }


    protected void initDelegate() {
        mRequestCallBack = new RequestCallBack() {

            @Override
            public void callBack(SimiResponse simiResponse, boolean isSuccess) {
                if (isSuccess) {
                    mJSON = simiResponse.getDataJSON();
                    parseData();
                    if (null != mModelSuccessCallBack) {
                        mModelSuccessCallBack.onSuccess(collection);
                    }
                } else {
                    if (null != mModelFailCallBack) {
                        SimiError error = null;
                        if (null != simiResponse) {
                            error = simiResponse.getSimiError();
                        }
                        mModelFailCallBack.onFail(error);
                    }
                }

            }
        };

    }

    protected void setUrlAction() {
    }

    protected void setShowNotifi() {
    }

    protected void setEnableCache() {

    }

    private void createBodyEntity() {

        if (DataPreferences.isSignInComplete()
                && (!mUrlAction.equals(Constants.SIGN_IN) || !mUrlAction
                .equals(Constants.SIGN_OUT))) {
            String email = DataPreferences.getEmail();
            String pass_word = DataPreferences.getPassword();
            addBody(Constants.USER_EMAIL, email);
            addBody(Constants.USER_PASSWORD, pass_word);

        }

        if (!mHashMapBody.isEmpty()) {
            Iterator<Entry<String, Object>> iterator = mHashMapBody.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                Object value = entry.getValue();
                String key = String.valueOf(entry.getKey());
                if (value instanceof String) {
                    mRequest.addBody(key, String.valueOf(value));
                } else if (value instanceof JSONObject) {
                    mRequest.addBody(key, (JSONObject) value);
                } else if (value instanceof JSONArray) {
                    mRequest.addBody(key, (JSONArray) value);
                }
            }
        }
    }


    public void addBody(String tag, String value) {
        mHashMapBody.put(tag, value);

    }

    public void addBody(String tag, JSONArray value) {
        mHashMapBody.put(tag, value);
    }

    public void addBody(String tag, JSONObject value) {
        mHashMapBody.put(tag, value);
    }

    protected void getDataFromCache() {
        JSONObject json = SimiManager.getIntance().getRequestQueue()
                .getDataFromCacheL1(mRequest);
        if (null != json) {
            SimiResponse simiResponse = new SimiResponse();
            simiResponse.parse(json.toString());
            mRequestCallBack.callBack(simiResponse, true);
        } else {
            SimiManager.getIntance().getRequestQueue().add(mRequest);
        }
    }


    protected void parseData() {
        try {
            JSONArray list = this.mJSON.getJSONArray("data");
            collection = new SimiCollection();
            for (int i = 0; i < list.length(); i++) {
                SimiEntity entity = new SimiEntity();
                entity.setJSONObject(list.getJSONObject(i));
                collection.addEntity(entity);
            }

            if (mJSON.has(Constants.OTHER)) {
                JSONObject jsonOther = mJSON.getJSONObject(Constants.OTHER);
                if (null != jsonOther) {
                    collection.setJSONOther(jsonOther);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    protected void setCloud() {

    }

    protected void setTypeMethod() {

    }


    public JSONObject getDataJSON() {
        return mJSON;
    }


    public void setJSONBodyEntity(JSONObject json) {
        mJSONBodyEntity = json;
    }


    public SimiCollection getCollection() {
        if (collection == null)
            return new SimiCollection();
        return this.collection;
    }

    public void addHeader(String key, String value) {
        if (Utils.validateString(key) && Utils.validateString(value)) {
            mHeader.put(key, value);
        }
    }

    public void setSuccessListener(ModelSuccessCallBack successListener) {
        mModelSuccessCallBack = successListener;
    }

    public void setFailListener(ModelFailCallBack failListener) {
        mModelFailCallBack = failListener;
    }

}
