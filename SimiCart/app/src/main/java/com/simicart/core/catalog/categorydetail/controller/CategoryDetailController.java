package com.simicart.core.catalog.categorydetail.controller;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.catalog.categorydetail.component.SortComponent;
import com.simicart.core.catalog.categorydetail.delegate.CategoryDetailDelegate;
import com.simicart.core.catalog.categorydetail.fragment.CategoryDetailFragment;
import com.simicart.core.catalog.categorydetail.model.CategoryDetailModel;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.Constants;

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
    protected int mLimit = 8;
    protected int mResultNumber = 0;
    protected boolean isOnscroll = true;

    protected View.OnClickListener onChangeViewClick;
    protected View.OnClickListener mSortListener;
    protected RecyclerView.OnScrollListener onListScroll;

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

        mSortListener  = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortComponent sortComponent = new SortComponent(v   );
                sortComponent.showPopup();
            }
        };

        onChangeViewClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDelegate.changeView();
            }
        };

        onListScroll = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 0) {
                    // Scrolling up
                    mDelegate.showBottomMenu(true);
                } else {
                    // Scrolling down
                    mDelegate.showBottomMenu(false);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int count = recyclerView.getChildCount();
                int lastPosition;
                if(mDelegate.getTagView().equals(Constants.TAG_LISTVIEW)) {
                    lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                } else {
                    lastPosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                }
                int threshold = mOffset + mLimit -1;
                if (lastPosition == threshold
                        && mResultNumber > count) {
                    if (isOnscroll) {
                        mOffset += mLimit;
                        isOnscroll = false;
                        mDelegate.showLoadMore(true);
                        requestCategoryDetail();
                    }
                }
            }
        };

    }

    protected void requestCategoryDetail() {
        if(mOffset == 0) {
            mDelegate.showLoading();
        }
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
                mDelegate.dismissLoading();
                mResultNumber = ((CategoryDetailModel)mModel).getResultNumber();
                mDelegate.showLoadMore(false);
                mDelegate.updateView(mModel.getCollection());
                if ((mOffset + mLimit) <= mResultNumber)
                    isOnscroll = true;
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
        mDelegate.updateView(mModel.getCollection());
    }

    public void setData(HashMap<String, Object> data) {
        hmData = data;
    }

    public void setDelegate(CategoryDetailDelegate delegate) {
        mDelegate = delegate;
    }

    public View.OnClickListener getOnChangeViewClick() {
        return onChangeViewClick;
    }

    public RecyclerView.OnScrollListener getOnListScroll() {
        return onListScroll;
    }

    public View.OnClickListener getSortListener(){
        return mSortListener;
    }
}
