package com.simicart.core.checkout.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.fragment.OrderHistoryDetailFragment;
import com.simicart.core.material.ButtonRectangle;
import com.simicart.core.material.LayoutRipple;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ThankyouFragment extends SimiFragment {

    private TextView tvThankyou;
    private TextView tvOrder;
    private LayoutRipple layout_order;
    private TextView txt_content_message;
    private ButtonRectangle btn_continue_shopping;
    private ImageView imageView;

    private String message = "";

    private String invoice_number = "";

    public static ThankyouFragment newInstance(SimiData data) {
        ThankyouFragment fragment = new ThankyouFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                Rconfig.getInstance().layout("core_thankyou_layout"),
                container, false);


        view.setBackgroundColor(AppColorConfig.getInstance().getAppBackground());
        initView(view);
        handleEvent();
        return view;
    }

    private void initView(View rootView) {
        tvThankyou = (TextView) rootView.findViewById(Rconfig
                .getInstance().id("tvThankyou"));
        tvOrder = (TextView) rootView.findViewById(Rconfig.getInstance().id(
                "tvOrder"));
        txt_content_message = (TextView) rootView.findViewById(Rconfig
                .getInstance().id("txt_content_message"));
        tvThankyou.setTextColor(AppColorConfig.getInstance().getContentColor());
        tvOrder.setTextColor(AppColorConfig.getInstance().getContentColor());
        txt_content_message.setTextColor(AppColorConfig.getInstance().getContentColor());
        layout_order = (LayoutRipple) rootView.findViewById(Rconfig
                .getInstance().id("layout_order"));
        btn_continue_shopping = (ButtonRectangle) rootView.findViewById(Rconfig
                .getInstance().id("btn_continue_shopping"));
        btn_continue_shopping.setText(SimiTranslator.getInstance().translate("Continue Shopping"));
        btn_continue_shopping.setTextColor(Color.parseColor("#ffffff"));
        btn_continue_shopping.setBackgroundColor(AppColorConfig.getInstance()
                .getKeyColor());
        imageView = (ImageView) rootView.findViewById(Rconfig
                .getInstance().id("img_order"));
        //	Utils.changeColorImageview(SimiManager.getIntance().getCurrentActivity(), imageView, "ic_extend");
        //set data
        tvThankyou.setText(SimiTranslator.getInstance().translate(message));
        tvOrder.setText(SimiTranslator.getInstance().translate("Your order # is:") + invoice_number);
        txt_content_message.setText(SimiTranslator.getInstance().translate("You will receive an order confirmation email with detail of your order and alink to track its progress"));
        if (DataPreferences.isSignInComplete()) {
            layout_order.setVisibility(View.VISIBLE);
        } else {
            layout_order.setVisibility(View.GONE);
        }
    }

    private void parseJson(JSONObject object) {
        try {
            if (object.getString("status").equals("SUCCESS")) {
                JSONArray arrayData = object.getJSONArray("data");
                if (arrayData.length() > 0) {
                    JSONObject jsonObject = arrayData.getJSONObject(0);
                    if (jsonObject.has("invoice_number")) {
                        invoice_number = jsonObject.getString("invoice_number");
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Error paser json:", e.getMessage());
        }
    }

    private void handleEvent() {
        btn_continue_shopping.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SimiManager.getIntance().backToHomeFragment();
            }
        });
        layout_order.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                HashMap<String, Object> hmData = new HashMap<String, Object>();
                hmData.put(KeyData.ORDER_HISTORY_DETAIL.ORDER_ID, invoice_number.trim());
//				hmData.put(KeyData.ORDER_HISTORY_DETAIL.TARGET, ConfigCheckout.TARGET_REVIEWORDER);
                SimiData data = new SimiData(hmData);
                OrderHistoryDetailFragment fragment = OrderHistoryDetailFragment.newInstance(data);
                if (DataLocal.isTablet) {
                    SimiManager.getIntance().addFragmentSub(fragment);
                } else {
                    SimiManager.getIntance().replaceFragment(fragment);
                }
            }
        });
    }


}
