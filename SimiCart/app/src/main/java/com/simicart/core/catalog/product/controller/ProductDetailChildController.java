package com.simicart.core.catalog.product.controller;

import android.util.Log;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.product.delegate.ProductDetailAdapterDelegate;
import com.simicart.core.catalog.product.delegate.ProductDetailChildDelegate;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.model.ProductModel;

import java.util.ArrayList;

public class ProductDetailChildController extends SimiController {

    protected ProductDetailChildDelegate mDelegate;
    protected ProductDetailAdapterDelegate mAdapterDelegate;
    protected ProductDetailParentController mController;
    protected String mID;

    @Override
    public void onStart() {
        requestData(mID);
    }

    @Override
    public void onResume() {
        mDelegate.updateView(mModel.getCollection());
        String current_id = mAdapterDelegate.getCurrentID();
        if (getProductFromCollection() != null) {
            String id = getProductFromCollection().getId();
            if (current_id != null && current_id.equals(id)) {
                mDelegate.updateIndicator();
            }
        }
    }

    protected void requestData(final String id) {
        mDelegate.showLoading();
        mModel = new ProductModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                mDelegate.updateView(mModel.getCollection());

                if (null != mAdapterDelegate) {
                    String current_id = mAdapterDelegate.getCurrentID();
                    String id = getProductFromCollection().getId();
                    if (current_id != null && current_id.equals(id)) {
                        mController
                                .onUpdateTopBottom((ProductModel) mModel);
                        Log.e("ProductDetailChildController ",
                                "requestData " + id);
                        mDelegate.updateIndicator();
                    }
                }
            }
        });
        mModel.addBody("product_id", id);
        mModel.addBody("width", "600");
        mModel.addBody("height", "600");
        mModel.request();
    }

    public void onUpdateTopBottom() {
        if (null != mModel && null != mController) {
            Log.e("ProductDetailChildController ", "onUpdateTopBottom");
            mController.onUpdateTopBottom((ProductModel) mModel);
        } else {
            Log.e("ProductDetailChildController ", "onUpdateTopBottom NULL");
        }
    }

    protected Product getProductFromCollection() {
        Product product = null;
        ArrayList<SimiEntity> entity = mModel.getCollection().getCollection();
        if (null != entity && entity.size() > 0) {
            product = (Product) entity.get(0);
        }
        return product;
    }

    public void setParentController(ProductDetailParentController controller) {
        mController = controller;
    }

    public void setAdapterDelegate(ProductDetailAdapterDelegate delegate) {
        mAdapterDelegate = delegate;
    }


    public void setProductID(String id) {
        mID = id;
    }

    public void setDelegate(ProductDetailChildDelegate delegate) {
        mDelegate = delegate;
    }

}
