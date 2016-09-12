package com.simicart.plugins.paypal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.payment.Payment;
import com.simicart.core.checkout.entity.OrderInforEntity;
import com.simicart.core.checkout.entity.PaymentMethodEntity;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.Utils;

/**
 * Created by frank on 30/08/2016.
 */
public class PayPal extends Payment {

    public PayPal() {
        super();
    }

    @Override
    protected String getKeyEvent() {
        Log.e("PayPal ", "getKeyEvent " + KeyEvent.REVIEW_ORDER.FOR_PAYMENT_TYPE_SDK + "PAYPAL_MOBILE");
        return KeyEvent.REVIEW_ORDER.FOR_PAYMENT_TYPE_SDK + "PAYPAL_MOBILE";
    }

    @Override
    protected void openPaymentPage() {
        Log.e("PayPal ", "openPaymentPage");

        PaypalEntity paypalEntity = new PaypalEntity();

        if (mData.containsKey("total_price")) {
            TotalPrice totalPrice = (TotalPrice) mData.get("total_price");
            String total_price = parseTotalPrice(totalPrice);
            paypalEntity.setTotalPrice(total_price);
        }

        if (mData.containsKey("payment_method")) {
            PaymentMethodEntity paymentMethodEntity = (PaymentMethodEntity) mData.get("payment_method");

            String client_id = paymentMethodEntity.getData("client_id");
            paypalEntity.setClientID(client_id);

            String is_sandbox = paymentMethodEntity.getData("is_sandbox");
            if (Utils.TRUE(is_sandbox)) {
                paypalEntity.setSandbox(true);
            }

            String bnCode = paymentMethodEntity.getData("bncode");
            paypalEntity.setBNCode(bnCode);
        }


        if (mData.containsKey("order_infor_entity")) {
            OrderInforEntity orderInforEntity = (OrderInforEntity) mData.get("order_infor_entity");
            String invoice_number = orderInforEntity.getData("invoice_number");
            paypalEntity.setInvoiceNumber(invoice_number);
        }

        Context context = SimiManager.getIntance().getCurrentActivity();
        Intent intent = new Intent(context, PayPalActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", paypalEntity);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    protected String parseTotalPrice(TotalPrice totalPrice) {
        String mTotalPrice = null;
        String grandTotal = totalPrice.getGrandTotal();
        if (Utils.validateString(grandTotal)) {
            mTotalPrice = grandTotal;
            return mTotalPrice;
        }

        String grandTotalIncl = totalPrice.getGrandTotalInclTax();
        if (Utils.validateString(grandTotalIncl)) {
            mTotalPrice = grandTotalIncl;
            return mTotalPrice;
        }

        String grandTotalExcl = totalPrice.getGrandTotalExclTax();
        if (Utils.validateString(grandTotalExcl)) {
            mTotalPrice = grandTotalExcl;
            return mTotalPrice;
        }
        return mTotalPrice;
    }
}
