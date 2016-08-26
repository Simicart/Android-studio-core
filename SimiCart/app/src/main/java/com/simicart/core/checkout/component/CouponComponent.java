package com.simicart.core.checkout.component;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

/**
 * Created by frank on 6/30/16.
 */
public class CouponComponent extends SimiComponent {

    protected String mCouponCode;

    public CouponComponent(String mCouponCode) {
        super();
        this.mCouponCode = mCouponCode;
    }

    @Override
    public View createView() {
        EditText edtCoupon = new EditText(mContext);
        int padding = Utils.toDp(10);
        edtCoupon.setPadding(padding, padding, padding, padding);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = Utils.toDp(10);
        param.leftMargin = margin;
        param.rightMargin = margin;
        edtCoupon.setLayoutParams(param);
        Drawable backgroud = mContext.getResources().getDrawable(Rconfig.getInstance().drawable("core_coupon_line_border"));
        edtCoupon.setBackground(backgroud);
        edtCoupon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        edtCoupon.setHighlightColor(Color.parseColor("#b2b2b2"));
        String hintText = SimiTranslator.getInstance().translate("Enter a coupon code");
        edtCoupon.setHint(hintText);
        if (Utils.validateString(mCouponCode)) {
            edtCoupon.setText(mCouponCode);
        }
        edtCoupon.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        return edtCoupon;
    }
}
