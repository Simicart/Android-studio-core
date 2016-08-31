package com.simicart.core.checkout.component;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.delegate.ShippingMethodCallBack;
import com.simicart.core.checkout.entity.ShippingMethodEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;

import java.util.ArrayList;

/**
 * Created by frank on 11/05/2016.
 */
public class ShippingMethodComponent extends SimiComponent implements ShippingMethodCallBack {

    protected LinearLayout llComponent;
    protected TextView tvTitle;
    protected ShippingMethodCallBack mCallBack;
    protected ArrayList<ItemShippingMethodView> mItemViews;
    protected ArrayList<ShippingMethodEntity> mShippingEntities;
    protected int topMargin = Utils.getValueDp(5);
    protected boolean isCreated = false;


    public ShippingMethodComponent(ArrayList<ShippingMethodEntity> mShippingEntities) {
        super();
        this.mShippingEntities = mShippingEntities;
    }

    @Override
    public View createView() {
        if (isCreated) {
            return rootView;
        }
        isCreated = true;
        rootView = findLayout("core_component_layout");
        intView();

        return rootView;
    }

    protected void intView() {
        tvTitle = (TextView) findView("tv_title");
        String title = SimiTranslator.getInstance().translate("Shipping Method");
        tvTitle.setText(title);
        int bgColor = AppColorConfig.getInstance().getSectionColor();
        tvTitle.setBackgroundColor(bgColor);
        int bgText = AppColorConfig.getInstance().getContentColor();
        tvTitle.setTextColor(bgText);

        llComponent = (LinearLayout) findView("ll_body_component");

        if (null != mShippingEntities && mShippingEntities.size() > 0) {
            mItemViews = new ArrayList<>();
            for (int i = 0; i < mShippingEntities.size(); i++) {
                ShippingMethodEntity entity = mShippingEntities.get(i);
                ItemShippingMethodView itemView = new ItemShippingMethodView(entity);
                itemView.setCallBack(this);
                View view = itemView.createView();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = topMargin;
                llComponent.addView(view, params);
                mItemViews.add(itemView);
            }
        }
    }


    @Override
    public void onSelect(ShippingMethodEntity entity) {
        if (null != mCallBack) {
            mCallBack.onSelect(entity);
        }

        for (int i = 0; i < mItemViews.size(); i++) {
            ItemShippingMethodView itemView = mItemViews.get(i);
            if (!itemView.isEqual(entity)) {
                itemView.selectItem(false);
            }
        }


    }

    public void setCallBack(ShippingMethodCallBack callBack) {
        this.mCallBack = callBack;
    }
}
