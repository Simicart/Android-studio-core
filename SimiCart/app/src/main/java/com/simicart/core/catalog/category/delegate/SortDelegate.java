package com.simicart.core.catalog.category.delegate;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.catalog.category.entity.Sort;

import java.util.ArrayList;

public interface SortDelegate extends SimiDelegate {

	public void setSort_option(String mSort);
	public void setListSort(ArrayList<Sort> mListSort);
	public String getSortTag();

}
