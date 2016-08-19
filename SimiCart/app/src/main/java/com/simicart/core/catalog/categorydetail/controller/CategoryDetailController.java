package com.simicart.core.catalog.categorydetail.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.catalog.categorydetail.delegate.CategoryDetailDelegate;
import com.simicart.core.catalog.categorydetail.fragment.CategoryDetailFragment;
import com.simicart.core.catalog.categorydetail.model.CategoryDetailModel;
import com.simicart.core.common.KeyData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by frank on 8/19/16.
 */
public class CategoryDetailController extends SimiController {

    protected HashMap<String, Object> hmData;
    protected CategoryDetailDelegate mDelegate;
    protected String mTypeCate;
    protected int mOffset = 0;
    protected int mLimit = 6;


    @Override
    public void onStart() {
        getTypeCate();

        initListener();

        requestCategoryDetail();

    }

    protected void getTypeCate() {
        if (hmData.containsKey(KeyData.CATEGORY_DETAIL.TYPE)) {
            mTypeCate = (String) hmData.get(KeyData.CATEGORY_DETAIL.TYPE);
            hmData.remove(KeyData.CATEGORY_DETAIL.TYPE);
        }

        if(hmData.containsKey(KeyData.CATEGORY_DETAIL.OFFSET)){
            String offset = (String) hmData.get(KeyData.CATEGORY_DETAIL.OFFSET);
            mOffset = Integer.parseInt(offset);
            hmData.remove(KeyData.CATEGORY_DETAIL.OFFSET);
        }

        if(hmData.containsKey(KeyData.CATEGORY_DETAIL.LIMIT)){
            String limit = (String) hmData.get(KeyData.CATEGORY_DETAIL.LIMIT);
            mLimit = Integer.parseInt(limit);
            hmData.remove(KeyData.CATEGORY_DETAIL.LIMIT);
        }

    }


    protected void initListener() {

    }

    protected void requestCategoryDetail() {
        if (null == mModel) {
            mModel = new CategoryDetailModel(mTypeCate);
            if (mTypeCate.equals(CategoryDetailFragment.CUSTOM)) {
                if (hmData.containsKey(KeyData.CATEGORY_DETAIL.CUSTOM_URL)) {
                    String customUrl = (String) hmData.get(KeyData.CATEGORY_DETAIL.CUSTOM_URL);
                    ((CategoryDetailModel) mModel).setCustomUrl(customUrl);
                    hmData.remove(KeyData.CATEGORY_DETAIL.CUSTOM_URL);
                }
            }
        }

        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.updateView(collection);
            }
        });

        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.updateView(null);
            }
        });

        addBodyForModel();

        mModel.request();

    }


    protected void addBodyForModel() {
        if (null != hmData) {
            Set<String> keys = hmData.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = hmData.get(key);
                if (value instanceof String) {
                    mModel.addBody(key, (String) value);
                } else if (value instanceof JSONObject) {
                    mModel.addBody(key, (JSONObject) value);
                }
            }
        }
        mModel.addBody(KeyData.CATEGORY_DETAIL.OFFSET,String.valueOf(mOffset));
        mModel.addBody(KeyData.CATEGORY_DETAIL.LIMIT,String.valueOf(mLimit));
    }


    @Override
    public void onResume() {

    }

    public void setData(HashMap<String, Object> data) {
        hmData = data;
    }

    public void setDelegate(CategoryDetailDelegate delegate) {
        mDelegate = delegate;
    }

}
