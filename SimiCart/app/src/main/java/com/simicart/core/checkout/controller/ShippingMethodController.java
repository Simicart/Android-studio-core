package com.simicart.core.checkout.controller;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.checkout.delegate.SelectedShippingMethodDelegate;
import com.simicart.core.checkout.delegate.ShippingMethodDelegate;
import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.checkout.entity.ShippingMethod;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.checkout.model.ShippingMethodModel;

public class ShippingMethodController extends SimiController {

    protected ShippingMethodDelegate mDelegate;
    protected ArrayList<ShippingMethod> mListShipping;
    protected OnItemClickListener mItemClicker;
    protected SelectedShippingMethodDelegate mSelectedDelegate;

    public void setDelegate(ShippingMethodDelegate delegate) {
        mDelegate = delegate;
    }

    public void setListShipping(ArrayList<ShippingMethod> listShipping) {
        mListShipping = listShipping;
    }

    public void setSelectedShippingMethodDelegate(
            SelectedShippingMethodDelegate delegate) {
        mSelectedDelegate = delegate;
    }

    public OnItemClickListener getItemClicker() {
        return mItemClicker;
    }

    @Override
    public void onStart() {
        mDelegate.updateListShippingMethod(mListShipping);
        mItemClicker = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                onSeletected(position);

            }

        };

    }

    protected void onSeletected(int position) {

        final ShippingMethod shippingMethod = mListShipping.get(position);
        if (!shippingMethod.isS_method_selected()) {
            mListShipping.get(position).setS_method_selected(true);
            for (int i = 0; i < mListShipping.size(); i++) {
                if (i != position) {
                    mListShipping.get(i).setS_method_selected(false);
                }
            }
            String code = shippingMethod.getS_method_code();
            String id = shippingMethod.getS_method_id();

            mDelegate.showLoading();
            mModel = new ShippingMethodModel();
            mModel.setSuccessListener(new ModelSuccessCallBack() {
                @Override
                public void onSuccess(SimiCollection collection) {
                    mDelegate.dismissLoading();
                    mDelegate.updateListShippingMethod(mListShipping);
                    SimiManager.getIntance().backPreviousFragment();
                    ArrayList<PaymentMethod> paymentMethod = ((ShippingMethodModel) mModel)
                            .getListPayment();
                    if (null != paymentMethod && paymentMethod.size() > 0) {
                        mSelectedDelegate
                                .updatePaymentMethod(paymentMethod);
                    }
                    TotalPrice totalPrice = ((ShippingMethodModel) mModel)
                            .getTotalPrice();
                    if (null != totalPrice) {

                        mSelectedDelegate.updateTotalPrice(totalPrice);
                    }

                    mSelectedDelegate.updateShippingMethod(shippingMethod);
                }
            });

            mModel.addBody("s_method_id", id);
            mModel.addBody("s_method_code", code);
            mModel.request();

        }

    }

    @Override
    public void onResume() {
        mDelegate.updateListShippingMethod(mListShipping);

    }

}
