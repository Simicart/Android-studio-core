package com.simicart.core.customer.block;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.component.ListProductCheckoutComponent;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.common.Utils;
import com.simicart.core.common.price.TotalPriceView;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.component.AddressOrderComponent;
import com.simicart.core.customer.entity.AddressEntity;
import com.simicart.core.customer.entity.OrderHisDetail;

import java.util.ArrayList;

@SuppressLint("DefaultLocale")
public class OrderHistoryDetailBlock extends SimiBlock {
    protected AppCompatButton btReorder;
    protected TextView tvDateLabel, tvOrderNumberLablel, tvTotalLabel;
    protected TextView tvDate, tvOrderNumber, tvTotal;
    protected TextView tvShipToLabel;
    protected TextView tvShippingMethod;
    protected LinearLayout llShippingAddress;
    protected TextView tvItemsLabel;
    protected LinearLayout llItems;
    protected TextView tvFeeDetailLabel;
    protected LinearLayout llPrice;
    protected TextView tvPaymentLabel;
    protected TextView tvPaymentMethod, tvPaymentCoupon;
    protected LinearLayout llBillingAddress;

    protected OrderHisDetail orderHistoryEntity;

    public OrderHistoryDetailBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {

        btReorder = (AppCompatButton) mView.findViewById(Rconfig.getInstance().id("bt_reorder"));
        btReorder.setText(SimiTranslator.getInstance().translate("Reorder"));
        btReorder.setTextColor(Color.WHITE);
        btReorder.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());

    }

    protected void initOrderDetail() {
        tvDateLabel = (TextView) mView.findViewById(Rconfig.getInstance().id("tv_date_label"));
        tvDateLabel.setText(SimiTranslator.getInstance().translate("Order Date"));
        tvDateLabel.setTextColor(AppColorConfig.getInstance().getContentColor());

        tvDate = (TextView) mView.findViewById(Rconfig.getInstance().id("tv_date"));
        tvDate.setText(orderHistoryEntity.getOrder_date());
        tvDate.setTextColor(AppColorConfig.getInstance().getContentColor());

        tvOrderNumberLablel = (TextView) mView.findViewById(Rconfig.getInstance().id("tv_order_number_label"));
        tvOrderNumberLablel.setText(SimiTranslator.getInstance().translate("Order Number"));
        tvOrderNumberLablel.setTextColor(AppColorConfig.getInstance().getContentColor());

        tvOrderNumber = (TextView) mView.findViewById(Rconfig.getInstance().id("tv_order_number"));
        tvOrderNumber.setText(orderHistoryEntity.getOrder_code());
        tvOrderNumber.setTextColor(AppColorConfig.getInstance().getContentColor());

        tvTotalLabel = (TextView) mView.findViewById(Rconfig.getInstance().id("tv_total_label"));
        tvTotalLabel.setText(SimiTranslator.getInstance().translate("Order Total"));
        tvTotalLabel.setTextColor(AppColorConfig.getInstance().getContentColor());

        tvTotal = (TextView) mView.findViewById(Rconfig.getInstance().id("tv_total"));
        tvTotal.setTextColor(AppColorConfig.getInstance().getPriceColor());
        String symbol = orderHistoryEntity.getTotal_price().getCurrencySymbol();
        String price = AppStoreConfig.getInstance().getPrice(
                orderHistoryEntity.getOrder_total());
        if (null != symbol) {
            price = AppStoreConfig.getInstance().getPrice(
                    orderHistoryEntity.getOrder_total(), symbol);
        }
        tvTotal.setText(price);

    }

    protected void initShipping() {
        tvShipToLabel = (TextView) mView.findViewById(Rconfig.getInstance().id("tv_shipto_label"));
        tvShipToLabel.setText(SimiTranslator.getInstance().translate("Ship To").toUpperCase());
        tvShipToLabel.setTextColor(AppColorConfig.getInstance().getContentColor());

        llShippingAddress = (LinearLayout) mView.findViewById(Rconfig.getInstance().id("ll_shipping_address"));
        AddressEntity shippingAddress = orderHistoryEntity.getShipping_address();
        if (shippingAddress != null) {
            AddressOrderComponent addressOrderComponent = new AddressOrderComponent(shippingAddress);
            llShippingAddress.addView(addressOrderComponent.createView());
        }

        tvShippingMethod = (TextView) mView.findViewById(Rconfig.getInstance().id("tv_shipping_method"));
        tvShippingMethod.setText(orderHistoryEntity.getShipping_method());
        tvShippingMethod.setTextColor(AppColorConfig.getInstance().getContentColor());
    }

    protected void initListItems() {
        tvItemsLabel = (TextView) mView.findViewById(Rconfig.getInstance().id("tv_items_label"));
        tvItemsLabel.setText(SimiTranslator.getInstance().translate("Items").toUpperCase());
        tvItemsLabel.setTextColor(AppColorConfig.getInstance().getContentColor());

        llItems = (LinearLayout) mView.findViewById(Rconfig.getInstance().id("ll_list_item"));
        ArrayList<Cart> listItems = orderHistoryEntity.getOrder_item();
        if (listItems != null) {
            ListProductCheckoutComponent listProductCheckoutComponent = new ListProductCheckoutComponent(listItems, null);
            llItems.addView(listProductCheckoutComponent.createView());
        }
    }

    protected void initFeeDetail() {
        tvFeeDetailLabel = (TextView) mView.findViewById(Rconfig.getInstance().id("tv_fee_detail_label"));
        tvFeeDetailLabel.setText(SimiTranslator.getInstance().translate("Fee Detail").toUpperCase());
        tvFeeDetailLabel.setTextColor(AppColorConfig.getInstance().getContentColor());

        llPrice = (LinearLayout) mView.findViewById(Rconfig.getInstance().id("ll_price"));
        TotalPrice totalPriceEntity = orderHistoryEntity.getTotal_price();
        TotalPriceView viewPrice = new TotalPriceView(totalPriceEntity);
        if (viewPrice != null) {
            //TotalPriceComponent totalPriceComponent = new TotalPriceComponent(totalPriceEntity);
            llPrice.addView(viewPrice.getTotalPriceView());
        }
    }

    protected void initPayment() {
        tvPaymentLabel = (TextView) mView.findViewById(Rconfig.getInstance().id("tv_payment_label"));
        tvPaymentLabel.setText(SimiTranslator.getInstance().translate("Payment").toUpperCase());
        tvPaymentLabel.setTextColor(AppColorConfig.getInstance().getContentColor());

        tvPaymentMethod = (TextView) mView.findViewById(Rconfig.getInstance().id("tv_payment_method"));
        tvPaymentMethod.setText(orderHistoryEntity.getPayment_method());
        tvPaymentMethod.setTextColor(AppColorConfig.getInstance().getContentColor());

        llBillingAddress = (LinearLayout) mView.findViewById(Rconfig.getInstance().id("ll_billing_address"));
        AddressEntity billingAddress = orderHistoryEntity.getBilling_address();
        if (billingAddress != null) {
            AddressOrderComponent addressOrderComponent = new AddressOrderComponent(billingAddress);
            llBillingAddress.addView(addressOrderComponent.createView());
        }

        tvPaymentCoupon = (TextView) mView.findViewById(Rconfig.getInstance().id("tv_payment_couponCode"));
        String coupon = orderHistoryEntity.getOrder_gift_code();
        if (!Utils.validateString(coupon)) {
            coupon = SimiTranslator.getInstance().translate("None");
        }
        tvPaymentCoupon.setText(SimiTranslator.getInstance().translate("Coupon Code") + ": " + coupon);
        tvPaymentCoupon.setTextColor(AppColorConfig.getInstance().getContentColor());

    }

    @Override
    public void drawView(SimiCollection collection) {
        ArrayList<SimiEntity> entity = collection.getCollection();
        if (null != entity && entity.size() > 0) {
            orderHistoryEntity = (OrderHisDetail) entity.get(0);

            initOrderDetail();
            initShipping();
            initFeeDetail();
            initListItems();
            initPayment();
        }
    }

    public void onReorderClick(View.OnClickListener listener) {
        btReorder.setOnClickListener(listener);
    }

}
