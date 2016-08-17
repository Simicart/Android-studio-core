package com.simicart.core.home.controller;

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
import com.simicart.core.catalog.product.entity.ProductList;
import com.simicart.core.home.component.BannerComponent;
import com.simicart.core.home.component.CateHomeComponent;
import com.simicart.core.home.component.SpotProductComponent;
import com.simicart.core.home.delegate.HomeDelegate;
import com.simicart.core.home.model.CategoryHomeModel;
import com.simicart.core.home.model.SportProductDefaultModel;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by frank on 17/08/2016.
 */
public class HomeController extends SimiController {

    protected ArrayList<BannerEntity> mListBanner;
    protected ArrayList<Category> mListCate;
    protected ArrayList<ProductList> mProductList;
    protected HomeDelegate mDelegate;


    @Override
    public void onStart() {
        initListener();

        requestBanner();

        requestCateHome();

        requestSpotProduct();
    }

    protected void initListener() {

    }

    protected void requestBanner() {
        BannerModel bannerModel = new BannerModel();
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

    protected void requestCateHome() {
        CategoryHomeModel categoryHomeModel = new CategoryHomeModel();
        categoryHomeModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                ArrayList<SimiEntity> entities = collection.getCollection();
                if (null != entities && entities.size() > 0) {
                    parseListCate(entities);
                }
                showCate();
            }
        });

        categoryHomeModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {

            }
        });

        categoryHomeModel.request();

    }

    protected void requestSpotProduct() {
        final SportProductDefaultModel sportProductDefaultModel = new SportProductDefaultModel();
        sportProductDefaultModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                ArrayList<SimiEntity> entities = collection.getCollection();
                if (null != entities && entities.size() > 0) {
                    parseSportProduct(entities);
                }
                showSportProduct();
            }
        });

        sportProductDefaultModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {

            }
        });

        sportProductDefaultModel.addBody("limit", "10");
        sportProductDefaultModel.request();
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
        if (mListCate.size() == 0) {
            mDelegate.showCateHome(null);
        } else {
            CateHomeComponent cateHomeComponent = new CateHomeComponent(mListCate);
            View cateHomeView = cateHomeComponent.createView();
            mDelegate.showCateHome(cateHomeView);
        }
    }

    protected void parseSportProduct(ArrayList<SimiEntity> entities) {
        mProductList = new ArrayList<>();
        for (SimiEntity simiEntity : entities) {
            ProductList product = new ProductList();
            product.parse(simiEntity.getJSONObject());
            mProductList.add(product);
        }
    }

    protected void showSportProduct() {
        ArrayList<View> views = new ArrayList<>();
        if (null != mProductList && mProductList.size() > 0) {
            for (int i = 0; i < mProductList.size(); i++) {
                ProductList productList = mProductList.get(i);
                SpotProductComponent spotProductComponent = new SpotProductComponent(productList);
                View sportProductView = spotProductComponent.createView();
                views.add(sportProductView);
            }
        }
        mDelegate.showSpotProduct(views);
    }


    @Override
    public void onResume() {
        showBanner();
        showCate();
        showSportProduct();
    }

    public void setDelegate(HomeDelegate delegate) {
        mDelegate = delegate;
    }
}
