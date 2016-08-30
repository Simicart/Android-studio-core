package com.simicart.core.checkout.block;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatButton;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.simicart.core.adapter.CartListAdapter;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.controller.CartListenerController;
import com.simicart.core.checkout.controller.PopupCheckoutController;
import com.simicart.core.checkout.delegate.CartDelegate;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.common.price.TotalPriceView;
import com.simicart.core.config.AppCheckoutConfig;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.material.ButtonRectangle;

public class CartBlock extends SimiBlock implements CartDelegate {

    protected LinearLayout llCart;
    protected AppCompatButton btn_Checkout;
    protected TableLayout layoutPrice;
    protected CartListAdapter mAdapter;
    protected CartListenerController mListenerController;
    protected PopupCheckoutController mPCheckoutController;

    protected ProgressDialog pp_checkout;
    protected TextView tv_cancel;
    protected TextView tv_excustomer;
    protected TextView tv_newcustomer;
    protected TextView tv_guest;

    private View line_price;
    private View line_bottom;
    private View line_vertical;

    protected static String message = "";

    public CartBlock(View view, Context context) {
        super(view, context);
        mListenerController = new CartListenerController();
        mListenerController.setCartDelegate(this);
        mPCheckoutController = new PopupCheckoutController();
        mPCheckoutController.setDelegate(this);
    }

    @Override
    public void initView() {
        // check out button
        btn_Checkout = (AppCompatButton) mView.findViewById(Rconfig
                .getInstance().id("checkout"));
        btn_Checkout.setText(SimiTranslator.getInstance().translate("Checkout"));
        btn_Checkout.setTextColor(AppColorConfig.getInstance().getButtonTextColor());
        btn_Checkout.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());
        btn_Checkout.setTextSize(Constants.SIZE_TEXT_BUTTON);
        btn_Checkout.setOnClickListener(mListenerController
                .getCheckoutClicker());

        // list product
        llCart = (LinearLayout) mView.findViewById(Rconfig.getInstance().id(
                "ll_carts"));

        // price
        layoutPrice = (TableLayout) mView.findViewById(Rconfig.getInstance()
                .id("ll_pricetotal"));
        if (DataLocal.isTablet) {
            line_price = mView.findViewById(Rconfig.getInstance().id(
                    "line_price"));
            line_bottom = mView.findViewById(Rconfig.getInstance().id(
                    "line_bottom"));
            line_vertical = mView.findViewById(Rconfig.getInstance().id(
                    "line_vertical"));
//            Utils.changeColorLine(line_price);
//            Utils.changeColorLine(line_bottom);
//            Utils.changeColorLine(line_vertical);
        }
    }

    @Override
    public void drawView(SimiCollection collection) {
        mListenerController.setmMessage(message);
    }

    protected boolean isShown() {
        return mView.isShown();
    }

    @Override
    public void onUpdateTotalPrice(TotalPrice totalPrice) {
        setPriceView(totalPrice);
    }

    protected void setPriceView(TotalPrice totalPrice) {
        TotalPriceView viewPrice = new TotalPriceView(totalPrice);
        View view = viewPrice.getTotalPriceView();
        if (null != view) {
            layoutPrice.removeAllViews();
            layoutPrice.addView(view);
        }
    }

    public void setCheckoutClicker(OnClickListener clicker) {
        btn_Checkout.setOnClickListener(clicker);
    }

    public void visiableView() {
        ((ViewGroup) mView).removeAllViewsInLayout();
        TextView tv_notify = new TextView(mContext);
        tv_notify.setTextColor(AppColorConfig.getInstance().getContentColor());
        tv_notify.setText(SimiTranslator.getInstance().translate(
                "Your shopping cart is empty"));
        tv_notify.setTypeface(null, Typeface.BOLD);
        if (DataLocal.isTablet) {
            tv_notify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        } else {
            tv_notify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        tv_notify.setGravity(Gravity.CENTER);
        tv_notify.setLayoutParams(params);
        ((ViewGroup) mView).addView(tv_notify);
    }

    // Popup select option checkout when the customer haven't logged in
    public void createPopupMenu() {
        // if (DataLocal.isTablet) {
        // SimiManager.getIntance().removeDialog();
        // }

        pp_checkout = ProgressDialog.show(mView.getContext(), null, null, true,
                false);
        pp_checkout.setContentView(Rconfig.getInstance().layout(
                "core_popup_checkout_layout"));
        pp_checkout.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pp_checkout.setCanceledOnTouchOutside(false);
        mPCheckoutController.onStart();
        pp_checkout.show();

        tv_cancel = (TextView) pp_checkout.findViewById(Rconfig.getInstance()
                .id("method_cancel"));
        tv_cancel.setText(SimiTranslator.getInstance().translate("Cancel"));
        tv_cancel.setOnTouchListener(mPCheckoutController.getOnCancel());

        tv_excustomer = (TextView) pp_checkout.findViewById(Rconfig
                .getInstance().id("method_excustomer"));
        tv_excustomer.setText(SimiTranslator.getInstance().translate(
                "Checkout as existing customer"));
        tv_excustomer
                .setOnTouchListener(mPCheckoutController.getOnExcustomer());

        tv_newcustomer = (TextView) pp_checkout.findViewById(Rconfig
                .getInstance().id("method_newcustomer"));
        tv_newcustomer.setText(SimiTranslator.getInstance().translate(
                "Checkout as new customer"));
        tv_newcustomer.setOnTouchListener(mPCheckoutController
                .getOnNewcustomer());

        tv_guest = (TextView) pp_checkout.findViewById(Rconfig.getInstance()
                .id("method_guest"));
        if (AppCheckoutConfig.getInstance().isGuestCheckout() == true) {
            tv_guest.setText(SimiTranslator.getInstance().translate("Checkout as guest"));
            tv_guest.setOnTouchListener(mPCheckoutController.getOnAsguest());
        } else {
            tv_guest.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPopupCheckout() {
        createPopupMenu();
        pp_checkout.show();
    }

    @Override
    public void dismissPopupCheckout() {
        pp_checkout.dismiss();
    }

    @Override
    public void setMessage(String message) {
        CartBlock.message = message;
        mListenerController.setmMessage(message);
    }

    @Override
    public void setCheckoutWebView(String url) {
        mListenerController.setWebviewUrl(url);
    }

    @Override
    public void showListProductsView(View view) {
        if (null != view) {
            llCart.removeAllViews();
            llCart.addView(view);
        }
    }

    @Override
    public void visibleAllView() {
        visiableView();
    }

}
