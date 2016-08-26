package com.simicart.theme.ztheme.home.delegate;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.theme.ztheme.home.entity.ZThemeCatalogEntity;

import java.util.ArrayList;

public interface HomeZThemeDelegate extends SimiDelegate {

	public ArrayList<ZThemeCatalogEntity> getListCatalog();

}
