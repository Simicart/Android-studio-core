package com.simicart.plugins.braintree;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.payment.Payment;
import com.simicart.core.checkout.entity.OrderInforEntity;
import com.simicart.core.checkout.entity.PaymentMethodEntity;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.Utils;

public class BraintreePayment extends Payment {

    @Override
    protected String getKeyEvent() {
        Log.e("Braintree ", "getKeyEvent " + KeyEvent.REVIEW_ORDER.FOR_PAYMENT_TYPE_SDK + "SIMIBRAINTREE");
        return KeyEvent.REVIEW_ORDER.FOR_PAYMENT_TYPE_SDK + "SIMIBRAINTREE";
    }

    @Override
    protected void openPaymentPage() {

        Context context = SimiManager.getIntance().getCurrentActivity();

        Log.e("Braintree ", "openPaymentPage");
        SimiData data = new SimiData(mData);

        Intent intent = new Intent(context, BrainTreeActivity.class);

        OrderInforEntity orderInfo = (OrderInforEntity) data.getData().get("order_infor_entity");
        String invoice_number = orderInfo.getInvoiceNumber();
        PaymentMethodEntity paymentMethod = (PaymentMethodEntity) data.getData().get("payment_method_entity");
        TotalPrice totalPriceEntity = (TotalPrice) data.getData().get("total_price");
        String total_price = totalPriceEntity.getGrandTotal();

        String token = paymentMethod.getData("token");
        intent.putExtra("EXTRA_TOKEN", token);
        Log.e("BrainTree TOKEN_ID ", token);
        if (!Utils.validateString(token)) {
            SimiNotify.getInstance().showNotify(
                    "The requested Payment Method is not available.");
            SimiManager.getIntance().backToHomeFragment();
            return;
        }

        String client_id = paymentMethod.getData("client_id");
        intent.putExtra("EXTRA_CLIENT_ID", client_id);
        Log.e("BrainTree CLIENT_ID ", client_id);
        // if (Utils.validateString(client_id)) {
        // SimiManager.getIntance().showNotify(
        // "The requested Payment Method is not available.");
        // return;
        // }

        String is_sandbox = paymentMethod.getData("is_sandbox");
        intent.putExtra("EXTRA_SANDBOX", is_sandbox);
        Log.e("BrainTree IS_SANDBOX ", is_sandbox);
        if (!Utils.validateString(is_sandbox)) {
            SimiNotify.getInstance().showNotify(
                    "The requested Payment Method is not available.");
            SimiManager.getIntance().backToHomeFragment();
            return;
        }

        intent.putExtra("EXTRA_PRICE", total_price);
        Log.e("BrainTree EXTRA_PRICE", total_price);

//            String bnCode = paymentMethod.getData("bncode");
//            intent.putExtra("EXTRA_BNCODE", bnCode);
//            Log.e("BrainTree BNCODE ", bnCode);
//            if (!Utils.validateString(bnCode)) {
//                SimiManager.getIntance().showNotify(
//                        "The requested Payment Method is not available.");
//                SimiManager.getIntance().backToHomeFragment();
//                return;
//            }

        intent.putExtra("EXTRA_INVOICE_NUMBER", invoice_number);
        Log.e("BrainTree INVOICE NUMBER ", invoice_number);
        if (!Utils.validateString(invoice_number)) {
            SimiNotify.getInstance().showNotify(
                    "The requested Payment Method is not available.");
            SimiManager.getIntance().backToHomeFragment();
            return;
        }
        context.startActivity(intent);
    }

}
