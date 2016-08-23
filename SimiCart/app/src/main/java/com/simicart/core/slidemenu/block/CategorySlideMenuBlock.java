package com.simicart.core.slidemenu.block;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.category.block.CategoryBlock;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.delegate.CategorySlideMenuDelegate;

/**
 * Created by Martial on 8/19/2016.
 */
public class CategorySlideMenuBlock extends CategoryBlock {

    protected ImageView iv_back;

    public CategorySlideMenuBlock(View view, Context context) {
        super(view, context);
    }

    public void onBackClick(View.OnClickListener listener) {
        iv_back.setOnClickListener(listener);
    }

    @Override
    public void initView() {
        super.initView();

        iv_back = (ImageView) mView.findViewById(Rconfig.getInstance().id("iv_back"));
        Drawable iconBack = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable("ic_back"));
        iconBack.setColorFilter(AppColorConfig.getInstance().getMenuIconColor(),
                PorterDuff.Mode.SRC_ATOP);
        iv_back.setImageDrawable(iconBack);
        if(!mName.equals("all categories")) {
            iv_back.setVisibility(View.VISIBLE);
        } else {
            iv_back.setVisibility(View.GONE);
        }

    }

    @Override
    public void showBack(boolean show) {
        if(show == true) {
            iv_back.setVisibility(View.VISIBLE);
        } else {
            iv_back.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateCategoryParent(String name) {
        tv_CategoryName.setText(SimiTranslator.getInstance().translate(name));
    }
}
