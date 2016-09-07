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
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.component.AddressCheckoutComponent;
import com.simicart.core.checkout.delegate.AddressComponentCallback;
import com.simicart.core.checkout.entity.ShippingMethodEntity;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.AddressEntity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by frank on 9/7/16.
 */
public class ExpressAddressFragment extends SimiFragment {

    public static ExpressAddressFragment newInstance(SimiData data) {
        ExpressAddressFragment fragment = new ExpressAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    protected AddressEntity shippingAddress;
    protected AddressEntity billingAddress;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int idView = Rconfig.getInstance().layout("plugin_paypalexpress_address");
        rootView = inflater.inflate(idView, null, false);

        if (mHashMapData.containsKey("shipping_address")) {
            LinearLayout llShipping = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id("ll_shipping_address"));
            shippingAddress = (AddressEntity) mHashMapData.get("shipping_address");
            AddressCheckoutComponent shippingComponent = new AddressCheckoutComponent(AddressCheckoutComponent.SHIPPING_TYPE, shippingAddress);
            shippingComponent.setCallBack(new AddressComponentCallback() {
                @Override
                public void onSelect() {
                    onEditAddress(true);
                }
            });
            View shippingView = shippingComponent.createView();
            llShipping.addView(shippingView);
        }

        if (mHashMapData.containsKey("billing_address")) {
            LinearLayout llBilling = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id("ll_billing_address"));
            billingAddress = (AddressEntity) mHashMapData.get("billing_address");
            AddressCheckoutComponent billingComponent = new AddressCheckoutComponent(AddressCheckoutComponent.BILLING_TYPE, billingAddress);
            billingComponent.setCallBack(new AddressComponentCallback() {
                @Override
                public void onSelect() {
                    onEditAddress(false);
                }
            });
            View billingView = billingComponent.createView();
            llBilling.addView(billingView);
        }

        AppCompatButton btnConfirm = (AppCompatButton) rootView.findViewById(Rconfig.getInstance().id("btn_confirm_address"));
        btnConfirm.setTextColor(AppColorConfig.getInstance().getContentColor());
        btnConfirm.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());
        String tranConfirm = SimiTranslator.getInstance().translate("Confirm Address");
        btnConfirm.setText(tranConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmAddress();
            }
        });

        return rootView;
    }

    protected void onEditAddress(boolean isShipping) {

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("billing_address", billingAddress);
        hm.put("shipping_address", shippingAddress);
        if (isShipping) {
            hm.put(KeyData.ADDRESS_BOOK_DETAIL.ADDRESS_FOR_EDIT, shippingAddress);
            hm.put("type_edit", ExpressEditAddressFragment.EDIT_SHIPPING_ADDRESS);
        } else {
            hm.put(KeyData.ADDRESS_BOOK_DETAIL.ADDRESS_FOR_EDIT, billingAddress);
            hm.put("type_edit", ExpressEditAddressFragment.EDIT_BILLING_ADDRESS);
        }
        hm.put(KeyData.ADDRESS_BOOK_DETAIL.OPEN_FOR, ValueData.ADDRESS_BOOK_DETAIL.OPEN_FOR_CUSTOMER);
        hm.put(KeyData.ADDRESS_BOOK_DETAIL.ACTION, ValueData.ADDRESS_BOOK_DETAIL.ACTION_EDIT);

        SimiData data = new SimiData(hm);
        ExpressEditAddressFragment editAddressFragment = ExpressEditAddressFragment.newInstance(data);
        SimiManager.getIntance().addPopupFragment(editAddressFragment);


    }

    protected void onConfirmAddress() {
        SimiModel confirmModel = new SimiModel();
        confirmModel.setUrlAction("paypalexpress/api/update_address");

        confirmModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {

            }
        });

        confirmModel.setSuccessListener(new ModelSuccessCallBack() {
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

        if (null != billingAddress) {
            confirmModel.addBody("billingAddress", billingAddress.toParamForPlaceOrder());
        }

        if (null != shippingAddress) {
            confirmModel.addBody("shippingAddress", shippingAddress.toParamForPlaceOrder());
        }

        confirmModel.request();


    }

}
