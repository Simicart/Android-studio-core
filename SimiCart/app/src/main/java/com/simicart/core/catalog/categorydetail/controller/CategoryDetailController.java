package com.simicart.core.catalog.categorydetail.controller;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.catalog.categorydetail.component.FilterPopup;
import com.simicart.core.catalog.categorydetail.component.SortPopup;
import com.simicart.core.catalog.categorydetail.delegate.CategoryDetailDelegate;
import com.simicart.core.catalog.categorydetail.delegate.FilterCallBack;
import com.simicart.core.catalog.categorydetail.delegate.SortCallBack;
import com.simicart.core.catalog.categorydetail.entity.LayerEntity;
import com.simicart.core.catalog.categorydetail.model.CategoryDetailModel;
import com.simicart.core.catalog.categorydetail.entity.FilterEntity;
import com.simicart.core.catalog.categorydetail.entity.FilterState;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by frank on 8/19/16.
 */
public class CategoryDetailController extends SimiController {

    protected HashMap<String, Object> hmData;
    protected CategoryDetailDelegate mDelegate;
    protected String mTypeCate ;
    protected int mOffset = 0;
    protected int mLimit = 8;
    protected int mResultNumber = 0;
    protected boolean isOnscroll = true;
    protected int mCurrentSort;
    protected LayerEntity mLayerEntity;
    protected JSONObject mJSONFilter;
    protected String tagView;

    protected View.OnClickListener onChangeViewClick;
    protected View.OnClickListener mSortListener;
    protected View.OnClickListener mFilterListener;
    protected RecyclerView.OnScrollListener onListScroll;

    @Override
    public void onStart() {
        getTypeCate();

        initListener();

        mDelegate.showLoading();
        requestCategoryDetail();

    }

    protected void getTypeCate() {
        if (hmData.containsKey(KeyData.CATEGORY_DETAIL.TYPE)) {
            mTypeCate = (String) hmData.get(KeyData.CATEGORY_DETAIL.TYPE);
            hmData.remove(KeyData.CATEGORY_DETAIL.TYPE);
        }

        if (hmData.containsKey(KeyData.CATEGORY_DETAIL.OFFSET)) {
            String offset = (String) hmData.get(KeyData.CATEGORY_DETAIL.OFFSET);
            mOffset = Integer.parseInt(offset);
            hmData.remove(KeyData.CATEGORY_DETAIL.OFFSET);
        }

        if (hmData.containsKey(KeyData.CATEGORY_DETAIL.LIMIT)) {
            String limit = (String) hmData.get(KeyData.CATEGORY_DETAIL.LIMIT);
            mLimit = Integer.parseInt(limit);
            hmData.remove(KeyData.CATEGORY_DETAIL.LIMIT);
        }

    }


    protected void initListener() {

        mSortListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortPopup sortPopup = new SortPopup();
                sortPopup.setCurrentValue(mCurrentSort);
                sortPopup.setCallBack(new SortCallBack() {
                    @Override
                    public void onSort(int sortValue) {
                        mCurrentSort = sortValue;
                        mModel = null;
                        mDelegate.showLoading();
                        requestCategoryDetail();
                    }
                });
                sortPopup.createPopup();
            }
        };

        onChangeViewClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tagView == Constants.TAG_GRIDVIEW) {
                    tagView = Constants.TAG_LISTVIEW;
                } else {
                    tagView = Constants.TAG_GRIDVIEW;
                }
                mDelegate.setTagView(tagView);
                mDelegate.changeView();
            }
        };

        onListScroll = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!DataLocal.isTablet) {
                    if (dy <= 0) {
                        // Scrolling up
                        mDelegate.showBottomMenu(true);
                    } else {
                        // Scrolling down
                        mDelegate.showBottomMenu(false);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int count = recyclerView.getChildCount();
                int lastPosition = 0;
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if (tagView.equals(Constants.TAG_LISTVIEW)) {
                    if (manager instanceof LinearLayoutManager) {
                        lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    }
                } else {
                    if (manager instanceof GridLayoutManager) {
                        lastPosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    }
                }
                int threshold = mOffset + mLimit - 1;
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

        mFilterListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mLayerEntity) {
                    ArrayList<FilterState> states = mLayerEntity.getListState();
                    ArrayList<FilterEntity> filters = mLayerEntity.getListFilter();

                    FilterPopup filterPopup = new FilterPopup();
                    filterPopup.setListFilter(filters);
                    filterPopup.setStates(states);
                    //filterPopup.setNameCate(m);
                    filterPopup.setJSONFilter(mJSONFilter);
                    filterPopup.setCallBack(new FilterCallBack() {
                        @Override
                        public void requestFilter(JSONObject jsFilter) {
                            mJSONFilter = jsFilter;
                            mModel = null;
                            mDelegate.showLoading();
                            requestCategoryDetail();
                        }
                    });
                    filterPopup.createPopup();

                }
            }
        };

    }

    protected void requestCategoryDetail() {

        if (null == mModel) {
            mModel = new CategoryDetailModel(mTypeCate);
            if (mTypeCate.equals(ValueData.CATEGORY_DETAIL.CUSTOM)) {
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
                mDelegate.dismissDialogLoading();
                mDelegate.showLoadMore(false);
                mResultNumber = ((CategoryDetailModel) mModel).getResultNumber();
                if(mOffset == 0) {
                    mDelegate.showTotalQuantity(String.valueOf(mResultNumber));
                }
                mLayerEntity = ((CategoryDetailModel) mModel).getLayerEntity();
                mDelegate.showLoadMore(false);
                mDelegate.updateView(mModel.getCollection());
                if ((mOffset + mLimit) <= mResultNumber)
                    isOnscroll = true;
            }
        });

        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissLoading();
                mDelegate.dismissDialogLoading();
                mDelegate.showLoadMore(false);
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
        mModel.addBody("sort_option", String.valueOf(mCurrentSort));
        if (null != mJSONFilter) {
            mModel.addBody("filter", mJSONFilter);
        }
        mModel.addBody(KeyData.CATEGORY_DETAIL.OFFSET, String.valueOf(mOffset));
        mModel.addBody(KeyData.CATEGORY_DETAIL.LIMIT, String.valueOf(mLimit));
    }


    @Override
    public void onResume() {
        mDelegate.setTagView(tagView);
        mDelegate.updateView(mModel.getCollection());
    }

    public void setTagView(String tagView) {
        this.tagView = tagView;
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

    public View.OnClickListener getSortListener() {
        return mSortListener;
    }

    public View.OnClickListener getFilterListener() {
        return mFilterListener;
    }
}
