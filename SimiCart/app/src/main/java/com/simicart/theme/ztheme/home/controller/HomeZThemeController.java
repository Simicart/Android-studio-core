package com.simicart.theme.ztheme.home.controller;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.catalog.listproducts.fragment.ProductListFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.slidemenu.fragment.CateSlideMenuFragment;
import com.simicart.theme.ztheme.home.delegate.HomeZThemeDelegate;
import com.simicart.theme.ztheme.home.entity.ZThemeCatalogEntity;
import com.simicart.theme.ztheme.home.entity.ZThemeSpotEntity;
import com.simicart.theme.ztheme.home.fragment.SpotProductListZthemeFragment;
import com.simicart.theme.ztheme.home.model.HomeZThemeModel;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeZThemeController extends SimiController {
    protected HomeZThemeDelegate mDelegate;
    protected ExpandableListView.OnGroupClickListener onGroupClickListener;
    protected ExpandableListView.OnChildClickListener onChildClickListener;

    public ExpandableListView.OnChildClickListener getOnChildClickListener() {
        return onChildClickListener;
    }

    public ExpandableListView.OnGroupClickListener getOnGroupClickListener() {
        return onGroupClickListener;
    }

    @Override
    public void onStart() {
        initListener();

        mModel = new HomeZThemeModel();
        mDelegate.showLoading();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                mDelegate.updateView(mModel.getCollection());
            }
        });
        if (DataLocal.isTablet) {
            mModel.addBody(Constants.PHONE_TYPE, Constants.TABLET);
        }
        mModel.request();
    }

    protected void initListener() {
        onChildClickListener = new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ZThemeCatalogEntity catalogEntity = mDelegate.getListCatalog().get(groupPosition);
                if(catalogEntity.getType().equals("category") && catalogEntity.getCategoryZTheme().hasChild() == true) {
                    Category childCategory = catalogEntity.getCategoryZTheme().getListChildCategory().get(childPosition);
                    openCate(childCategory);
                }
                return true;
            }
        };

        onGroupClickListener = new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                ZThemeCatalogEntity catalogEntity = mDelegate.getListCatalog().get(groupPosition);

                if(catalogEntity.getType().equals("category")) {
                    Category categoryEntity = catalogEntity.getCategoryZTheme();
                    if(categoryEntity.hasChild() == false) {
                        openCate(categoryEntity);
                    }
                } else {
                    ZThemeSpotEntity spot = catalogEntity.getZThemeSpotEntity();
                    openListProduct(spot);
                }

                return false;
            }
        };
    }

    protected void openCate(Category cate) {
//        String id = cate.getID();
//        if (cate.hasChild()) {
//            SimiData data = new SimiData();
//            data.setData(id);
//            CategoryFragment categoryFragment = CategoryFragment.newInstance(data);
//            SimiManager.getIntance().replaceFragment(categoryFragment);
//        } else {
//            HashMap<String, Object> hs = new HashMap<>();
//            hs.put("cat_id", id);
//            SimiData data = new SimiData();
//            data.setHsData(hs);
//            CategoryDetailFragment fragment = CategoryDetailFragment.newInstance(data);
//            SimiManager.getIntance().replaceFragment(fragment);
//        }
    }

    protected void openListProduct(ZThemeSpotEntity spot) {
        CategoryFragment fragment = CategoryFragment.newInstance(new SimiData(new HashMap<String, Object>()));
        SimiManager.getIntance().replaceFragment(fragment);
//        SimiData data = new SimiData();
//        data.addData("list_id", product.getmId());
//        CategoryDetailFragment fragment = CategoryDetailFragment.newInstance(data);
//        SimiManager.getIntance().replaceFragment(fragment);
    }

    @Override
    public void onResume() {
        mDelegate.updateView(mModel.getCollection());
    }

    public void setDelegate(HomeZThemeDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

}
