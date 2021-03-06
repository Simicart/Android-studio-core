package com.simicart.core.common.options.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.common.Utils;
import com.simicart.core.common.options.delegate.OptionProductDelegate;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

public class CacheOptionView {

    public static boolean ADD_OPERATOR = true;
    public static boolean SUB_OPERATOR = false;

    protected CacheOption mCacheOption;
    protected View mView;
    protected RelativeLayout rlt_header;
    protected ImageView imv_arr;
    protected LinearLayout ll_body;
    protected TextView tv_name;
    protected TextView tv_required;
    protected Context mContext;
    protected OptionProductDelegate mDelegate;
    protected boolean isShowWhenStart = false;

    public CacheOptionView(CacheOption cacheOption) {
        mCacheOption = cacheOption;
        mContext = SimiManager.getIntance().getCurrentActivity();
    }

    public void setShowWhenStart(boolean isShow) {
        isShowWhenStart = isShow;
    }

    public void setDelegate(OptionProductDelegate delegate) {
        mDelegate = delegate;
    }

    public View initOptionsView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mView = inflater.inflate(
                Rconfig.getInstance().layout("core_cache_option_layout"), null,
                false);
        rlt_header = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
                .id("core_cache_option_layout_rlt_header"));
        imv_arr = (ImageView) mView.findViewById(Rconfig.getInstance().id(
                "imv_arr"));
        ll_body = (LinearLayout) mView.findViewById(Rconfig.getInstance().id(
                "core_cache_option_layout_ll_body"));
        initHeaderView();

        ll_body.setVisibility(View.GONE);
        ll_body.setBackgroundResource(Rconfig.getInstance().drawable(
                "background_option"));
        createHeader();
        createCacheOption();
        return mView;
    }

    protected void createHeader() {
        rlt_header.setBackgroundResource(Rconfig.getInstance().drawable(
                "bottom_line_border"));
        rlt_header.setBackgroundColor(Color.parseColor("#E8E8E8"));

        // name cache option
        tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv_name.setMaxEms(50);
        tv_name.setTypeface(tv_name.getTypeface(), Typeface.BOLD);
        tv_name.setTextColor(Color.parseColor("#131313"));
        tv_name.setText(mCacheOption.getOptionTitle());
        // initial required text
        if (mCacheOption.isRequired()) {
            tv_required.setText(SimiTranslator.getInstance().translate("*"));
            tv_required.setTextColor(Color.RED);
        }

        if (isShowWhenStart) {
            ll_body.setVisibility(View.VISIBLE);
            imv_arr.setRotation(90);
        } else {
            ll_body.setVisibility(View.GONE);
            imv_arr.setRotation(0);

        }
        rlt_header.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                animateOption();
            }
        });


        initHeaderView();
    }

    protected void initHeaderView() {
        try {
            tv_name = (TextView) mView.findViewById(Rconfig.getInstance().id(
                    "core_cache_option_layout_tv_name"));
            tv_required = (TextView) mView.findViewById(Rconfig.getInstance()
                    .id("core_cache_option_layout_tv_required"));
            if (mCacheOption.isRequired()) {
                tv_required.setText(SimiTranslator.getInstance().translate("*"));
                tv_required.setTextColor(Color.RED);
            }
        } catch (Exception e) {
        }
    }

    protected void animateOption() {
        if (ll_body.getVisibility() == View.VISIBLE) {
            Utils.collapse(ll_body);
            imv_arr.setRotation(0);
        } else {
            Utils.expand(ll_body);
            imv_arr.setRotation(90);
        }
    }

    protected void createCacheOption() {

    }

    protected void updatePriceHeader(String price) {
        if (tv_required.getVisibility() == View.GONE) {
            tv_required.setVisibility(View.VISIBLE);
        }

        if (price.equals("") && mCacheOption.isRequired()) {
            tv_required.setText(SimiTranslator.getInstance().translate("*"));
            tv_required.setTextColor(AppColorConfig.getInstance().getPriceColor());
            if (price.equals("") && mCacheOption.isCompleteRequired()) {
                tv_required.setText("");
            }
        } else {
            tv_required.setTextColor(AppColorConfig.getInstance().getPriceColor());
            if (mCacheOption.isDependence() && DataLocal.isCloud) {
                tv_required.setText("");
            } else {
                tv_required.setText(Html.fromHtml(price));
            }
        }
    }

    protected void updatePriceForParent(ProductOption option, boolean isAdd) {
        if (null != mDelegate) {
            mDelegate.updatePrice(option, isAdd);
        }
    }

    protected String getPrice(ProductOption option) {
        float price = option.getOptionPrice();

        String qty = option.getOptionQty();
        if (Utils.validateString(qty)) {

            float f_qty = Float.parseFloat(qty);

            if (f_qty > 1) {
                price = f_qty * price;
            }
        }

        return String.valueOf(price);
    }

    public CacheOption getCacheOption() {
        return mCacheOption;
    }

    protected String showValueOption(ProductOption option) {
        String content;
        int i_qty = 0;
        String title = "<font color='grey'>" + option.getOptionValue()
                + "</font>";

        String qty = option.getOptionQty();

        if ((null != qty) && (!qty.equals("") && (!qty.equals("null")))) {
            i_qty = (int) Float.parseFloat(qty);
            if (i_qty > 1) {
                title = "<font color='grey'>" + i_qty + " x "
                        + option.getOptionValue() + "</font>";
            }
        }

        if (option.getOptionType().equals("grouped")) {
            title = option.getOptionValue();
        }

        float f_price = option.getOptionPrice();

        String price = "<font color='red'> +"
                + AppStoreConfig.getInstance().getPrice("" + f_price) + "</font>";

        content = title + price;
        if (option.getOptionPrice() == 0
                && !AppStoreConfig.getInstance().isShowZeroPrice()) {
            content = title;
        }
        if (option.getOption_price_incl_tax() != -1) {
            String price_tax = "<font color='grey'> (</font><font color='red'>+"
                    + AppStoreConfig.getInstance().getPrice(
                    "" + option.getOption_price_incl_tax())
                    + "</font> <font color='grey'>"
                    + SimiTranslator.getInstance().translate("Incl. Tax") + ")</font>";
            if (!AppStoreConfig.getInstance().isShowZeroPrice()
                    && option.getOption_price_incl_tax() == 0) {
            } else {
                content = content + price_tax;
            }
        }
        return content;
    }

}
