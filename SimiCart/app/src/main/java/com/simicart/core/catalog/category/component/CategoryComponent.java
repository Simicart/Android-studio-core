package com.simicart.core.catalog.category.component;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.catalog.category.adapter.CategoryAdapter;
import com.simicart.core.catalog.category.entity.Category;

import java.util.ArrayList;

/**
 * Created by Martial on 8/18/2016.
 */
public class CategoryComponent extends SimiComponent {

    protected RecyclerView rvCategory;
    protected ArrayList<Category> listCategories;

    public CategoryComponent(ArrayList<Category> listCategories) {
        super();
        this.listCategories = listCategories;
    }

    @Override
    public View createView() {
        rootView = findLayout("core_component_category");

        rvCategory = (RecyclerView) findView("rv_category");
        rvCategory.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvCategory.setNestedScrollingEnabled(false);
        CategoryAdapter adapter = new CategoryAdapter(listCategories);
        rvCategory.setAdapter(adapter);

        return rootView;
    }
}
