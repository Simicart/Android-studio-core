package com.simicart.core.checkout.component;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.delegate.PaymentMethodCallBack;
import com.simicart.core.checkout.entity.PaymentMethodEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

/**
 * Created by frank on 11/05/2016.
 */
public class PaymentMethodComponent extends SimiComponent implements PaymentMethodCallBack {

    protected LinearLayout ll_core_component;
    protected TextView tv_title;
    protected int topMargin = Utils.getValueDp(5);
    protected PaymentMethodCallBack mCallBack;
    protected ArrayList<PaymentMethodEntity> mPaymentMethods;
    protected ArrayList<ItemPaymentMethodView> mItemViews;
    /**
     * This is the  payment that user selected.
     */
    protected PaymentMethodEntity mCurrentPayment;

    public PaymentMethodComponent(ArrayList<PaymentMethodEntity> mPaymentMethods) {
        super();
        this.mPaymentMethods = mPaymentMethods;

    }

    @Override
    public View createView() {
        rootView = mInflater.inflate(Rconfig.getInstance().layout("core_component_layout"), null, false);
        intView();
        return rootView;
    }

    protected void intView() {
        tv_title = (TextView) findView("tv_title");
        String title = SimiTranslator.getInstance().translate("Payment Method");
        tv_title.setText(title);
        int bgColor = AppColorConfig.getInstance().getSectionColor();
        tv_title.setBackgroundColor(bgColor);
        int bgText = AppColorConfig.getInstance().getContentColor();
        tv_title.setTextColor(bgText);

        ll_core_component = (LinearLayout) findView("ll_body_component");
        createRows();
    }

    protected void createRows() {
        ll_core_component.removeAllViewsInLayout();
        if (null != mPaymentMethods && mPaymentMethods.size() > 0) {
            mItemViews = new ArrayList<>();
            for (int i = 0; i < mPaymentMethods.size(); i++) {
                PaymentMethodEntity payment = mPaymentMethods.get(i);
                ItemPaymentMethodView itemView = new ItemPaymentMethodView(payment);
                itemView.setCallBack(this);
                View view = itemView.createView();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = topMargin;
                ll_core_component.addView(view);
                mItemViews.add(itemView);
            }
        }
    }

    public View updateView(ArrayList<PaymentMethodEntity> paymentMethods) {
        mPaymentMethods = paymentMethods;
        return createView();
    }

    @Override
    public void onSelectItem(PaymentMethodEntity payment) {
        if (null != mCallBack) {
            mCurrentPayment = payment;
            mCallBack.onSelectItem(payment);
        }
    }

    @Override
    public void onEditAction(PaymentMethodEntity payment) {
        if (null != mCallBack) {
            mCurrentPayment = payment;
            mCallBack.onEditAction(payment);
        }
    }

    public void setListPaymentMethod(ArrayList<PaymentMethodEntity> paymentMethods) {
        mPaymentMethods = paymentMethods;
    }



    public void setCallBack(PaymentMethodCallBack callBack) {
        mCallBack = callBack;
    }

    public PaymentMethodEntity getCurrentPayment() {
        return mCurrentPayment;
    }

}
