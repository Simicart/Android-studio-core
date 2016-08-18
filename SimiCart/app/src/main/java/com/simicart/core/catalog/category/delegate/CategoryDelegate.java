package com.simicart.core.catalog.category.delegate;

import android.view.View;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.catalog.category.entity.Category;

import java.util.ArrayList;

public interface CategoryDelegate extends SimiDelegate {

    public void onUpdateData(ArrayList<Category> categories);

	public void onSelectedItem(int position);

    public void showListCategory(View view);

    public void showListProducts(View view);

}
