package com.simicart.theme.ztheme.home.block;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.catalog.listproducts.fragment.ProductListFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.fragment.CateSlideMenuFragment;
import com.simicart.theme.ztheme.home.adapter.HomeZThemeAdapterTablet;
import com.simicart.theme.ztheme.home.entity.ZThemeCatalogEntity;

import java.util.ArrayList;

public class HomeZThemeBlockTablet extends HomeZThemeBlock implements
        OnTouchListener {

    ListView lv_category;
    FrameLayout fr_layout;
    ZThemeCatalogEntity current_Category;
    RelativeLayout rlt_home;

    ArrayList<ZThemeCatalogEntity> categoriesTree;

    public HomeZThemeBlockTablet(View view, Context context) {
        super(view, context);
    }

    public void setCategoryItemClickListener(OnItemClickListener listener) {
        lv_category.setOnItemClickListener(listener);
    }

    protected void closeCatSub() {
        Animation animation = AnimationUtils.loadAnimation(mContext, Rconfig
                .getInstance().getId("out_to_right", "anim"));
        fr_layout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                fr_layout.removeAllViews();
                fr_layout.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void initView() {
        rlt_home = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
                .id("rlt_home"));
        fr_layout = (FrameLayout) mView.findViewById(Rconfig.getInstance().id(
                "container2"));
        lv_category = (ListView) mView.findViewById(Rconfig.getInstance().id(
                "lv_category"));

        // lv_category.setOnTouchListener(this);
    }

    protected void showCategoriesView(ArrayList<ZThemeCatalogEntity> categories) {
        HomeZThemeAdapterTablet adapter = new HomeZThemeAdapterTablet(mContext,
                categories);
        lv_category.setAdapter(adapter);
    }

    float down_x = 0;
    float down_y = 0;
    float up_x = 0;
    float up_y = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                down_x = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                up_x = event.getX();
                float distance = Math.abs(up_x - down_x);

                System.out.println(distance);
                break;
        }
        return true;
    }
}
