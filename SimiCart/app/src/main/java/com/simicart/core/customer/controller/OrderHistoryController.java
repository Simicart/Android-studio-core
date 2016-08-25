package com.simicart.core.customer.controller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.delegate.OrderHistoryDelegate;
import com.simicart.core.customer.entity.OrderHistory;
import com.simicart.core.customer.fragment.OrderHistoryDetailFragment;
import com.simicart.core.customer.model.OrderHistoryModel;

import java.util.HashMap;

public class OrderHistoryController extends SimiController {
    protected OrderHistoryDelegate mDelegate;
    protected RecyclerView.OnScrollListener mScrollListener;
    protected int mOffset = 0;
    protected int mLimit = 5;
    protected int itemCount = 0;
    protected boolean mCheckOnScroll = true;

    public RecyclerView.OnScrollListener getScrollListener() {
        return mScrollListener;
    }

    public void setDelegate(OrderHistoryDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void onStart() {

        mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                itemCount = recyclerView.getChildCount();
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int threshold = mOffset + mLimit - 1;
                if (lastPosition == threshold
                        && mOffset <= itemCount) {
                    if (mCheckOnScroll == true) {
                        mOffset += mLimit;
                        mCheckOnScroll = false;
                        mDelegate.isShowLoadMore(true);
                        onRequestData();
                    }
                }
            }
        };

        onRequestData();

    }

    protected void onRequestData() {
        if (mOffset == 0) {
            mDelegate.showLoading();
        }
        if(mModel == null) {
            mModel = new OrderHistoryModel();
        }
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                mDelegate.isShowLoadMore(false);
                mDelegate.updateView(mModel.getCollection());
                if (DataLocal.isTablet
                        && mModel.getCollection().getCollection().size() > 0) {
                    onSelectedItem(0);
                }
                itemCount = mModel.getCollection().getCollection().size();
                if (itemCount >= mOffset) {
                    mCheckOnScroll = true;
                }
            }
        });
        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissLoading();
                mDelegate.isShowLoadMore(false);
            }
        });

        String email = DataPreferences.getEmail();
        String pass = DataPreferences.getPassword();

        mModel.addBody(Constants.USER_EMAIL, email);
        mModel.addBody(Constants.USER_PASSWORD, pass);
        mModel.addBody(Constants.LIMIT, String.valueOf(mLimit));
        mModel.addBody(Constants.OFFSET, String.valueOf(mOffset));

        mModel.request();
    }

    protected void onSelectedItem(int position) {
        OrderHistory orderHis = (OrderHistory) mModel.getCollection()
                .getCollection().get(position);

        HashMap<String, Object> hmData = new HashMap<String, Object>();
        hmData.put(KeyData.ORDER_HISTORY_DETAIL.ORDER_ID, orderHis.getOrder_id());
        hmData.put(KeyData.ORDER_HISTORY_DETAIL.TARGET, 0);
        SimiData data = new SimiData(hmData);
        OrderHistoryDetailFragment fragment = OrderHistoryDetailFragment.newInstance(data);
        if (DataLocal.isTablet) {
            SimiManager.getIntance().addFragmentSub(fragment);
        } else {
            SimiManager.getIntance().replaceFragment(fragment);
        }
    }

    @Override
    public void onResume() {
        mDelegate.updateView(mModel.getCollection());
    }

}
