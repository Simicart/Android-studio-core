package com.simicart.core.catalog.category.block;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.category.delegate.CategoryDelegate;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

public class CategoryBlock extends SimiBlock implements CategoryDelegate {

    protected String mName;
    protected LinearLayout llCategory;
    protected TextView tv_CategoryName;
    protected TextView tv_viewmore;
    protected ImageView iv_showmore;
    protected LinearLayout ll_listProducts;

    public CategoryBlock(View view, Context context) {
        super(view, context);
    }

    public void setCategoryName(String name) {
        mName = name;
    }

    @Override
    public void initView() {

        tv_CategoryName = (TextView) mView.findViewById(Rconfig.getInstance()
                .id("tv_category"));
        tv_CategoryName.setText(mName);
        if (DataLocal.isLanguageRTL) {
            tv_CategoryName.setGravity(Gravity.RIGHT);
        }
        tv_viewmore = (TextView) mView.findViewById(Rconfig.getInstance().id(
                "tv_viewmore"));
        tv_viewmore.setText(SimiTranslator.getInstance().translate("View more"));

        iv_showmore = (ImageView) mView.findViewById(Rconfig.getInstance().id(
                "iv_showmore"));
        Drawable icon = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable("ic_view_all"));

        llCategory = (LinearLayout) mView.findViewById(Rconfig
                .getInstance().id("ll_categories"));
        View v_line = (View) mView.findViewById(Rconfig.getInstance().id(
                "v_line2"));

        if (DataLocal.isTablet) {

            icon.setColorFilter(AppColorConfig.getInstance().getMenuIconColor(),
                    PorterDuff.Mode.SRC_ATOP);
            iv_showmore.setImageDrawable(icon);

            tv_viewmore.setTextColor(AppColorConfig.getInstance().getMenuTextColor());
            tv_CategoryName.setTextColor(AppColorConfig.getInstance()
                    .getMenuTextColor());

            mView.setBackgroundColor(AppColorConfig.getInstance().getMenuBackground());
            v_line.setBackgroundColor(AppColorConfig.getInstance().getMenuLineColor());
        } else {
            ll_listProducts = (LinearLayout) mView.findViewById(Rconfig.getInstance()
                    .id("ll_listProduct"));

            tv_viewmore.setTextColor(AppColorConfig.getInstance().getContentColor());

            icon.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                    PorterDuff.Mode.SRC_ATOP);

            tv_CategoryName.setTextColor(AppColorConfig.getInstance()
                    .getContentColor());

            v_line.setBackgroundColor(AppColorConfig.getInstance().getLineColor());
        }

    }

    @Override
    public void drawView(SimiCollection collection) {

    }

    @Override
    public void onUpdateData(ArrayList<Category> categories) {

    }

    @Override
    public void onSelectedItem(int position) {

    }

    @Override
    public void showListCategory(View view) {
        llCategory.removeAllViews();
        llCategory.addView(view);
    }

    @Override
    public void showListProducts(View view) {
        if(view != null) {
            tv_viewmore.setVisibility(View.VISIBLE);
            iv_showmore.setVisibility(View.VISIBLE);
            ll_listProducts.setVisibility(View.VISIBLE);
            ll_listProducts.removeAllViews();
            ll_listProducts.addView(view);
        } else {
            tv_viewmore.setVisibility(View.GONE);
            iv_showmore.setVisibility(View.GONE);
            ll_listProducts.setVisibility(View.GONE);
        }
    }

    @Override
    public void showBack(boolean show) {

    }

    @Override
    public void updateCategoryParent(String name) {

    }
}
