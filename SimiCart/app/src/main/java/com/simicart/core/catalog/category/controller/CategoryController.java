package com.simicart.core.catalog.category.controller;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.category.component.CategoryComponent;
import com.simicart.core.catalog.category.delegate.CategoryDelegate;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.catalog.category.model.CategoryModel;
import com.simicart.core.catalog.category.model.ListProductModel;
import com.simicart.core.catalog.listproducts.fragment.ProductListFragment;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.entity.ProductList;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.home.component.SpotProductComponent;
import com.simicart.core.slidemenu.fragment.CateSlideMenuFragment;

import java.util.ArrayList;

public class CategoryController extends SimiController {
    protected CategoryDelegate mDelegate;
    protected String mID;

    @Override
    public void onStart() {
        requestListCategories();
        if (!DataLocal.isTablet) {
            requestListProducts();
        }
    }

    private void requestListCategories() {
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

    private void requestListProducts() {
        final ListProductModel model = new ListProductModel();
        model.setCategoryID(mID);
        model.addBody("category_id", mID);
        model.addBody("offset", "0");
        model.addBody("limit", "10");
        model.addBody("width", "300");
        model.addBody("height", "300");
        model.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                drawListProducts(model.getListProducts());
            }
        });
        model.request();

    }

    protected void drawListCategory(ArrayList<Category> listCategories) {
        CategoryComponent categoryComponent = new CategoryComponent(listCategories);
        View categoryView = categoryComponent.createView();
        mDelegate.showListCategory(categoryView);
    }

    protected void drawListProducts(ArrayList<Product> listProducts) {
        ProductList productList = new ProductList();
        productList.setSpotProduct(listProducts);
        SpotProductComponent listProductComponent = new SpotProductComponent(productList);
        View listProductView = listProductComponent.createView();
        mDelegate.showListProducts(listProductView);
    }

    @Override
    public void onResume() {
        mDelegate.updateView(mModel.getCollection());
    }

    public void setDelegate(CategoryDelegate delegate) {
        mDelegate = delegate;
    }

    public void setCategoryID(String id) {
        mID = id;
    }

}
