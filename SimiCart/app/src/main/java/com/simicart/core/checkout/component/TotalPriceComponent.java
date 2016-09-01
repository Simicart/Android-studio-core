package com.simicart.core.checkout.component;

import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by frank on 6/29/16.
 */
public class TotalPriceComponent extends SimiComponent {

    protected TotalPrice mTotalEntity;
    protected TableLayout tblPrice;
    protected ArrayList<TableRow> listRows;

    public TotalPriceComponent(TotalPrice mTotalEntity) {
        super();
        this.mTotalEntity = mTotalEntity;
    }

    @Override
    public View createView() {

        rootView = mInflater.inflate(Rconfig.getInstance().layout("core_component_layout"), null, false);
        findView("tv_title").setVisibility(View.GONE);
        initView();

        return rootView;
    }

    protected void initView() {
        listRows = new ArrayList<>();

        LinearLayout ll_core_component = (LinearLayout) findView("ll_body_component");
        ll_core_component.setGravity(Gravity.RIGHT);
        ll_core_component.removeAllViewsInLayout();

        tblPrice = new TableLayout(mContext);
        TableLayout.LayoutParams param = new TableLayout.LayoutParams();
        param.bottomMargin = Utils.toDp(5);
        param.topMargin = Utils.toDp(5);
        param.rightMargin = Utils.toDp(10);
        tblPrice.setLayoutParams(param);
        tblPrice.setGravity(Gravity.RIGHT);
        ll_core_component.addView(tblPrice);

        initSubTotal();

        initShippingHandling();

        initDiscount();

        initTax();

        initGrandTotal();

        initCustomRow();

        showTotalPrice();

    }

    protected void initSubTotal() {
        String subTotalExcl = mTotalEntity.getSubtotalExclTax();
        String subTotalIncl = mTotalEntity.getSubtotalInclTax();
        float fSubTotalExcl = convertToFloat(subTotalExcl);
        float fSubTotalIncl = convertToFloat(subTotalIncl);
        if (fSubTotalExcl >= 0 || fSubTotalIncl >= 0) {
            if (fSubTotalExcl >= 0) {
                TableRow tbrSubTotalExcl = createSubtotalExcl();
                listRows.add(tbrSubTotalExcl);
            }

            if (fSubTotalIncl >= 0) {
                TableRow tbrSubTotalIncl = createSubtotalIncl();
                listRows.add(tbrSubTotalIncl);
            }
        } else {
            TableRow tbrSubtotal = createSubtotal();
            listRows.add(tbrSubtotal);
        }
    }

    protected void initShippingHandling() {
        String shippingExcl = mTotalEntity.getShippingHandlingExclTax();
        String shippingIncl = mTotalEntity.getShippingHandlingInclTax();
        float fShippingExcl = convertToFloat(shippingExcl);
        float fShippingIncl = convertToFloat(shippingIncl);
        if (fShippingExcl >= 0 || fShippingIncl >= 0) {

            if (fShippingExcl >= 0) {
                TableRow tbrShippingExcl = createShippingHandlingExcl();
                listRows.add(tbrShippingExcl);
            }

            if (fShippingIncl >= 0) {
                TableRow tbrShippingIncl = createShippingHandlingIncl();
                listRows.add(tbrShippingIncl);
            }

        } else {
            TableRow tbrShipping = createShippingHandling();
            if (null != tbrShipping) {
                listRows.add(tbrShipping);
            }
        }
    }

    protected void initDiscount() {
        String discount = mTotalEntity.getDiscount();
        float fDiscount = convertToFloat(discount);
        if (fDiscount > 0) {
            TableRow tbrDiscount = createDiscount();
            listRows.add(tbrDiscount);
        }
    }

    protected void initTax() {
        String tax = mTotalEntity.getTax();
        float fTax = convertToFloat(tax);
        if (fTax > 0) {
            TableRow tbrTax = createTax();
            listRows.add(tbrTax);
        }
    }

    protected void initGrandTotal() {
        String grandTotalExcl = mTotalEntity.getGrandTotalExclTax();
        String grandTotalIncl = mTotalEntity.getGrandTotalInclTax();
        float fGrandTotalExcl = convertToFloat(grandTotalExcl);
        float fGrandTotalIncl = convertToFloat(grandTotalIncl);
        if (fGrandTotalExcl >= 0 && fGrandTotalIncl >= 0) {

            if (fGrandTotalExcl >= 0) {
                TableRow tbrGrandTotalExcl = createGrandtotalExcl();
                listRows.add(tbrGrandTotalExcl);
            }

            if (fGrandTotalIncl >= 0) {
                TableRow tbrGrandTotalIncl = createGrandtotalIncl();
                listRows.add(tbrGrandTotalIncl);
            }

        } else {
            TableRow tbrGrandTotal = createGrandTotal();
            if (null != tbrGrandTotal) {
                listRows.add(tbrGrandTotal);
            }
        }
    }

    protected void initCustomRow() {
        HashMap<String,Object> hmData = new HashMap<>();
        hmData.put(KeyData.TOTAL_PRICE.LIST_ROWS, listRows);
        hmData.put(KeyData.TOTAL_PRICE.JSON_DATA, mTotalEntity.getJSONObject());
        SimiEvent.dispatchEvent(KeyEvent.TOTAL_PRICE_EVENT.TOTAL_PRICE_ADD_ROW, hmData);
    }

    protected void showTotalPrice() {
        for(TableRow row : listRows) {
            tblPrice.addView(row);
        }
    }

    protected TableRow createSubtotalExcl() {
        String subTotalExcl = mTotalEntity.getSubtotalExclTax();
        return createRow("Subtotal(Excl.Tax)", subTotalExcl);
    }

    protected TableRow createSubtotalIncl() {
        String subTotalIncl = mTotalEntity.getSubtotalInclTax();
        return createRow("Subtotal(Incl.Tax", subTotalIncl);
    }

    protected TableRow createSubtotal() {
        String subTotal = mTotalEntity.getSubTotal();
        return createRow("Subtotal", subTotal);
    }

    protected TableRow createShippingHandling() {
        String shipping = mTotalEntity.getShippingHandling();
        float fShipping = convertToFloat(shipping);
        if (fShipping >= 0) {
            return createRow("Shipping and Handling", shipping);
        }
        return null;
    }

    protected TableRow createShippingHandlingExcl() {
        String shippingExcl = mTotalEntity.getShippingHandlingExclTax();
        return createRow("Shipping Excl.Tax", shippingExcl);
    }

    protected TableRow createShippingHandlingIncl() {
        String shippingIncl = mTotalEntity.getShippingHandlingInclTax();
        return createRow("Shipping Incl.Tax", shippingIncl);
    }

    protected TableRow createDiscount() {
        String discount = mTotalEntity.getDiscount();
        return createRow("Discount", discount);
    }

    protected TableRow createTax() {
        String tax = mTotalEntity.getTax();
        return createRow("Tax", tax);
    }

    protected TableRow createGrandtotalExcl() {
        String grandTotalExcl = mTotalEntity.getGrandTotalExclTax();
        return createRow("Grand Total(Excl.Tax)", grandTotalExcl);
    }

    protected TableRow createGrandtotalIncl() {
        String grandTotalIncl = mTotalEntity.getGrandTotalInclTax();
        return createRow("Grand Total(Incl.Tax", grandTotalIncl);
    }

    protected TableRow createGrandTotal() {
        String grandTotal = mTotalEntity.getGrandTotal();
        float fGrandTotal = convertToFloat(grandTotal);
        if (fGrandTotal >= 0) {
            return createRow("Grand Total", grandTotal);
        }
        return null;
    }

    protected TableRow createRow(String label, String price) {
        TableRow tbrPrice = new TableRow(mContext);
        tbrPrice.setGravity(Gravity.RIGHT);
        TableLayout.LayoutParams param = new TableLayout.LayoutParams();
        param.topMargin = Utils.toDp(3);

        tbrPrice.setLayoutParams(param);


        String trslLabel = SimiTranslator.getInstance().translate(label);
        TextView tvLabel = createLabel(trslLabel);
        tvLabel.setTextColor(AppColorConfig.getInstance().getContentColor());
        tbrPrice.addView(tvLabel);

        TextView tvPrice = createPrice(price);
        tbrPrice.addView(tvPrice);

        return tbrPrice;
    }

    protected TextView createLabel(String label) {
        TextView tvLabel = new TextView(mContext);
        tvLabel.setGravity(Gravity.RIGHT);
        tvLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        String htmlLabel = Html.fromHtml(label) + ": ";
        tvLabel.setText(htmlLabel);
        return tvLabel;
    }

    protected TextView createPrice(String price) {
        TextView tvPrice = new TextView(mContext);
        tvPrice.setGravity(Gravity.RIGHT);
        tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        String htmlPrice = AppStoreConfig.getInstance().getPrice(price);
        tvPrice.setText(Html.fromHtml(htmlPrice));
        tvPrice.setTextColor(AppColorConfig.getInstance().getPriceColor());
        return tvPrice;
    }

    protected float convertToFloat(String price) {
        try {
            return Float.parseFloat(price);
        } catch (Exception e) {
            return -1;
        }
    }

    public void refreshTotalPrice() {
        initView();
    }

    public void setTotalPrice(TotalPrice totalPrice) {
        mTotalEntity = totalPrice;
    }

    public TotalPrice getTotalPrice() {
        return mTotalEntity;
    }
}
