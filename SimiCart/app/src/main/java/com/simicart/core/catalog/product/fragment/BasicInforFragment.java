package com.simicart.core.catalog.product.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.price.ProductPriceViewDetail;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;

import java.util.HashMap;

public class BasicInforFragment extends SimiFragment {
    protected Product mProduct;

    public static BasicInforFragment newInstance(SimiData data) {
        BasicInforFragment fragment = new BasicInforFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(
                Rconfig.getInstance().layout(
                        "core_information_basic_inf_layout"), container, false);

        if (mData != null) {
            mProduct = (Product) getValueWithKey(Constants.KeyData.PRODUCT);
        }

        TextView tv_Name = (TextView) rootView.findViewById(Rconfig
                .getInstance().id("tv_Name"));
        tv_Name.setText(mProduct.getName().trim());
        tv_Name.setTextColor(AppColorConfig.getInstance().getContentColor());

        // price
        LinearLayout ll_price = (LinearLayout) rootView.findViewById(Rconfig
                .getInstance().id("ll_price"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ProductPriceViewDetail viewPrice = new ProductPriceViewDetail(mProduct);
        View view = viewPrice.getViewPrice();
        if (null != view) {
            if (AppStoreConfig.getInstance().isRTL()) {
                params.gravity = Gravity.RIGHT;
                ll_price.setGravity(Gravity.RIGHT);
            }
            ll_price.removeAllViewsInLayout();
            ll_price.addView(view, params);
        }

        TextView tv_Stock = (TextView) rootView.findViewById(Rconfig
                .getInstance().id("tv_Stock"));
        tv_Stock.setTextColor(AppColorConfig.getInstance().getContentColor());
        if (mProduct.getStock()) {
            tv_Stock.setText(SimiTranslator.getInstance().translate("In Stock") + ".");
        } else {
            tv_Stock.setText(SimiTranslator.getInstance().translate("Out Stock") + ".");
        }

        TextView tv_shortDescription = (TextView) rootView.findViewById(Rconfig
                .getInstance().id("tv_descrip"));
        tv_Name.setTextColor(AppColorConfig.getInstance().getContentColor());
        if (mProduct.getShortDecripition() != null
                && !mProduct.getShortDecripition().toLowerCase().equals("null")) {
            tv_shortDescription.setText(Html.fromHtml("<font color='"
                    + AppColorConfig.getInstance().getContentColor() + "'>"
                    + mProduct.getShortDecripition() + "</font>"));
        }

//		SimiCollection simiCollection = new SimiCollection();
//		simiCollection.setJSON(mProduct.getJSONObject());
//		CacheBlock cache = new CacheBlock();
//		cache.setSimiCollection(simiCollection);
//		cache.setView(rootView);
//		EventBlock event = new EventBlock();
//		event.dispatchEvent(
//				"com.simicart.core.catalog.fragment.BasicInforFragment", cache);
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put(KeyData.REWARD_POINT.VIEW, rootView);
        hmData.put(KeyData.REWARD_POINT.ENTITY_JSON, mProduct.getJSONObject());
        SimiEvent.dispatchEvent(KeyEvent.REWARD_POINT_EVENT.REWARD_ADD_ITEM_BASIC_INFO, hmData);

        rootView.setBackgroundColor(AppColorConfig.getInstance().getAppBackground());

        return rootView;
    }

    public Product getProduct() {
        return mProduct;
    }
}
