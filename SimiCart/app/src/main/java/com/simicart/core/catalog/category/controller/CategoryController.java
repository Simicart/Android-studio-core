package com.simicart.core.catalog.category.controller;

import android.view.View;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.category.component.CategoryComponent;
import com.simicart.core.catalog.category.delegate.CategoryDelegate;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.catalog.category.model.CategoryModel;
import com.simicart.core.catalog.category.model.ListProductModel;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.entity.ProductList;
import com.simicart.core.config.DataLocal;
import com.simicart.core.home.component.SpotProductComponent;

import java.util.ArrayList;

public class CategoryController extends SimiController {

    protected CategoryDelegate mDelegate;
    protected String mID;
    protected CategoryComponent categoryComponent;
    protected ListProductModel listProductModel;

    @Override
    public void onStart() {
        requestListCategories();
        if (!DataLocal.isTablet) {
            requestListProducts();
        }
    }

    public void requestListCategories() {
        mDelegate.showLoading();
        mModel = new CategoryModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                drawListCategory(((CategoryModel) mModel).getListCategory());
                mDelegate.updateView(mModel.getCollection());
            }
        });
        ((CategoryModel) mModel).setCategoryID(mID);
        if (!mID.equals("-1")) {
            ((CategoryModel) mModel).setCategoryID(mID);
            mModel.addBody("category_id", mID);
        }
        mModel.request();

    }

    public void requestListProducts() {
        listProductModel = new ListProductModel();
        listProductModel.setCategoryID(mID);
        listProductModel.addBody("category_id", mID);
        listProductModel.addBody("offset", "0");
        listProductModel.addBody("limit", "10");
        listProductModel.addBody("width", "300");
        listProductModel.addBody("height", "300");
        listProductModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                drawListProducts(listProductModel.getListProducts());
            }
        });
        listProductModel.request();

    }

    public void drawListCategory(ArrayList<Category> listCategories) {
        categoryComponent = new CategoryComponent(listCategories);
        View categoryView = categoryComponent.createView();
        mDelegate.showListCategory(categoryView);
    }

    public void drawListProducts(ArrayList<Product> listProducts) {
        View listProductView = null;
        if(listProducts.size() > 0) {
            ProductList productList = new ProductList();
            productList.setSpotProduct(listProducts);
            SpotProductComponent listProductComponent = new SpotProductComponent(productList);
            listProductView = listProductComponent.createView();
        }
        mDelegate.showListProducts(listProductView);
    }

    @Override
    public void onResume() {
        drawListCategory(((CategoryModel) mModel).getListCategory());
        drawListProducts(listProductModel.getListProducts());
        mDelegate.updateView(mModel.getCollection());
    }

    public void setDelegate(CategoryDelegate delegate) {
        mDelegate = delegate;
    }

    public void setCategoryID(String id) {
        mID = id;
    }

}
