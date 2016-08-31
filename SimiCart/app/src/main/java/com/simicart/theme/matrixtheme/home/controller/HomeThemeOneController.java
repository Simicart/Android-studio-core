package com.simicart.theme.matrixtheme.home.controller;

import android.view.View;

import com.simicart.core.banner.entity.BannerEntity;
import com.simicart.core.banner.model.BannerModel;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.config.DataLocal;
import com.simicart.core.home.component.BannerComponent;
import com.simicart.theme.matrixtheme.home.component.CateHomeThemeOneComponent;
import com.simicart.theme.matrixtheme.home.component.SpotProductHomeComponent;
import com.simicart.theme.matrixtheme.home.delegate.HomeThemeOneDelegate;
import com.simicart.theme.matrixtheme.home.entity.OrderProduct;
import com.simicart.theme.matrixtheme.home.model.CategoryHomeThemeOneModel;
import com.simicart.theme.matrixtheme.home.model.SpotProductHomeThemeOneModel;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by frank on 8/20/16.
 */
public class HomeThemeOneController extends SimiController {

    protected HomeThemeOneDelegate mDelegate;
    protected ArrayList<Category> mListCate;
    protected ArrayList<BannerEntity> mListBanner;
    protected ArrayList<OrderProduct> mListProduct;

    protected BannerModel bannerModel;
    protected CategoryHomeThemeOneModel cateModel;
    protected SpotProductHomeThemeOneModel spotModel;

    @Override
    public void onStart() {
        requestBanner();

        requestHomeCate();

        requestSpotProduct();
    }

    protected void requestBanner() {
        bannerModel = new BannerModel();
        bannerModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                ArrayList<SimiEntity> entities = collection.getCollection();
                if (null != entities && entities.size() > 0) {
                    parseListBanner(entities);
                }
                showBanner();
            }
        });

        bannerModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {

            }
        });

        bannerModel.addBody("limit", "10");
        bannerModel.request();
    }

    protected void requestHomeCate() {

        cateModel = new CategoryHomeThemeOneModel();
        cateModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                ArrayList<SimiEntity> entities = collection.getCollection();
                if (null != entities && entities.size() > 0) {
                    parseListCate(entities);
                }
                showCate();
            }
        });

        cateModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {

            }
        });

        if (DataLocal.isTablet) {
            cateModel.addBody("phone_type", "tablet");
        }

        cateModel.request();
    }

    protected void requestSpotProduct() {

        spotModel = new SpotProductHomeThemeOneModel();
        spotModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                ArrayList<SimiEntity> entities = collection.getCollection();
                parseSpotProduct(entities);
                showSpotProduct();
            }
        });

        spotModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {

            }
        });
        if (DataLocal.isTablet) {
            spotModel.addBody("phone_type", "tablet");
        }

        spotModel.request();
    }

    protected void parseListBanner(ArrayList<SimiEntity> entities) {
        mListBanner = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            SimiEntity entity = entities.get(i);
            JSONObject json = entity.getJSONObject();
            BannerEntity banner = new BannerEntity();
            banner.parse(json);
            mListBanner.add(banner);
        }
    }

    protected void showBanner() {
        if (mListBanner.size() == 0) {
            mDelegate.showBanner(null);
        } else {
            BannerComponent bannerComponent = new BannerComponent(mListBanner);
            View bannerView = bannerComponent.createView();
            mDelegate.showBanner(bannerView);
        }
    }

    protected void parseListCate(ArrayList<SimiEntity> entities) {
        mListCate = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            Category category = (Category) entities.get(i);
            mListCate.add(category);
        }

    }

    protected void showCate() {
        if (null != mListCate && mListCate.size() > 0) {
            int size = mListCate.size();

            Category category1 = mListCate.get(0);
            CateHomeThemeOneComponent cate1Component = new CateHomeThemeOneComponent(category1);
            View cate1View = cate1Component.createView();
            mDelegate.showCate(cate1View, 1);

            if (size > 1) {
                Category category2 = mListCate.get(1);
                CateHomeThemeOneComponent cate2Component = new CateHomeThemeOneComponent(category2);
                View cate2View = cate2Component.createView();
                mDelegate.showCate(cate2View, 2);
            }

            if (size > 2) {
                Category category3 = mListCate.get(2);
                CateHomeThemeOneComponent cate3Component = new CateHomeThemeOneComponent(category3);
                View cate3View = cate3Component.createView();
                mDelegate.showCate(cate3View, 3);
            }

            if (size > 3) {
                Category category4 = mListCate.get(3);
                CateHomeThemeOneComponent cate4Component = new CateHomeThemeOneComponent(category4);
                View cate4View = cate4Component.createView();
                mDelegate.showCate(cate4View, 4);
            }
        }
    }

    protected void parseSpotProduct(ArrayList<SimiEntity> entities) {
        if (null != entities && entities.size() > 0) {
            mListProduct = new ArrayList<>();
            for (int i = 0; i < entities.size(); i++) {
                OrderProduct orderProduct = (OrderProduct) entities.get(i);
                mListProduct.add(orderProduct);
            }
        }
    }

    protected void showSpotProduct() {
        if (null != mListProduct && mListProduct.size() > 0) {
            SpotProductHomeComponent spotComponent = new SpotProductHomeComponent(mListProduct);
            View spotView = spotComponent.createView();
            mDelegate.showSpotProduct(spotView);
        }

    }

    @Override
    public void onResume() {
        showBanner();
        showCate();
        showSpotProduct();
    }

    public void setDelegate(HomeThemeOneDelegate delegate) {
        mDelegate = delegate;
    }
}
