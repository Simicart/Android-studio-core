package com.simicart.plugins.storelocator.delegate;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.plugins.storelocator.entity.StoreObject;

import java.util.ArrayList;

public interface StoreLocatorMapDelegate extends SimiDelegate {

    public void addMarkerToMap(ArrayList<StoreObject> listStores);

}
