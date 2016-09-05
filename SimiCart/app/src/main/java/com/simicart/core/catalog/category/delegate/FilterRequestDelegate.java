package com.simicart.core.catalog.category.delegate;

import com.simicart.core.catalog.categorydetail.entity.FilterEntity;
import com.simicart.core.catalog.categorydetail.entity.FilterState;


public interface FilterRequestDelegate {

	public void requestFilter(FilterEntity filterEntity);

	public void clearFilter(FilterState filter);

	public void clearAllFilter();
}
