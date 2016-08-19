package com.simicart.core.slidemenu.delegate;

import com.simicart.core.catalog.category.delegate.CategoryDelegate;

/**
 * Created by Martial on 8/19/2016.
 */
public interface CategorySlideMenuDelegate extends CategoryDelegate {

    public void showBack(boolean show);

    public void updateCategoryParent(String name);

}
