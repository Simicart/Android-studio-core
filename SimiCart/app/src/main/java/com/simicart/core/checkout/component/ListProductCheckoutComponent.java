package com.simicart.core.checkout.component;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.checkout.adapter.CartAdapter;
import com.simicart.core.checkout.entity.Cart;

import java.util.ArrayList;

/**
 * Created by Crabby PC on 7/1/2016.
 */
public class ListProductCheckoutComponent extends SimiComponent {

    protected ArrayList<Cart> listQuotes;
    protected SimiDelegate mDelegate;
    protected int layoutID;

    public ListProductCheckoutComponent(ArrayList<Cart> listQuotes, SimiDelegate delegate, int layoutID) {
        super();
        this.listQuotes = listQuotes;
        mDelegate = delegate;
        this.layoutID = layoutID;
    }

    @Override
    public View createView() {
        RecyclerView rvListProducts = new RecyclerView(mContext);
        rvListProducts.setNestedScrollingEnabled(false);
        LinearLayoutManager llManager = new LinearLayoutManager(mContext);
        rvListProducts.setLayoutManager(llManager);
        CartAdapter cartAdapter = new CartAdapter(listQuotes, layoutID, mDelegate);
        rvListProducts.setAdapter(cartAdapter);
        return rvListProducts;
    }
}
