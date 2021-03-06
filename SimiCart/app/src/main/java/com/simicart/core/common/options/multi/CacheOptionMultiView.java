package com.simicart.core.common.options.multi;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.common.options.base.CacheOptionView;
import com.simicart.core.common.options.delegate.CacheOptionMultiDelegate;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

public class CacheOptionMultiView extends CacheOptionView implements
        CacheOptionMultiDelegate {

    protected RadioGroup mRadioGroup;

    protected int iconRadio;

    public CacheOptionMultiView(CacheOption cacheOption) {
        super(cacheOption);
        iconRadio = Rconfig.getInstance().drawable("check_box");
    }

    public void setIconRadio(int idDrawable) {
        iconRadio = idDrawable;
    }

    @Override
    protected void createCacheOption() {
        createOptionView();
    }

    protected void createOptionView() {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        for (final ProductOption option : mCacheOption.getAllOption()) {
            OptionMulti option_multi = new OptionMulti(option, mContext, this);
            View view = option_multi.createView();
            ll_body.addView(view, param);

        }
    }

    @Override
    public boolean isCheckedAll() {
        for (ProductOption option : mCacheOption.getAllOption()) {
            if (!option.isChecked()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void updateStateCacheOption(String id, boolean isSeletecd) {
        if (isSeletecd) {
            if (!mCacheOption.isCompleteRequired()) {
                mCacheOption.setCompleteRequired(true);
            }
        } else {
            if (!isCheckedAll()) {
                mCacheOption.setCompleteRequired(false);
            }
        }
    }

    @Override
    public void updatePriceForHeader(String price) {
        updatePriceHeader(price);

    }

    @Override
    public void updatePriceParent(ProductOption option, boolean operation) {
        updatePriceForParent(option, operation);

    }

    @Override
    public void updatePriceMulti(float price, boolean isAdd) {
        if (isAdd) {
            mCacheOption.setPriceMulti(mCacheOption.getPriceMulti() + price);
        } else {
            mCacheOption.setPriceMulti(mCacheOption.getPriceMulti() - price);
        }

    }

    @Override
    public void onSendDependOption(ArrayList<String> optionDepends,
                                   String currentID) {
        // TODO Auto-generated method stub

    }

    @Override
    public void clearCheckAll(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public float getPriceMulti() {
        return mCacheOption.getPriceMulti();
    }

    @Override
    public boolean isRequired() {
        // TODO Auto-generated method stub
        return mCacheOption.isRequired();
    }

}
