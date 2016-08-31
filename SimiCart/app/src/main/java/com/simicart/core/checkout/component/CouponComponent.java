package com.simicart.core.checkout.component;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.delegate.CouponCodeCallBack;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

/**
 * Created by frank on 6/30/16.
 */
public class CouponComponent extends SimiComponent {

    protected String mCouponCode;
    protected CouponCodeCallBack mCallBack;

    public CouponComponent(String mCouponCode) {
        super();
        this.mCouponCode = mCouponCode;
    }

    @Override
    public View createView() {

        rootView = findLayout("core_component_coupon_code");

        EditText edtCoupon = (EditText) findView("edt_coupon_code");
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
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String code = v.getText().toString().trim();
                    if (null != mCallBack) {
                        mCallBack.applyCouponCode(code);
                    }
                    v.setFocusable(false);
                    v.setFocusableInTouchMode(true);
                    Utils.hideKeyboard(v);
                }
                return false;
            }
        });
        edtCoupon.setImeOptions(EditorInfo.IME_ACTION_DONE);
        return rootView;
    }

    public void setCallBack(CouponCodeCallBack callBack) {
        mCallBack = callBack;
    }

}
