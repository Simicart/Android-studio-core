package com.simicart.plugins.mobileanalytics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.entity.OrderInforEntity;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by frank on 06/09/2016.
 */
public class Analytic {

    protected Tracker mTracker;
    protected Context mContext;
    protected String TRACKER_ID = "UA-52928750-1";

    public Analytic() {
        mContext = SimiManager.getIntance().getCurrentActivity();

        BroadcastReceiver trackerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (null == mTracker) {
                    getTrackerID();
                }
            }
        };

        SimiEvent.registerEvent("com.simicart.analytics.register", trackerReceiver);


        BroadcastReceiver sendActionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (null != mTracker) {
                    Bundle bundle = intent.getBundleExtra(Constants.DATA);
                    SimiData data = bundle.getParcelable("entity");
                    HashMap<String, Object> hm = data.getData();
                    sendAction(hm);
                }
            }
        };
        SimiEvent.registerEvent("com.simicart.analytics.sendaction", sendActionReceiver);


    }

    protected void getTrackerID() {

        final AnalyticModel model = new AnalyticModel();

        model.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                TRACKER_ID = model.getTrackerID();
                if (Utils.validateString(TRACKER_ID)) {
                    Log.e("Analytic", "-----> TRACKER_ID " + TRACKER_ID);
                    createTracker();
                }

            }
        });

        model.request();


    }

    protected void createTracker() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(mContext);
        mTracker = analytics.newTracker(TRACKER_ID);
    }


    protected void sendAction(HashMap<String, Object> hm) {
        if (null != hm) {
            if (hm.containsKey(KeyData.ANALYTICS.SEND_TYPE)) {
                String type = (String) hm.get(KeyData.ANALYTICS.SEND_TYPE);
                if (type == ValueData.ANALYTICS.SCREEN_TYPE) {
                    sendScreenName(hm);
                } else if (type == ValueData.ANALYTICS.BANNER_TYPE) {
                    sendBanerAction(hm);
                } else if (type == ValueData.ANALYTICS.ORDER_TYPE) {
                    sendOrderAction(hm);
                }
            }
        }
    }

    protected void sendScreenName(HashMap<String, Object> hm) {
        if (hm.containsKey(KeyData.ANALYTICS.NAME_SCREEN)) {
            String screenName = (String) hm.get(KeyData.ANALYTICS.NAME_SCREEN);
            Log.e("Analytic", "-----> send screen name " + screenName);
            if (Utils.validateString(screenName)) {
                mTracker.setScreenName(screenName);
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            }
        }
    }

    protected void sendBanerAction(HashMap<String, Object> hm) {
        if (hm.containsKey(KeyData.ANALYTICS.URL_BANNER)) {
            String url = (String) hm.get(KeyData.ANALYTICS.URL_BANNER);
            Log.e("Analytic", "-----> send banner action " + url);
            mTracker.send(new HitBuilders.EventBuilder().setCategory("ui_action").setAction("banner_press").setLabel(url).build());
        }

    }

    protected void sendOrderAction(HashMap<String, Object> hm) {
        OrderInforEntity orderInforEntity = (OrderInforEntity) hm.get("order_infor_entity");
        String invoiceNumber = orderInforEntity.getData("invoice_number");

        Log.e("Analytic", "-----> send order action " + invoiceNumber);

        TotalPrice totalPrice = (TotalPrice) hm.get("total_price");
        Double shippingFee = getShippingFee(totalPrice);
        Double taxFee = convertToDouble(totalPrice.getTax());
        Double totalPriceFee = getTotalPrice(totalPrice);

        ArrayList<Cart> listCart = new ArrayList<>();
        listCart = DataLocal.listCarts;


        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder();

        ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE);
        productAction.setTransactionId(invoiceNumber);
        productAction.setTransactionAffiliation("In-app Store");
        if (null != shippingFee) {
            productAction.setTransactionShipping(shippingFee);
        }
        if (null != taxFee) {
            productAction.setTransactionTax(taxFee);
        }
        if (null != totalPrice) {
            productAction.setTransactionRevenue(totalPriceFee);
        }

        builder.setProductAction(productAction);

        if (null != listCart && listCart.size() > 0) {
            for (int i = 0; i < listCart.size(); i++) {
                Cart cart = listCart.get(i);
                Product product = new Product();
                product.setId(cart.getProduct_id());
                product.setName(cart.getProduct_name());
                product.setPrice(cart.getProduct_price());
                product.setQuantity(1);
                builder.addProduct(product);
            }
        }

        mTracker.setScreenName("transaction");
        mTracker.send(builder.build());


    }

    protected Double getShippingFee(TotalPrice totalPrice) {

        String shippingFee = totalPrice.getShippingHandling();
        if (Utils.validateString(shippingFee)) {
            return convertToDouble(shippingFee);
        }

        String shippingInclFee = totalPrice.getShippingHandlingInclTax();
        if (Utils.validateString(shippingInclFee)) {
            return convertToDouble(shippingInclFee);
        }

        String shippingExclFee = totalPrice.getShippingHandlingExclTax();
        if (Utils.validateString(shippingExclFee)) {
            return convertToDouble(shippingExclFee);
        }

        return null;

    }

    protected Double getTotalPrice(TotalPrice totalPrice) {
        String grandTotal = totalPrice.getGrandTotal();
        if (Utils.validateString(grandTotal)) {
            return convertToDouble(grandTotal);
        }

        String grandTotalIncl = totalPrice.getGrandTotalInclTax();
        if (Utils.validateString(grandTotalIncl)) {
            return convertToDouble(grandTotalIncl);
        }

        String grandTotalExcl = totalPrice.getGrandTotalExclTax();
        if (Utils.validateString(grandTotalExcl)) {
            return convertToDouble(grandTotalExcl);
        }

        return null;

    }

    protected Double convertToDouble(String fee) {
        try {
            Double dFee = Double.parseDouble(fee);
            return dFee;
        } catch (Exception e) {
            return null;
        }
    }


}
