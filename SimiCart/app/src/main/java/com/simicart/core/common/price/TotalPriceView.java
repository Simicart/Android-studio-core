package com.simicart.core.common.price;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;

@SuppressLint("UseValueOf")
public class TotalPriceView {
    protected TotalPrice mTotalPrice;
    protected Context mContext;
    protected String mColorLabel = String.valueOf(AppColorConfig.getInstance().getContentColor());
    protected int mColorPrice = AppColorConfig.getInstance().getPriceColor();
    protected int mTextSize = 16;
    protected String mSymbol;

    public TotalPriceView() {
    }

    public TotalPriceView(TotalPrice totalPrice) {
        mTotalPrice = totalPrice;
        mContext = SimiManager.getIntance().getCurrentActivity();
    }

    public String getSymbol() {
        return mSymbol;
    }

    public void setSymbol(String symbol) {
        mSymbol = symbol;
    }

    public void setColorLabel(String colorLabel) {
        mColorLabel = colorLabel;
    }

    public void setSizeText(int sizeText) {
        mTextSize = sizeText;
    }

    public View getTotalPriceView() {
        TableLayout tbl_price = new TableLayout(mContext);

        LayoutParams params = new LayoutParams();
        params.bottomMargin = Utils.toPixel(3);
        params.topMargin = Utils.toPixel(3);

        // event for top price
//        TotalPriceViewEvent event_reward_point = new TotalPriceViewEvent();
//        TotalPriceViewData data_reward_point = new TotalPriceViewData();
//        data_reward_point.setContext(mContext);
//        data_reward_point.setView(tbl_price);
//        data_reward_point.setColorLabel(mColorLabel);
//        data_reward_point.setColorPrice(mColorPrice);
//        data_reward_point.setTotalPrice(mTotalPrice);
//        data_reward_point.setSymbol(mSymbol);
//        data_reward_point.setTextSize(new Integer(mTextSize));
//        event_reward_point.dispatchEvent(
//                "com.simicart.core.common.price.TotalPriceView",
//                data_reward_point, tbl_price);

        TableRow tbr_subTotal = (TableRow) getSubTotalView();
        if (null != tbr_subTotal) {
            tbl_price.addView(tbr_subTotal, params);
        }

        TableRow tbr_subTotalIncl = (TableRow) getSubTotalInclView();
        if (null != tbr_subTotalIncl) {
            tbl_price.addView(tbr_subTotalIncl, params);
        }

        TableRow tbr_shipping = (TableRow) getShippingHandlingView();
        if (null != tbr_shipping) {
            tbl_price.addView(tbr_shipping, params);
        }

        TableRow tbr_shippingIncl = (TableRow) getShippingHandlingInclView();
        if (null != tbr_shippingIncl) {
            tbl_price.addView(tbr_shippingIncl, params);
        }

        TableRow tbr_discount = (TableRow) getDiscountView();
        if (null != tbr_discount) {
            tbl_price.addView(tbr_discount, params);
        }

        // event for affiliates coupon code
//        TotalPriceViewEvent event_affilliate = new TotalPriceViewEvent();
//        TotalPriceViewData data_affilliate = new TotalPriceViewData();
//        data_affilliate.setContext(mContext);
//        data_affilliate.setView(tbl_price);
//        data_affilliate.setColorLabel(mColorLabel);
//        data_affilliate.setColorPrice(mColorPrice);
//        data_affilliate.setTotalPrice(mTotalPrice);
//        data_affilliate.setSymbol(mSymbol);
//        data_affilliate.setTextSize(new Integer(mTextSize));
//        event_affilliate.dispatchEvent(
//                "com.simicart.core.common.price.TotalPriceView.Affilliate",
//                data_affilliate, tbl_price);

        TableRow tbr_grandTotalExcl = (TableRow) getGrandTotalExclView();
        if (null != tbr_grandTotalExcl) {
            tbl_price.addView(tbr_grandTotalExcl, params);
        }

        TableRow tbr_Tax = (TableRow) getTaxView();
        if (null != tbr_Tax) {
            tbl_price.addView(tbr_Tax, params);
        }

        if (tbr_grandTotalExcl == null) {
            TableRow tbr_grandTotal = (TableRow) getGrandTotalView();
            if (null != tbr_grandTotal) {
                tbl_price.addView(tbr_grandTotal, params);
            }
        }

        TableRow tbr_grandTotalIncl = (TableRow) getGrandTotalInclTaxView();
        if (null != tbr_grandTotalIncl) {
            tbl_price.addView(tbr_grandTotalIncl, params);
        }

        return tbl_price;

    }

    public View getSubTotalView() {
        if (mTotalPrice == null) {
            mTotalPrice = new TotalPrice();
        }
        String subTotal = mTotalPrice.getSubTotalOrderHis();
        String subTotalExcl = mTotalPrice.getSubtotalExclTax();

        TableRow tbr_subTotal = new TableRow(mContext);
        if (null != subTotalExcl && !subTotalExcl.equals("null")
                && !subTotalExcl.equals("")) {

            String label = "<font color='" + mColorLabel + "'>"
                    + SimiTranslator.getInstance().translate("SubTotal") + " ("
                    + SimiTranslator.getInstance().translate("Excl.Tax") + ")"
                    + ": </font>";
            String price = "<font color='" + mColorPrice + "'>"
                    + AppStoreConfig.getInstance().getPrice(subTotalExcl) + "</font>";
            if (null != mSymbol) {
                price = "<font color='" + mColorPrice + "'>"
                        + AppStoreConfig.getInstance().getPrice(subTotalExcl, mSymbol)
                        + "</font>";
            }
            TextView tv_label = (TextView) showView(label);
            TextView tv_price = (TextView) showView(price);
            if (AppStoreConfig.getInstance().isRTL()) {
                tbr_subTotal.addView(tv_price);
                tbr_subTotal.addView(tv_label);
            } else {
                tbr_subTotal.addView(tv_label);
                tbr_subTotal.addView(tv_price);
            }
        } else {
            String label = "<font color='" + mColorLabel + "'>"
                    + SimiTranslator.getInstance().translate("SubTotal") + ": </font>";
            String price = "<font color='" + mColorPrice + "'>"
                    + AppStoreConfig.getInstance().getPrice(subTotal) + "</font>";
            if (null != mSymbol) {
                price = "<font color='" + mColorPrice + "'>"
                        + AppStoreConfig.getInstance().getPrice(subTotal, mSymbol)
                        + "</font>";
            }
            TextView tv_label = (TextView) showView(label);
            TextView tv_price = (TextView) showView(price);
            if (AppStoreConfig.getInstance().isRTL()) {
                tbr_subTotal.addView(tv_price);
                tbr_subTotal.addView(tv_label);
            } else {
                tbr_subTotal.addView(tv_label);
                tbr_subTotal.addView(tv_price);
            }
        }
        return tbr_subTotal;
    }

    public View getSubTotalInclView() {
        String subtotalIncl = mTotalPrice.getSubtotalInclTax();
        if (null != subtotalIncl && !subtotalIncl.equals("")
                && !subtotalIncl.equals("null")) {
            TableRow tbr_subTotalIncl = new TableRow(mContext);
            String label = "<font color='" + mColorLabel + "'>"
                    + SimiTranslator.getInstance().translate("SubTotal") + " ("
                    + SimiTranslator.getInstance().translate("Incl.Tax") + ")"
                    + ": </font>";
            String price = "<font color='" + mColorPrice + "'>"
                    + AppStoreConfig.getInstance().getPrice(subtotalIncl) + "</font>";

            if (null != mSymbol) {
                price = "<font color='" + mColorPrice + "'>"
                        + AppStoreConfig.getInstance().getPrice(subtotalIncl, mSymbol)
                        + "</font>";
            }

            TextView tv_label = (TextView) showView(label);
            TextView tv_price = (TextView) showView(price);
            if (AppStoreConfig.getInstance().isRTL()) {
                tbr_subTotalIncl.addView(tv_price);
                tbr_subTotalIncl.addView(tv_label);
            } else {
                tbr_subTotalIncl.addView(tv_label);
                tbr_subTotalIncl.addView(tv_price);
            }
            return tbr_subTotalIncl;
        }

        return null;
    }

    public View getShippingHandlingView() {
        String shippingTotal = mTotalPrice.getShippingHandling();
        String shippingTotalExcl = mTotalPrice.getShippingHandlingExclTax();
        TableRow tbr_shippingTotal = new TableRow(mContext);
        if (null != shippingTotalExcl && !shippingTotalExcl.equals("null")
                && !shippingTotalExcl.equals("")
                && !shippingTotalExcl.equals("0")) {
            String label = "<font color='" + mColorLabel + "'>"
                    + SimiTranslator.getInstance().translate("Shipping & Handling")
                    + " (" + SimiTranslator.getInstance().translate("Excl.Tax") + ")"
                    + ": </font>";
            String price = "<font color='" + mColorPrice + "'>"
                    + AppStoreConfig.getInstance().getPrice(shippingTotalExcl)
                    + "</font>";
            if (null != mSymbol) {
                price = "<font color='"
                        + mColorPrice
                        + "'>"
                        + AppStoreConfig.getInstance().getPrice(shippingTotalExcl,
                        mSymbol) + "</font>";
            }

            TextView tv_label = (TextView) showView(label);
            TextView tv_price = (TextView) showView(price);
            if (AppStoreConfig.getInstance().isRTL()) {
                tbr_shippingTotal.addView(tv_price);
                tbr_shippingTotal.addView(tv_label);
            } else {
                tbr_shippingTotal.addView(tv_label);
                tbr_shippingTotal.addView(tv_price);
            }
            return tbr_shippingTotal;
        } else {
            if (shippingTotal != null && !shippingTotal.equals("0")) {

                String label = "<font color='" + mColorLabel + "'>"
                        + SimiTranslator.getInstance().translate("Shipping & Handling")
                        + ": </font>";
                String price = "<font color='" + mColorPrice + "'>"
                        + AppStoreConfig.getInstance().getPrice(shippingTotal)
                        + "</font>";

                if (null != mSymbol) {
                    price = "<font color='"
                            + mColorPrice
                            + "'>"
                            + AppStoreConfig.getInstance().getPrice(shippingTotal,
                            mSymbol) + "</font>";
                }

                TextView tv_label = (TextView) showView(label);
                tv_label.setTypeface(null, Typeface.BOLD);
                TextView tv_price = (TextView) showView(price);
                tv_price.setTypeface(null, Typeface.BOLD);
                if (AppStoreConfig.getInstance().isRTL()) {
                    tbr_shippingTotal.addView(tv_price);
                    tbr_shippingTotal.addView(tv_label);
                } else {
                    tbr_shippingTotal.addView(tv_label);
                    tbr_shippingTotal.addView(tv_price);
                }
                return tbr_shippingTotal;
            }
        }
        return null;
    }

    public View getShippingHandlingInclView() {
        String shippingHandlingIncl = mTotalPrice
                .getShippingHandlingInclTax();
        if (null != shippingHandlingIncl && !shippingHandlingIncl.equals("")
                && !shippingHandlingIncl.equals("null")
                && !shippingHandlingIncl.equals("0")) {
            TableRow tbr_shippingHandlingIncl = new TableRow(mContext);
            String label = "<font color='" + mColorLabel + "'>"
                    + SimiTranslator.getInstance().translate("Shipping & Handling")
                    + " (" + SimiTranslator.getInstance().translate("Incl.Tax") + ")"
                    + ": </font>";
            String price = "<font color='" + mColorPrice + "'>"
                    + AppStoreConfig.getInstance().getPrice(shippingHandlingIncl)
                    + "</font>";
            if (null != mSymbol) {
                price = "<font color='"
                        + mColorPrice
                        + "'>"
                        + AppStoreConfig.getInstance().getPrice(shippingHandlingIncl,
                        mSymbol) + "</font>";
            }
            TextView tv_label = (TextView) showView(label);
            TextView tv_price = (TextView) showView(price);
            if (AppStoreConfig.getInstance().isRTL()) {
                tbr_shippingHandlingIncl.addView(tv_price);
                tbr_shippingHandlingIncl.addView(tv_label);
            } else {
                tbr_shippingHandlingIncl.addView(tv_label);
                tbr_shippingHandlingIncl.addView(tv_price);
            }
            return tbr_shippingHandlingIncl;
        }

        return null;
    }

    public View getGrandTotalExclView() {

        String grandTotalExcl = mTotalPrice.getGrandTotalExclTax();

        if (null != grandTotalExcl && !grandTotalExcl.equals("null")
                && !grandTotalExcl.equals("")) {
            TableRow tbr_grandTotal = new TableRow(mContext);
            String label = "<font color='" + mColorLabel + "'>"
                    + SimiTranslator.getInstance().translate("Grand Total") + " ("
                    + SimiTranslator.getInstance().translate("Excl.Tax") + ")"
                    + ": </font>";
            String price = "<font color='" + mColorPrice + "'>"
                    + AppStoreConfig.getInstance().getPrice(grandTotalExcl) + "</font>";

            if (null != mSymbol) {
                price = "<font color='"
                        + mColorPrice
                        + "'>"
                        + AppStoreConfig.getInstance()
                        .getPrice(grandTotalExcl, mSymbol) + "</font>";
            }

            TextView tv_label = (TextView) showView(label);
            tv_label.setTypeface(null, Typeface.BOLD);
            TextView tv_price = (TextView) showView(price);
            tv_price.setTypeface(null, Typeface.BOLD);
            if (AppStoreConfig.getInstance().isRTL()) {
                tbr_grandTotal.addView(tv_price);
                tbr_grandTotal.addView(tv_label);
            } else {
                tbr_grandTotal.addView(tv_label);
                tbr_grandTotal.addView(tv_price);
            }
            return tbr_grandTotal;
        }
        return null;
    }

    public View getGrandTotalView() {
        String grandTotal = mTotalPrice.getGrandTotal();

        if (null != grandTotal && !grandTotal.equals("")
                && !grandTotal.equals("null")) {
            TableRow tbr_grandTotal = new TableRow(mContext);
            String label = "<font color='" + mColorLabel + "'>"
                    + SimiTranslator.getInstance().translate("Grand Total") + ": </font>";
            String price = "<font color='" + mColorPrice + "'>"
                    + AppStoreConfig.getInstance().getPrice(grandTotal) + "</font>";

            if (null != mSymbol) {
                price = "<font color='" + mColorPrice + "'>"
                        + AppStoreConfig.getInstance().getPrice(grandTotal, mSymbol)
                        + "</font>";
            }

            TextView tv_label = (TextView) showView(label);
            tv_label.setTypeface(null, Typeface.BOLD);
            TextView tv_price = (TextView) showView(price);
            tv_price.setTypeface(null, Typeface.BOLD);
            if (AppStoreConfig.getInstance().isRTL()) {
                tbr_grandTotal.addView(tv_price);
                tbr_grandTotal.addView(tv_label);
            } else {
                tbr_grandTotal.addView(tv_label);
                tbr_grandTotal.addView(tv_price);
            }
            return tbr_grandTotal;
        }
        return null;
    }

    public View getGrandTotalInclTaxView() {
        String grandTotalIncl = mTotalPrice.getGrandTotalInclTax();
        if (null != grandTotalIncl && !grandTotalIncl.equals("")
                && !grandTotalIncl.equals("null")) {
            TableRow tbr_grandTotalIncl = new TableRow(mContext);
            String label = "<font color='" + mColorLabel + "'>"
                    + SimiTranslator.getInstance().translate("Grand Total") + " ("
                    + SimiTranslator.getInstance().translate("Incl.Tax") + ")"
                    + ": </font>";
            String price = "<font color='" + mColorPrice + "'>"
                    + AppStoreConfig.getInstance().getPrice(grandTotalIncl) + "</font>";
            if (null != mSymbol) {
                price = "<font color='"
                        + mColorPrice
                        + "'>"
                        + AppStoreConfig.getInstance()
                        .getPrice(grandTotalIncl, mSymbol) + "</font>";
            }

            TextView tv_label = (TextView) showView(label);
            tv_label.setTypeface(null, Typeface.BOLD);
            TextView tv_price = (TextView) showView(price);
            tv_price.setTypeface(null, Typeface.BOLD);
            if (AppStoreConfig.getInstance().isRTL()) {
                tbr_grandTotalIncl.addView(tv_price);
                tbr_grandTotalIncl.addView(tv_label);
            } else {
                tbr_grandTotalIncl.addView(tv_label);
                tbr_grandTotalIncl.addView(tv_price);
            }
            return tbr_grandTotalIncl;
        }

        return null;
    }

    public View getTaxView() {
        String tax = mTotalPrice.getTax();
        if (tax != null && !tax.equals("0") && !tax.equals("null")) {
            TableRow tbr_Tax = new TableRow(mContext);
            String label = "<font color='" + mColorLabel + "'>"
                    + SimiTranslator.getInstance().translate("Tax") + ": </font>";
            String price = "<font color='" + mColorPrice + "'>"
                    + AppStoreConfig.getInstance().getPrice(tax) + "</font>";

            if (null != mSymbol) {
                price = "<font color='" + mColorPrice + "'>"
                        + AppStoreConfig.getInstance().getPrice(tax, mSymbol)
                        + "</font>";
            }
            Log.e("TotalPriceView ", label + price);

            TextView tv_label = (TextView) showView(label);
            TextView tv_price = (TextView) showView(price);
            if (AppStoreConfig.getInstance().isRTL()) {
                tbr_Tax.addView(tv_price);
                tbr_Tax.addView(tv_label);
            } else {
                tbr_Tax.addView(tv_label);
                tbr_Tax.addView(tv_price);
            }
            return tbr_Tax;
        }

        return null;
    }

    public View getDiscountView() {
        String discount = mTotalPrice.getDiscount();
        if (discount != null && !discount.equals("0")
                && !discount.equals("null")) {
            TableRow tbr_discount = new TableRow(mContext);
            String label = "<font color='" + mColorLabel + "'>"
                    + SimiTranslator.getInstance().translate("Discount") + ": </font>";
            String price = "<font color='" + mColorPrice + "'>" + "-"
                    + AppStoreConfig.getInstance().getPrice(discount) + "</font>";
            if (null != mSymbol) {
                price = "<font color='" + mColorPrice + "'>" + ""
                        + AppStoreConfig.getInstance().getPrice(discount, mSymbol)
                        + "</font>";
            }

            TextView tv_label = (TextView) showView(label);
            TextView tv_price = (TextView) showView(price);
            if (AppStoreConfig.getInstance().isRTL()) {
                tbr_discount.addView(tv_price);
                tbr_discount.addView(tv_label);
            } else {
                tbr_discount.addView(tv_label);
                tbr_discount.addView(tv_price);
            }

            return tbr_discount;
        }

        return null;
    }

    protected View showView(String content) {
        TextView tv_price = new TextView(mContext);
        tv_price.setGravity(Gravity.RIGHT);
        tv_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        tv_price.setText(Html.fromHtml(content));
        return tv_price;
    }
}
