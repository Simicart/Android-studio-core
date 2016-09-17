package com.simicart.core.catalog.product.block;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.material.floatingactionbutton.FloatingActionButton;
import com.simicart.core.style.material.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

public class ProductMorePluginBlock extends SimiBlock {
    protected Product mProduct;
    protected FloatingActionsMenu mMultipleActions;
    protected FloatingActionButton more_share;

    protected ArrayList<FloatingActionButton> mListButton;

    public ProductMorePluginBlock(View view, Context context) {
        super(view, context);
    }

    public ArrayList<FloatingActionButton> getListButton() {
        return mListButton;
    }

    public Product getProduct() {
        return mProduct;
    }

    public void setProduct(Product mProduct) {
        this.mProduct = mProduct;
    }

    public void setListenerMoreShare(OnClickListener onclick) {
        more_share.setOnClickListener(onclick);
    }

    @Override
    public void initView() {
        mListButton = new ArrayList<FloatingActionButton>();
        mMultipleActions = (FloatingActionsMenu) mView.findViewById(Rconfig
                .getInstance().id("more_plugins_action"));
        mMultipleActions.createButton(mContext, AppColorConfig.getInstance()
                .getColorButtonBackground(), AppColorConfig.getInstance()
                .getColorButtonBackground(), AppColorConfig.getInstance()
                .getButtonTextColor());
        more_share = new FloatingActionButton(mContext);
        more_share.setColorNormal(Color.parseColor("#FFFFFF"));
        more_share.setColorPressed(Color.parseColor("#f4f4f4"));
        more_share.setIcon(Rconfig.getInstance().drawable("ic_share"));
        mListButton.add(more_share);
        for (int i = 0; i < mListButton.size(); i++) {
            mMultipleActions.addButton(mListButton.get(i));
        }
//		EventBlock event = new EventBlock();
//		CacheBlock cacheBlock = new CacheBlock();
//		cacheBlock.setBlock(this);
//		cacheBlock.setView(mView);
//		cacheBlock.setContext(mContext);
//		cacheBlock.setSimiEntity(mProduct);
//		event.dispatchEvent(
//				"com.simicart.core.catalog.product.block.ProductMorePluginBlock",
//				cacheBlock);
    }
}
