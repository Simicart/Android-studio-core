package com.simicart.core.home.controller;

import java.util.ArrayList;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.product.entity.ProductList;
import com.simicart.core.home.delegate.ProductListDelegate;
import com.simicart.core.home.model.SportProductDefaultModel;

public class ProductListHomeController extends SimiController {
    protected ProductListDelegate mDelegate;
    protected ArrayList<ProductList> mProductList;

    public ProductListHomeController() {
        this.mProductList = new ArrayList<ProductList>();
    }

    public void setDelegate(ProductListDelegate delegate) {
        this.mDelegate = delegate;
    }

    @Override
    public void onStart() {
        mDelegate.showLoading();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                ArrayList<SimiEntity> entity = mModel.getCollection()
                        .getCollection();
                for (SimiEntity simiEntity : entity) {
                    ProductList product = new ProductList();
                    product.setJSONObject(simiEntity.getJSONObject());
                    mProductList.add(product);
                }
                mDelegate.onUpdate(mProductList);
            }
        });
        mModel = new SportProductDefaultModel();
        mModel.addBody("limit", "15");
        mModel.addBody("width", "200");
        mModel.addBody("height", "200");
        mModel.request();
    }

    @Override
    public void onResume() {
        mDelegate.onUpdate(mProductList);

    }
}
