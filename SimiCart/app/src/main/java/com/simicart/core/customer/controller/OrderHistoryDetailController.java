package com.simicart.core.customer.controller;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.delegate.OrderHistoryReOrderDelegate;
import com.simicart.core.customer.model.OrderHistoryDetailModel;
import com.simicart.core.customer.model.OrderHistoryReOrderModel;

public class OrderHistoryDetailController extends SimiController {

    protected SimiDelegate mDelegate;
    protected String mID;
    protected View.OnClickListener mReOrderClicker;

    public View.OnClickListener getReOrderClicker() {
        return mReOrderClicker;
    }

    public void setID(String id) {
        mID = id;
    }

    public void setDelegate(SimiDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void onStart() {
        requestGetOrderDetail();

        mReOrderClicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestReOrder();
            }
        };
    }

    @Override
    public void onResume() {
        mDelegate.updateView(mModel.getCollection());
    }

    protected void requestGetOrderDetail() {
        mDelegate.showLoading();
        mModel = new OrderHistoryDetailModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                mDelegate.updateView(mModel.getCollection());
            }
        });
        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissDialogLoading();
                SimiNotify.getInstance().showNotify(error.getMessage());
            }
        });

        String email = DataPreferences.getEmail();
        String password = DataPreferences.getPassword();

        mModel.addBody(Constants.USER_EMAIL, email);
        mModel.addBody(Constants.USER_PASSWORD, password);
        mModel.addBody(Constants.ORDER_ID, mID);

        mModel.request();
    }

    protected void requestReOrder() {
        mDelegate.showDialogLoading();
        final OrderHistoryReOrderModel mModel = new OrderHistoryReOrderModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissDialogLoading();
                int numberQty = 0;
                ArrayList<SimiEntity> entity = mModel.getCollection()
                        .getCollection();
                if (null != entity && entity.size() > 0) {
                    for (SimiEntity simiEntity : entity) {
                        String qty = simiEntity.getData("cart_qty");
                        if (null != qty && !qty.equals("")
                                && !qty.equals("null")) {
                            numberQty += Integer.parseInt(qty);
                        }
                    }
                }
                SimiManager.getIntance().onUpdateCartQty(
                        String.valueOf(numberQty));
            }
        });
        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissDialogLoading();
                SimiNotify.getInstance().showNotify(error.getMessage());
            }
        });
        mModel.addBody(Constants.ORDER_ID, mID);
        mModel.request();
    }
}
