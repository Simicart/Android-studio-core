package com.simicart.plugins.paypalexpress.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.payment.WebviewPaymentCallBack;
import com.simicart.core.base.payment.WebviewPaymentComponent;
import com.simicart.core.checkout.entity.ShippingMethodEntity;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.AddressEntity;
import com.simicart.plugins.paypalexpress.model.AddressExpressModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by frank on 9/7/16.
 */
public class ExpressWebviewFragment extends SimiFragment {

    protected LinearLayout llPayment;
    protected String mUrl;
    protected boolean isReviewAddress;

    public static ExpressWebviewFragment newInstance(SimiData data) {
        ExpressWebviewFragment fragment = new ExpressWebviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int idView = Rconfig.getInstance().layout("core_fragment_payment");
        rootView = inflater.inflate(idView, null);
        llPayment = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id("ll_payment"));

        if (mHashMapData.containsKey("url")) {
            mUrl = (String) mHashMapData.get("url");
        }

        if (mHashMapData.containsKey("review_address")) {
            isReviewAddress = (Boolean) mHashMapData.get("review_address");
        }

        if (Utils.validateString(mUrl)) {

            HashMap<String, Object> hm = new HashMap<>();
            hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.URL, mUrl);
            hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_SUCCESS, "return");

            WebviewPaymentComponent component = new WebviewPaymentComponent(hm, new WebviewPaymentCallBack() {
                @Override
                public boolean onSuccess(String url) {
                    processUrl(url);
                    return true;
                }

                @Override
                public boolean onFail(String url) {
                    return false;
                }

                @Override
                public boolean onError(String url) {
                    return false;
                }

                @Override
                public boolean onReview(String url) {
                    return false;
                }

                @Override
                public boolean onOther(String url) {
                    return false;
                }
            });

            View viewComponent = component.createView();
            llPayment.addView(viewComponent);

        }

        return rootView;
    }

    protected void processUrl(String url) {
        if (isReviewAddress) {
            requestAddress();
        } else {
            requestShipping();
        }
    }

    protected void requestAddress() {
        final AddressExpressModel model = new AddressExpressModel();

        model.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                if (null != error) {
                    String msg = error.getMessage();
                    if (Utils.validateString(msg)) {
                        SimiNotify.getInstance().showNotify(msg);
                        SimiManager.getIntance().backToHomeFragment();
                    }
                }
            }
        });


        model.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                AddressEntity billingAddress = model.getBillingAddress();
                AddressEntity shippingAddres = model.getShippingAddress();

                HashMap<String, Object> hm = new HashMap<>();
                hm.put("billing_address", billingAddress);
                hm.put("shipping_address", shippingAddres);
                SimiData data = new SimiData(hm);

                ExpressAddressFragment fragment = ExpressAddressFragment.newInstance(data);
                SimiManager.getIntance().addPopupFragment(fragment);

            }
        });

        model.request();


    }

    protected void requestShipping() {
        SimiModel shippingModel = new SimiModel();
        shippingModel.setUrlAction("");

        shippingModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                if (null != error) {
                    String msg = error.getMessage();
                    if (Utils.validateString(msg)) {
                        SimiNotify.getInstance().showNotify(msg);
                        SimiManager.getIntance().backToHomeFragment();
                    }
                }
            }
        });

        shippingModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                ArrayList<SimiEntity> entities = collection.getCollection();
                if (null != entities && entities.size() > 0) {
                    ArrayList<ShippingMethodEntity> listShipping = new ArrayList<>();
                    for (int i = 0; i < entities.size(); i++) {
                        SimiEntity entity = entities.get(i);
                        ShippingMethodEntity shippingMethodEntity = new ShippingMethodEntity();
                        shippingMethodEntity.parse(entity.getJSONObject());
                        listShipping.add(shippingMethodEntity);
                    }

                    HashMap<String, Object> hm = new HashMap<>();
                    hm.put("list_shipping", listShipping);
                    SimiData data = new SimiData(hm);

                    ExpressShippingMethodFragment fragment = ExpressShippingMethodFragment.newInstance(data);
                    SimiManager.getIntance().addPopupFragment(fragment);
                }

            }
        });

        shippingModel.request();
    }


}
