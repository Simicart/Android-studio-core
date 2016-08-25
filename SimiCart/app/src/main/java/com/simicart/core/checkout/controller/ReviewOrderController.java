package com.simicart.core.checkout.controller;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.checkout.component.AddressCheckoutComponent;
import com.simicart.core.checkout.component.CouponComponent;
import com.simicart.core.checkout.component.ListProductCheckoutComponent;
import com.simicart.core.checkout.component.PaymentMethodComponent;
import com.simicart.core.checkout.component.ShippingMethodComponent;
import com.simicart.core.checkout.component.TermConditionComponent;
import com.simicart.core.checkout.delegate.AddressComponentCallback;
import com.simicart.core.checkout.delegate.PaymentMethodCallBack;
import com.simicart.core.checkout.delegate.ReviewOrderDelegate;
import com.simicart.core.checkout.delegate.ShippingMethodCallBack;
import com.simicart.core.checkout.entity.PaymentMethodEntity;
import com.simicart.core.checkout.entity.ReviewOrderEntity;
import com.simicart.core.checkout.entity.ShippingMethodEntity;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.checkout.model.ReviewOrderModel;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.AppCheckoutConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.entity.AddressEntity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ClickableViewAccessibility")
public class ReviewOrderController extends SimiController {

    protected ReviewOrderDelegate mDelegate;
    protected int mPlaceFor;
    protected AddressEntity mBillingAddress;
    protected AddressEntity mShippingAddress;
    protected ReviewOrderEntity mReviewOrderEntity;
    protected HashMap<String, Object> mData;
    protected ArrayList<SimiComponent> mListComponent;
    protected OnClickListener mPlaceOrderListener;


    @Override
    public void onStart() {

        parseParam();

        initListener();

        requestPlaceOrder();
    }

    protected void parseParam() {

        if (mData.containsKey(KeyData.REVIEW_ORDER.PLACE_FOR)) {
            mPlaceFor = ((Integer) mData.get(KeyData.REVIEW_ORDER.PLACE_FOR)).intValue();
        }

        if (mData.containsKey(KeyData.REVIEW_ORDER.BILLING_ADDRESS)) {
            mBillingAddress = (AddressEntity) mData.get(KeyData.REVIEW_ORDER.BILLING_ADDRESS);
        }

        if (mData.containsKey(KeyData.REVIEW_ORDER.SHIPPING_ADDRESS)) {
            mShippingAddress = (AddressEntity) mData.get(KeyData.REVIEW_ORDER.SHIPPING_ADDRESS);
        }

    }

    protected void initListener() {
        mPlaceOrderListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                SimiManager.getIntance().hideKeyboard();
            }
        };
    }

    protected void requestPlaceOrder() {
        mDelegate.showLoading();
        mModel = new ReviewOrderModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mListComponent = new ArrayList<>();
                if (null != collection) {
                    ArrayList<SimiEntity> entities = collection.getCollection();
                    if (null != entities && entities.size() > 0) {
                        mReviewOrderEntity = (ReviewOrderEntity) entities.get(0);
                        showData();
                    }
                }
            }
        });

        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissLoading();
            }
        });

        String password = DataPreferences.getPassword();
        if (mPlaceFor == ValueData.REVIEW_ORDER.PLACE_FOR_NEW_CUSTOMER) {
            mModel.addBody("customer_password", password);
            mModel.addBody("confirm_password", password);
        } else {
            mModel.addBody("customer_password", "");
            mModel.addBody("confirm_password", "");
        }

        JSONObject jsBillingAddress = mBillingAddress.toParamForPlaceOrder();
        if (null != jsBillingAddress) {
            mModel.addBody("billing_address", jsBillingAddress);
        }

        JSONObject jsShippingAddress = mShippingAddress.toParamForPlaceOrder();
        if (null != jsShippingAddress) {
            mModel.addBody("shipping_address", jsShippingAddress);
        }

        mModel.request();

    }


    protected void showData() {

        showShippingAddress();

        showBillingAddress();

        showShippingMethod();

        showPaymentMethod();

        showShipmentDetail();

        showCouponCode();

        showTotalPrice();

        showTermCondition();

        // dispatch event for plugin

        ArrayList<View> rows = new ArrayList<>();
        if (null != mListComponent) {
            for (int i = 0; i < mListComponent.size(); i++) {
                View row = mListComponent.get(i).createView();
                rows.add(row);
            }
        }

        mDelegate.showView(rows);
    }

    protected void showShippingAddress() {
        AddressCheckoutComponent shippingAddressComponent = new AddressCheckoutComponent(AddressCheckoutComponent.SHIPPING_TYPE, mShippingAddress);
        shippingAddressComponent.setCallBack(new AddressComponentCallback() {
            @Override
            public void onSelect() {

            }
        });
        mListComponent.add(shippingAddressComponent);
    }

    protected void showBillingAddress() {
        AddressCheckoutComponent billingAddressComponent = new AddressCheckoutComponent(AddressCheckoutComponent.BILLING_TYPE, mBillingAddress);
        billingAddressComponent.setCallBack(new AddressComponentCallback() {
            @Override
            public void onSelect() {

            }
        });
        mListComponent.add(billingAddressComponent);
    }

    protected void showShippingMethod() {
        ArrayList<ShippingMethodEntity> listShipping = mReviewOrderEntity.getListShippingMethod();
        if (null != listShipping && listShipping.size() > 0) {
            ShippingMethodComponent shipingComponent = new ShippingMethodComponent(listShipping);
            shipingComponent.setCallBack(new ShippingMethodCallBack() {
                @Override
                public void onSelect(ShippingMethodEntity entity) {

                }
            });
            mListComponent.add(shipingComponent);
        }
    }

    protected void showPaymentMethod() {
        ArrayList<PaymentMethodEntity> listPayment = mReviewOrderEntity.getListPaymentMethod();
        if (null != listPayment && listPayment.size() > 0) {
            PaymentMethodComponent paymentComponent = new PaymentMethodComponent(listPayment);
            paymentComponent.setCallBack(new PaymentMethodCallBack() {
                @Override
                public void onSelectItem(PaymentMethodEntity payment) {

                }

                @Override
                public void onEditAction(PaymentMethodEntity payment) {

                }
            });
            mListComponent.add(paymentComponent);
        }
    }

    protected void showShipmentDetail() {
        //  ListProductCheckoutComponent shipmentComponent = new ListProductCheckoutComponent()
    }

    protected void showCouponCode() {
        String couponCode = "";
        CouponComponent couponComponent = new CouponComponent(couponCode);
        mListComponent.add(couponComponent);
    }

    protected void showTotalPrice() {

    }

    protected void showTermCondition() {
        if (AppCheckoutConfig.getInstance().isenableAgreements()) {
            TermConditionComponent termConditionComponent = new TermConditionComponent();

        }
    }


    @Override
    public void onResume() {
        ArrayList<View> rows = new ArrayList<>();
        if (null != mListComponent) {
            for (int i = 0; i < mListComponent.size(); i++) {
                View row = mListComponent.get(i).createView();
                rows.add(row);
            }
        }

        mDelegate.showView(rows);
    }

    public void setData(HashMap<String, Object> data) {
        mData = data;
    }

    public void setDelegate(ReviewOrderDelegate delegate) {
        this.mDelegate = delegate;
    }

    public OnClickListener getPlaceOrderListener() {
        return mPlaceOrderListener;
    }

}
