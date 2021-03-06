package com.simicart.core.common.options.delegate;

import com.simicart.core.catalog.product.entity.ProductOption;

import java.util.ArrayList;

public interface CacheOptionSingleDelegate {

    public void onSendDependOption(ArrayList<String> dependList,
                                   String currentID);

    public void updateStateCacheOption(String id, boolean isSeletecd);

    public void updatePriceForHeader(String price);

    public void updatePriceParent(ProductOption option, boolean operation);

    public void clearCheckAll(String id);

}
