package com.simicart.theme.ztheme.home.delegate;

import java.util.ArrayList;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.theme.ztheme.home.entity.ZThemeCatalogEntity;

public interface HomeZThemeDelegate extends SimiDelegate {

	void showCatSub(ZThemeCatalogEntity category);

	void setSelection(int position);

	void setCategoryTree(ArrayList<ZThemeCatalogEntity> categoriesTree);

}
