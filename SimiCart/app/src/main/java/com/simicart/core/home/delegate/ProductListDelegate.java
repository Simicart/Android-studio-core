package com.simicart.core.home.delegate;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.catalog.product.entity.ProductList;

import java.util.ArrayList;

public interface ProductListDelegate extends SimiDelegate{
	public void onUpdate(ArrayList<ProductList> productList);
}
