package com.simicart.plugins.paypalexpress.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
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
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.component.ShippingMethodComponent;
import com.simicart.core.checkout.delegate.ShippingMethodCallBack;
import com.simicart.core.checkout.entity.ShippingMethodEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

/**
 * Created by frank on 9/7/16.
 */
public class ExpressShippingMethodFragment extends SimiFragment {

    protected String mCodeShipping;

    public static ExpressShippingMethodFragment newInstance(SimiData data) {
        ExpressShippingMethodFragment fragment = new ExpressShippingMethodFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int idView = Rconfig.getInstance().layout("plugin_paypalexpress_fragment_shipping");
        rootView = inflater.inflate(idView, null, false);

        if (mHashMapData.containsKey("list_shipping")) {
            ArrayList<ShippingMethodEntity> listShipping = (ArrayList<ShippingMethodEntity>) mHashMapData.get("list_shipping");
            LinearLayout llShippingMethod = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id("ll_shipping_method"));
            if (null != listShipping && listShipping.size() > 0) {
                ShippingMethodComponent shipingComponent = new ShippingMethodComponent(listShipping);
                shipingComponent.setCallBack(new ShippingMethodCallBack() {
                    @Override
                    public void onSelect(ShippingMethodEntity entity) {
                        mCodeShipping = entity.getCode();
                    }
                });

                llShippingMethod.addView(shipingComponent.createView());

            }

            AppCompatButton btnPlaceOrder = (AppCompatButton) rootView.findViewById(Rconfig.getInstance().id("btn_place_order"));
            btnPlaceOrder.setTextColor(AppColorConfig.getInstance().getContentColor());
            btnPlaceOrder.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());
            String tranPlaceOrder = SimiTranslator.getInstance().translate("Place Order");
            btnPlaceOrder.setText(tranPlaceOrder);
            btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPlaceOrder();
                }
            });

        } else {
            rootView.setVisibility(View.GONE);
        }


        return rootView;
    }

    protected void onPlaceOrder() {
        final SimiModel model = new SimiModel();
        model.setUrlAction("paypalexpress/api/place");

        model.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                processAfterPlaceOrder(error);
            }
        });

        model.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {

                SimiError error = model.getError();
                processAfterPlaceOrder(error);

            }
        });

        if (Utils.validateString(mCodeShipping)) {
            model.addBody("shipping_method", mCodeShipping);
        }

        model.request();

    }

    protected void processAfterPlaceOrder(SimiError error) {

        if (null != error) {
            String msg = error.getMessage();
            if (Utils.validateString(msg)) {
                SimiNotify.getInstance().showToast(msg);
            }
        }

        SimiManager.getIntance().backToHomeFragment();

    }

}
