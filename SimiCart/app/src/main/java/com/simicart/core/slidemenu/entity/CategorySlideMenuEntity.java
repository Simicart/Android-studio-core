package com.simicart.core.slidemenu.entity;

import com.simicart.core.catalog.category.component.CategoryComponent;

/**
 * Created by Martial on 8/19/2016.
 */
public class CategorySlideMenuEntity {

    protected String categoryName;
    protected CategoryComponent categoryComponent;

    public CategoryComponent getCategoryComponent() {
        return categoryComponent;
    }

    public void setCategoryComponent(CategoryComponent categoryComponent) {
        this.categoryComponent = categoryComponent;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
