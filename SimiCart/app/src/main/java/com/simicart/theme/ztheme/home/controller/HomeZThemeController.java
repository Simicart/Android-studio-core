package com.simicart.theme.ztheme.home.controller;

import android.view.View;
import android.widget.ExpandableListView;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.catalog.categorydetail.fragment.CategoryDetailFragment;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.theme.ztheme.home.delegate.HomeZThemeDelegate;
import com.simicart.theme.ztheme.home.entity.ZThemeCatalogEntity;
import com.simicart.theme.ztheme.home.entity.ZThemeSpotEntity;
import com.simicart.theme.ztheme.home.model.HomeZThemeModel;

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
                if (catalogEntity.getType().equals("cat") && catalogEntity.getCategoryZTheme().hasChild() == true) {
                    Category childCategory = catalogEntity.getCategoryZTheme().getListChildCategory().get(childPosition);
                    if(childCategory.hasChild() == true) {
                        openCate(childCategory);
                    } else {
                        openListProduct(childCategory);
                    }
                }
                return true;
            }
        };

        onGroupClickListener = new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                ZThemeCatalogEntity catalogEntity = mDelegate.getListCatalog().get(groupPosition);

                if (catalogEntity.getType().equals("cat")) {
                    Category categoryEntity = catalogEntity.getCategoryZTheme();
                    if (categoryEntity.hasChild() == true) {
                        if(DataLocal.isTablet) {
                            SimiManager.getIntance().openSubCategory(categoryEntity.getCategoryId(), categoryEntity.getCategoryName());
                            return true;
                        }
                    } else {
                        openListProduct(categoryEntity);
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
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put(KeyData.CATEGORY.CATEGORY_ID, cate.getCategoryId());
        hmData.put(KeyData.CATEGORY.CATEGORY_NAME, cate.getCategoryName());
        SimiManager.getIntance().openCategory(hmData);
    }

    protected void openListProduct(ZThemeSpotEntity spot) {
        HashMap<String,Object> hm = new HashMap<>();
        hm.put(KeyData.CATEGORY_DETAIL.TYPE, CategoryDetailFragment.CUSTOM);
        hm.put("key", spot.getKey());
        hm.put(KeyData.CATEGORY_DETAIL.CATE_NAME, spot.getName());
        hm.put(KeyData.CATEGORY_DETAIL.CUSTOM_URL, "ztheme/api/get_spot_products");
        SimiManager.getIntance().openCategoryDetail(hm);
    }

    protected void openListProduct(Category category) {
        HashMap<String,Object> hm = new HashMap<>();
        hm.put(KeyData.CATEGORY_DETAIL.TYPE, CategoryDetailFragment.CATE);
        hm.put(KeyData.CATEGORY_DETAIL.CATE_ID, category.getCategoryId());
        hm.put(KeyData.CATEGORY_DETAIL.CATE_NAME, category.getCategoryName());
        SimiManager.getIntance().openCategoryDetail(hm);
    }

    @Override
    public void onResume() {
        mDelegate.updateView(mModel.getCollection());
    }

    public void setDelegate(HomeZThemeDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

}
