package com.simicart.core.checkout.component;

import android.view.View;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.config.Rconfig;

/**
 * Created by frank on 6/29/16.
 */
public class TotalPriceComponent extends SimiComponent {

    protected TotalPrice mTotalEntity;

    public TotalPriceComponent(TotalPrice mTotalEntity) {
        super();
        this.mTotalEntity = mTotalEntity;
    }

    @Override
    public View createView() {

        rootView = mInflater.inflate(Rconfig.getInstance().layout("core_component_layout"), null, false);
        findView("tv_title").setVisibility(View.GONE);
//        initView();

        return rootView;
    }

//    protected void initView() {
//
//        LinearLayout ll_core_component = (LinearLayout) findView("ll_body_component");
//        ll_core_component.setGravity(Gravity.RIGHT);
//
//        TableLayout tblPrice = new TableLayout(mContext);
//        TableLayout.LayoutParams param = new TableLayout.LayoutParams();
//        param.bottomMargin = Utils.toDp(5);
//        param.topMargin = Utils.toDp(5);
//        param.rightMargin = Utils.toDp(10);
//        tblPrice.setLayoutParams(param);
//        tblPrice.setGravity(Gravity.RIGHT);
//        ll_core_component.addView(tblPrice);
//
//
//        // sub total
//        String subTotalExcl = mTotalEntity.getSubTotalExclTax();
//        String subTotalIncl = mTotalEntity.getSubToalInclTax();
//        float fSubTotalExcl = convertToFloat(subTotalExcl);
//        float fSubTotalIncl = convertToFloat(subTotalIncl);
//        if (fSubTotalExcl >= 0 || fSubTotalIncl >= 0) {
//            if (fSubTotalExcl >= 0) {
//                TableRow tbrSubTotalExcl = createSubtotalExcl();
//                tblPrice.addView(tbrSubTotalExcl);
//            }
//
//            if (fSubTotalIncl >= 0) {
//                TableRow tbrSubTotalIncl = createSubtotalIncl();
//                tblPrice.addView(tbrSubTotalIncl);
//            }
//        } else {
//            TableRow tbrSubtotal = createSubtotal();
//            tblPrice.addView(tbrSubtotal);
//        }
//
//        // shipping handling
//        String shippingExcl = mTotalEntity.getShippingExclTax();
//        String shippingIncl = mTotalEntity.getShippingInclTax();
//        float fShippingExcl = convertToFloat(shippingExcl);
//        float fShippingIncl = convertToFloat(shippingIncl);
//        if (fShippingExcl >= 0 || fShippingIncl >= 0) {
//
//            if (fShippingExcl >= 0) {
//                TableRow tbrShippingExcl = createShippingHandlingExcl();
//                tblPrice.addView(tbrShippingExcl);
//            }
//
//            if (fShippingIncl >= 0) {
//                TableRow tbrShippingIncl = createShippingHandlingIncl();
//                tblPrice.addView(tbrShippingIncl);
//            }
//
//        } else {
//            TableRow tbrShipping = createShippingHandling();
//            if (null != tbrShipping) {
//                tblPrice.addView(tbrShipping);
//            }
//        }
//
//        // discount
//        String discount = mTotalEntity.getDiscount();
//        float fDiscount = convertToFloat(discount);
//        if (fDiscount > 0) {
//            TableRow tbrDiscount = createDiscount();
//            tblPrice.addView(tbrDiscount);
//        }
//
//        // tax
//        String tax = mTotalEntity.getTax();
//        float fTax = convertToFloat(tax);
//        if (fTax > 0) {
//            TableRow tbrTax = createTax();
//            tblPrice.addView(tbrTax);
//        }
//
//        // grand total
//        String grandTotalExcl = mTotalEntity.getGrandTotalExclTax();
//        String grandTotalIncl = mTotalEntity.getGrandTotalInclTax();
//        float fGrandTotalExcl = convertToFloat(grandTotalExcl);
//        float fGrandTotalIncl = convertToFloat(grandTotalIncl);
//        if (fGrandTotalExcl >= 0 && fGrandTotalIncl >= 0) {
//
//            if (fGrandTotalExcl >= 0) {
//                TableRow tbrGrandTotalExcl = createGrandtotalExcl();
//                tblPrice.addView(tbrGrandTotalExcl);
//            }
//
//            if (fGrandTotalIncl >= 0) {
//                TableRow tbrGrandTotalIncl = createGrandtotalIncl();
//                tblPrice.addView(tbrGrandTotalIncl);
//            }
//
//        } else {
//            TableRow tbrGrandTotal = createGrandTotal();
//            if (null != tbrGrandTotal) {
//                tblPrice.addView(tbrGrandTotal);
//            }
//        }
//
//    }
//
//    protected TableRow createSubtotalExcl() {
//        String subTotalExcl = mTotalEntity.getSubTotalExclTax();
//        return createRow("Subtotal(Excl.Tax)", subTotalExcl);
//    }
//
//    protected TableRow createSubtotalIncl() {
//        String subTotalIncl = mTotalEntity.getSubToalInclTax();
//        return createRow("Subtotal(Incl.Tax", subTotalIncl);
//    }
//
//    protected TableRow createSubtotal() {
//        String subTotal = mTotalEntity.getSubtotal();
//        return createRow("Subtotal", subTotal);
//    }
//
//    protected TableRow createShippingHandling() {
//        String shipping = mTotalEntity.getShippingHand();
//        float fShipping = convertToFloat(shipping);
//        if (fShipping >= 0) {
//            return createRow("Shipping and Handling", shipping);
//        }
//        return null;
//    }
//
//    protected TableRow createShippingHandlingExcl() {
//        String shippingExcl = mTotalEntity.getShippingExclTax();
//        return createRow("Shipping Excl.Tax", shippingExcl);
//    }
//
//    protected TableRow createShippingHandlingIncl() {
//        String shippingIncl = mTotalEntity.getShippingInclTax();
//        return createRow("Shipping Incl.Tax", shippingIncl);
//    }
//
//    protected TableRow createDiscount() {
//        String discount = mTotalEntity.getDiscount();
//        return createRow("Discount", discount);
//    }
//
//    protected TableRow createTax() {
//        String tax = mTotalEntity.getTax();
//        return createRow("Tax", tax);
//    }
//
//    protected TableRow createGrandtotalExcl() {
//        String grandTotalExcl = mTotalEntity.getGrandTotalExclTax();
//        return createRow("Grand Total(Excl.Tax)", grandTotalExcl);
//    }
//
//    protected TableRow createGrandtotalIncl() {
//        String grandTotalIncl = mTotalEntity.getGrandTotalInclTax();
//        return createRow("Grand Total(Incl.Tax", grandTotalIncl);
//    }
//
//    protected TableRow createGrandTotal() {
//        String grandTotal = mTotalEntity.getGrandTotal();
//        float fGrandTotal = convertToFloat(grandTotal);
//        if (fGrandTotal >= 0) {
//            return createRow("Grand Total", grandTotal);
//        }
//        return null;
//    }
//
//    protected TableRow createRow(String label, String price) {
//        TableRow tbrPrice = new TableRow(mContext);
//        tbrPrice.setGravity(Gravity.RIGHT);
//        TableLayout.LayoutParams param = new TableLayout.LayoutParams();
//        param.topMargin = Utils.toDp(3);
//
//        tbrPrice.setLayoutParams(param);
//
//
//        String trslLabel = SimiLanguage.getInstance().translateText(label);
//        TextView tvLabel = createLabel(trslLabel);
//        tbrPrice.addView(tvLabel);
//
//        TextView tvPrice = createPrice(price);
//        tbrPrice.addView(tvPrice);
//
//        return tbrPrice;
//    }
//
//    protected TextView createLabel(String label) {
//        TextView tvLabel = new TextView(mContext);
//        tvLabel.setGravity(Gravity.RIGHT);
//        tvLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//        String htmlLabel = Html.fromHtml(label) + ": ";
//        tvLabel.setText(htmlLabel);
//        return tvLabel;
//    }
//
//    protected TextView createPrice(String price) {
//        TextView tvPrice = new TextView(mContext);
//        tvPrice.setGravity(Gravity.RIGHT);
//        tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//        String htmlPrice = StoreViewBaseEntity.getInstance().getPrice(price);
//        tvPrice.setText(Html.fromHtml(htmlPrice));
//        return tvPrice;
//    }
//
//    protected float convertToFloat(String price) {
//        try {
//            return Float.parseFloat(price);
//        } catch (Exception e) {
//            return -1;
//        }
//    }


}
