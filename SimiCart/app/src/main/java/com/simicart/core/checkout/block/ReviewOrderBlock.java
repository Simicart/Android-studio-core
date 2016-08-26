package com.simicart.core.checkout.block;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.LinearLayout;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.delegate.ReviewOrderDelegate;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;

import java.util.ArrayList;

public class ReviewOrderBlock extends SimiBlock implements ReviewOrderDelegate {

    protected LinearLayout llReviewOrder;
    protected AppCompatButton btnPlaceOrder;
    protected int topMargin = Utils.getValueDp(10);

    public ReviewOrderBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {
        llReviewOrder = (LinearLayout) id("ll_review_order");
        btnPlaceOrder = (AppCompatButton) id("btn_place_order");

        String textPlaceOrder = SimiTranslator.getInstance().translate("Place Order");
        btnPlaceOrder.setText(textPlaceOrder);
        btnPlaceOrder.setTextColor(AppColorConfig.getInstance().getContentColor());
        btnPlaceOrder.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());
    }

    @Override
    public void showView(ArrayList<View> rows) {
        llReviewOrder.removeAllViewsInLayout();
        if (null != rows && rows.size() > 0) {
            for (int i = 0; i < rows.size(); i++) {
                View row = rows.get(i);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = topMargin;
                llReviewOrder.addView(row, params);
            }
        }
    }

    public void setPlaceOrderListener(View.OnClickListener listener) {
        btnPlaceOrder.setOnClickListener(listener);
    }
}
