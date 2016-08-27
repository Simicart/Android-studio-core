package com.simicart.core.checkout.controller;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.magestore.simicart.R;
import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.checkout.component.AddressCheckoutComponent;
import com.simicart.core.checkout.component.CouponComponent;
import com.simicart.core.checkout.component.CreditCardPopup;
import com.simicart.core.checkout.component.PaymentMethodComponent;
import com.simicart.core.checkout.component.ShippingMethodComponent;
import com.simicart.core.checkout.component.TermConditionComponent;
import com.simicart.core.checkout.component.TotalPriceComponent;
import com.simicart.core.checkout.delegate.AddressComponentCallback;
import com.simicart.core.checkout.delegate.CreditCardCallBack;
import com.simicart.core.checkout.delegate.PaymentMethodCallBack;
import com.simicart.core.checkout.delegate.ReviewOrderDelegate;
import com.simicart.core.checkout.delegate.ShippingMethodCallBack;
import com.simicart.core.checkout.entity.OrderInforEntity;
import com.simicart.core.checkout.entity.PaymentMethodEntity;
import com.simicart.core.checkout.entity.ReviewOrderEntity;
import com.simicart.core.checkout.entity.ShippingMethodEntity;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.checkout.model.PaymentMethodModel;
import com.simicart.core.checkout.model.PlaceOrderModel;
import com.simicart.core.checkout.model.ReviewOrderModel;
import com.simicart.core.checkout.model.ShippingMethodModel;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.AppCheckoutConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.customer.entity.AddressEntity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
    protected PaymentMethodComponent mPaymentComponent;
    protected PaymentMethodEntity mCurrentPaymentMethod;
    protected TotalPriceComponent mTotalPriceComponent;


    @Override
    public void onStart() {

        parseParam();

        initListener();

        getOrderInfor();
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
                requestPlaceOrder();
            }
        };
    }

    protected void getOrderInfor() {
        mDelegate.showLoading();
        mModel = new ReviewOrderModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
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
            mModel.addBody("billingAddress", jsBillingAddress);
        }

        JSONObject jsShippingAddress = mShippingAddress.toParamForPlaceOrder();
        if (null != jsShippingAddress) {
            mModel.addBody("shippingAddress", jsShippingAddress);
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

        showView();
    }

    protected void showShippingAddress() {
        AddressCheckoutComponent shippingAddressComponent = new AddressCheckoutComponent(AddressCheckoutComponent.SHIPPING_TYPE, mShippingAddress);
        shippingAddressComponent.setCallBack(new AddressComponentCallback() {
            @Override
            public void onSelect() {
                edtiAddress(true);
            }
        });
        mListComponent.add(shippingAddressComponent);
    }

    protected void showBillingAddress() {
        AddressCheckoutComponent billingAddressComponent = new AddressCheckoutComponent(AddressCheckoutComponent.BILLING_TYPE, mBillingAddress);
        billingAddressComponent.setCallBack(new AddressComponentCallback() {
            @Override
            public void onSelect() {
                edtiAddress(false);
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
                    selectShippingMethod(entity);
                }
            });
            mListComponent.add(shipingComponent);
        }
    }

    protected void showPaymentMethod() {
        ArrayList<PaymentMethodEntity> listPayment = mReviewOrderEntity.getListPaymentMethod();
        if (null != listPayment && listPayment.size() > 0) {
            mPaymentComponent = new PaymentMethodComponent(listPayment);
            mPaymentComponent.setCallBack(new PaymentMethodCallBack() {
                @Override
                public void onSelectItem(PaymentMethodEntity payment) {
                    selectPaymentMethod(payment);
                }

                @Override
                public void onEditAction(PaymentMethodEntity payment) {

                }
            });
            mListComponent.add(mPaymentComponent);
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
        TotalPrice totalPrice = mReviewOrderEntity.getTotalPrice();
        mTotalPriceComponent = new TotalPriceComponent(totalPrice);
        mListComponent.add(mTotalPriceComponent);
    }

    protected void showTermCondition() {
        if (AppCheckoutConfig.getInstance().isenableAgreements()) {
            TermConditionComponent termConditionComponent = new TermConditionComponent();

        }
    }

    protected void edtiAddress(boolean isShipping) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(KeyData.ADDRESS_BOOK.OPEN_FOR, ValueData.ADDRESS_BOOK.OPEN_FOR_CHECKOUT);
        hm.put(KeyData.ADDRESS_BOOK.BILLING_ADDRESS, mBillingAddress);
        hm.put(KeyData.ADDRESS_BOOK.SHIPPING_ADDRESS, mShippingAddress);
        if (isShipping) {
            hm.put(KeyData.ADDRESS_BOOK.ACTION, ValueData.ADDRESS_BOOK.ACTION_EDIT_SHIPPING_ADDRESS);
        } else {
            hm.put(KeyData.ADDRESS_BOOK.ACTION, ValueData.ADDRESS_BOOK.ACTION_EDIT_BILLING_ADDRESS);
        }
        SimiManager.getIntance().openAddressBook(hm);
    }

    protected void selectShippingMethod(ShippingMethodEntity shippingMethodEntity) {
        mDelegate.showDialogLoading();
        final ShippingMethodModel shippingModel = new ShippingMethodModel();
        shippingModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissDialogLoading();
                TotalPrice totalPrice = shippingModel.getTotalPrice();
                mReviewOrderEntity.setTotalPrice(totalPrice);

                ArrayList<PaymentMethodEntity> payments = shippingModel.getListPaymentMehtod();
                mPaymentComponent.setListPaymentMethod(payments);
                mReviewOrderEntity.setListPaymentMethod(payments);

                showView();
            }
        });
        shippingModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissDialogLoading();
                String message = error.getMessage();
                SimiNotify.getInstance().showNotify(message);
            }
        });

        String idShipping = shippingMethodEntity.getID();
        mModel.addBody("s_method_id", idShipping);

        String codeShipping = shippingMethodEntity.getCode();
        mModel.addBody("s_method_code", codeShipping);

        shippingModel.request();

    }

    protected void selectPaymentMethod(PaymentMethodEntity paymentMethodEntity) {
        PaymentMethodEntity.PAYMENTMETHODTYPE type = paymentMethodEntity.getType();
        if (type == PaymentMethodEntity.PAYMENTMETHODTYPE.CARD && !paymentMethodEntity.isSavedLocal()) {
            mCurrentPaymentMethod = paymentMethodEntity;
            createNewCreditCard();
        } else {
            if (AppStoreConfig.getInstance().isReloadPaymentMethod()) {
                reloadPayment(paymentMethodEntity);
            } else {
                mCurrentPaymentMethod = paymentMethodEntity;
            }
        }
    }

    protected void reloadPayment(final PaymentMethodEntity paymentMethodEntity) {
        mDelegate.showDialogLoading();

        final PaymentMethodModel methodModel = new PaymentMethodModel();
        methodModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissDialogLoading();
                TotalPrice totalPrice = methodModel.getTotalPrice();
                mReviewOrderEntity.setTotalPrice(totalPrice);
                mCurrentPaymentMethod = paymentMethodEntity;
                showView();
            }
        });

        methodModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissDialogLoading();
            }
        });

        HashMap<String, String> data = paymentMethodEntity.toParam();
        Iterator<String> iterator = data.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = data.get(key);
            if (Utils.validateString(key) && Utils.validateString(value)) {
                methodModel.addBody(key, value);
            }
        }
        methodModel.request();
    }

    protected void createNewCreditCard() {
        CreditCardPopup creditCardPopup = new CreditCardPopup(mCurrentPaymentMethod);
        creditCardPopup.setCallBack(new CreditCardCallBack() {
            @Override
            public void onSaveCreditCard(PaymentMethodEntity paymentMethodEntity) {
                mCurrentPaymentMethod = paymentMethodEntity;
                if (AppStoreConfig.getInstance().isReloadPaymentMethod()) {
                    reloadPayment(paymentMethodEntity);
                }
            }
        });
        creditCardPopup.show();
    }

    protected void requestPlaceOrder() {

        if (isCanPlcaceOrder()) {
            mDelegate.showLoading();

            if (dispatchEventBeforePlace()) {
                return;
            }

            final PlaceOrderModel placeOrderModel = new PlaceOrderModel();

            placeOrderModel.setSuccessListener(new ModelSuccessCallBack() {
                @Override
                public void onSuccess(SimiCollection collection) {
                    Log.e("ReviewOrderController", "PLACE ORDER SUCCESS");


                    ArrayList<SimiEntity> entities = collection.getCollection();
                    if (null != entities && entities.size() > 0) {
                        OrderInforEntity orderInforEntity = (OrderInforEntity) entities.get(0);
                        dispatchEventAfterPlace(orderInforEntity);
                    }

                    SimiManager.getIntance().backToHomeFragment();
                }
            });

            placeOrderModel.setFailListener(new ModelFailCallBack() {
                @Override
                public void onFail(SimiError error) {
                    Log.e("ReviewOrderController", "PLACE ORDER FAIL");
                }
            });

            HashMap<String, String> data = mCurrentPaymentMethod.toParam();
            Iterator<String> iterator = data.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = data.get(key);
                if (Utils.validateString(key) && Utils.validateString(value)) {
                    placeOrderModel.addBody(key, value);
                }
            }

            placeOrderModel.request();
        }
    }


    protected boolean isCanPlcaceOrder() {
        if (null == mCurrentPaymentMethod) {
            return false;
        }

        return true;
    }

    protected void onPlaceOrderSuccess(OrderInforEntity orderInforEntity) {

        //                    if (mAfterControll != Constants.NEW_AS_GUEST) {
//                        String email = DataLocal.getEmail();
//                        String password = DataLocal.getPassword();
//                        DataLocal.saveEmailPassRemember(email, password);
//                        DataLocal.saveSignInState(true);
//                        SimiManager.getIntance().onUpdateItemSignIn();
//                    }


        PaymentMethodEntity.PAYMENTMETHODTYPE type = mCurrentPaymentMethod.getType();
        if (type == PaymentMethodEntity.PAYMENTMETHODTYPE.SDK) {
            dispatchEventForPaymentSDK(orderInforEntity);
        } else if (type == PaymentMethodEntity.PAYMENTMETHODTYPE.WEBVIEW) {
            dispatchEventForPaymentWebview(orderInforEntity);
        } else {
            if (orderInforEntity.isShowNotification()) {
                // show notification

            } else {
                // go to thank you page
            }
        }

        dispatchEventAfterPlace(orderInforEntity);

    }


    protected boolean dispatchEventBeforePlace() {

        String paymentMethod = mCurrentPaymentMethod.getPaymentMethod();
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put("payment_method", paymentMethod);
        SimiEvent.dispatchEvent(KeyEvent.REVIEW_ORDER.FOR_PAYMENT_BEFORE_PLACE, hmData);

        return false;
    }

    protected void dispatchEventForPaymentSDK(OrderInforEntity orderInforEntity) {

        String paymentMethod = mCurrentPaymentMethod.getPaymentMethod();
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put("payment_method", paymentMethod);
        hmData.put("review_order_entity", mReviewOrderEntity);
        hmData.put("order_infor_entity", orderInforEntity);
        SimiEvent.dispatchEvent(KeyEvent.REVIEW_ORDER.FOR_PAYMENT_TYPE_SDK, hmData);
    }

    protected void dispatchEventForPaymentWebview(OrderInforEntity orderInforEntity) {
        String paymentMethod = mCurrentPaymentMethod.getPaymentMethod();
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put("payment_method", paymentMethod);
        hmData.put("review_order_entity", mReviewOrderEntity);
        hmData.put("order_infor_entity", orderInforEntity);
        SimiEvent.dispatchEvent(KeyEvent.REVIEW_ORDER.FOR_PAYMENT_TYPE_WEBVIEW, hmData);
    }

    protected void dispatchEventAfterPlace(OrderInforEntity orderInforEntity) {
        String paymentMethod = mCurrentPaymentMethod.getPaymentMethod();
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put("payment_method", paymentMethod);
        hmData.put("review_order_entity", mReviewOrderEntity);
        hmData.put("order_infor_entity", orderInforEntity);
        SimiEvent.dispatchEvent(KeyEvent.REVIEW_ORDER.FOR_PAYMENT_AFTER_PLACE, hmData);
    }


    @Override
    public void onResume() {
        showView();
    }

    protected void showView() {
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
