package com.simicart.core.checkout.fragment;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.entity.OrderInforEntity;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.controller.AutoSignInController;

import java.util.HashMap;

public class ThankyouFragment extends SimiFragment {

    private TextView tvThankyou;
    private TextView tvOrder;
    private LinearLayout llorder;
    private TextView tvContent;
    private AppCompatButton btn_continue_shopping;
    private String content = "You will receive an order confirmation email with detail of your order and a link to track its progress";
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
        int idView = Rconfig.getInstance().layout("core_fragment_thankyou");
        View view = inflater.inflate(idView, null, false);
        view.setBackgroundColor(AppColorConfig.getInstance().getAppBackground());
        parseData();
        initView(view);
        return view;
    }

    private void initView(View rootView) {
        tvThankyou = (TextView) rootView.findViewById(Rconfig
                .getInstance().id("tv_thankyou"));
        tvThankyou.setTextColor(AppColorConfig.getInstance().getContentColor());
        tvThankyou.setText(SimiTranslator.getInstance().translate(message));

        tvContent = (TextView) rootView.findViewById(Rconfig
                .getInstance().id("tv_content"));
        tvContent.setTextColor(AppColorConfig.getInstance().getContentColor());
        tvContent.setText(SimiTranslator.getInstance().translate(content));

        btn_continue_shopping = (AppCompatButton) rootView.findViewById(Rconfig
                .getInstance().id("btn_continue_shopping"));
        btn_continue_shopping.setText(SimiTranslator.getInstance().translate("Continue Shopping"));
        btn_continue_shopping.setTextColor(AppColorConfig.getInstance().getButtonTextColor());
        btn_continue_shopping.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());
        btn_continue_shopping.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SimiManager.getIntance().backToHomeFragment();
            }
        });

        tvOrder = (TextView) rootView.findViewById(Rconfig.getInstance().id(
                "tv_order"));
        tvOrder.setTextColor(AppColorConfig.getInstance().getContentColor());
        tvOrder.setText(SimiTranslator.getInstance().translate("Your order # is:") + invoice_number);

        ImageView imageView = (ImageView) rootView.findViewById(Rconfig
                .getInstance().id("img_order"));
        imageView.setImageDrawable(AppColorConfig.getInstance().getIcon("ic_extend"));

        llorder = (LinearLayout) rootView.findViewById(Rconfig
                .getInstance().id("ll_order"));
        if (DataPreferences.isSignInComplete()) {
            llorder.setVisibility(View.VISIBLE);
        } else {
            llorder.setVisibility(View.GONE);
        }
        llorder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                HashMap<String, Object> hmData = new HashMap<String, Object>();
                hmData.put(KeyData.ORDER_HISTORY_DETAIL.ORDER_ID, invoice_number.trim());
                hmData.put(KeyData.ORDER_HISTORY_DETAIL.TARGET, 0);
                SimiManager.getIntance().openOrderHistoryDetail(hmData);

            }
        });
    }

    protected void parseData() {
        if (mHashMapData.containsKey(KeyData.THANKYOU_PAGE.ORDER_INFO_ENTITY)) {
            OrderInforEntity orderInforEntity = (OrderInforEntity) mHashMapData.get(KeyData.THANKYOU_PAGE.ORDER_INFO_ENTITY);
            invoice_number = orderInforEntity.getData("invoice_number");

            message = orderInforEntity.getMessage();
        }

        if (mHashMapData.containsKey(KeyData.THANKYOU_PAGE.PLACE_FOR)) {
            int placeFor = ((Integer) mHashMapData.get(KeyData.THANKYOU_PAGE.PLACE_FOR)).intValue();
            if (placeFor == ValueData.REVIEW_ORDER.PLACE_FOR_NEW_CUSTOMER && !DataPreferences.isSignInComplete()) {
                autoSignin();
            }
        }
    }

    private void autoSignin() {
        AutoSignInController controller = new AutoSignInController();
        controller.onStart();
    }


}
