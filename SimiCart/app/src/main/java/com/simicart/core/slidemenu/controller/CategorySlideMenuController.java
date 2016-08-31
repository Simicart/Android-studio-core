package com.simicart.core.slidemenu.controller;

import android.view.View;

import com.simicart.core.catalog.category.controller.CategoryController;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.slidemenu.entity.CategorySlideMenuEntity;
import com.simicart.core.slidemenu.fragment.SlideMenuFragment;

import java.util.ArrayList;

/**
 * Created by Martial on 8/19/2016.
 */
public class CategorySlideMenuController extends CategoryController {

    protected ArrayList<CategorySlideMenuEntity> listCategoryComponents;
    protected View.OnClickListener onBackClick;
    protected String categoryName;

    public View.OnClickListener getOnBackClick() {
        return onBackClick;
    }

    public void setCategoryName(String name) {
        categoryName = name;
    }

    @Override
    public void onStart() {
        super.onStart();
        listCategoryComponents = new ArrayList<>();

        onBackClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreviousCat();
            }
        };

    }

    @Override
    public void drawListCategory(ArrayList<Category> listCategories) {
        super.drawListCategory(listCategories);
        CategorySlideMenuEntity slideMenuEntity = new CategorySlideMenuEntity();
        slideMenuEntity.setCategoryName(categoryName);
        slideMenuEntity.setCategoryComponent(categoryComponent);
        listCategoryComponents.add(slideMenuEntity);
        checkShowBackButton();
        mDelegate.updateCategoryParent(categoryName);
    }

    public void backToPreviousCat() {
        listCategoryComponents.remove(listCategoryComponents.size() - 1);
        checkShowBackButton();
        CategorySlideMenuEntity categorySlideMenuEntity = listCategoryComponents.get(listCategoryComponents.size() - 1);
        mDelegate.showListCategory(categorySlideMenuEntity.getCategoryComponent().createView());
        mDelegate.updateCategoryParent(categorySlideMenuEntity.getCategoryName());
    }

    public void openSubCategory(String categoryID, String categoryName) {
        checkShowBackButton();
        mID = categoryID;
        this.categoryName = categoryName;
        requestListCategories();
    }

    public void showRootCategory() {
        while (true) {
            if(listCategoryComponents.size() == 1) {
                break;
            }
            backToPreviousCat();
        }
    }

    protected void checkShowBackButton() {
        if(listCategoryComponents.size() > 1) {
            mDelegate.showBack(true);
        } else {
            mDelegate.showBack(false);
        }
    }

}
