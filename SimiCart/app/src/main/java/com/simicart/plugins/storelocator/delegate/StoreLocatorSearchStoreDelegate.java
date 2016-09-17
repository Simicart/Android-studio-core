package com.simicart.plugins.storelocator.delegate;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.plugins.storelocator.entity.CountryObject;
import com.simicart.plugins.storelocator.entity.SearchObject;

import java.util.ArrayList;

public interface StoreLocatorSearchStoreDelegate extends SimiDelegate {

    public void setListConfig(ArrayList<String> listConfigs);

    public void setListCountry(ArrayList<CountryObject> listCountries);

    public void setListTag(ArrayList<String> listTags);

    public SearchObject getSearchObject();

}
