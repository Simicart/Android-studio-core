package com.simicart.plugins.storelocator.block;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.GPSTracker;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.storelocator.adapter.StoreAdapter;
import com.simicart.plugins.storelocator.common.StoreLocatorConfig;
import com.simicart.plugins.storelocator.delegate.StoreLocatorStoreListDelegate;
import com.simicart.plugins.storelocator.entity.StoreObject;

import java.util.ArrayList;

public class StoreLocatorStoreListBlock extends SimiBlock implements StoreLocatorStoreListDelegate {

    protected RecyclerView listStore;
    protected LinearLayout llLayoutSearch;
    protected TextView tvStoreLocatorSearch;
    protected ProgressBar pbLoadMore;
    protected StoreAdapter adapter;
    protected ArrayList<StoreObject> listStoreObject;

    public StoreLocatorStoreListBlock(View view, Context context) {
        super(view, context);
        // TODO Auto-generated constructor stub
    }

    public void onListStoreScroll(RecyclerView.OnScrollListener listener) {
        listStore.setOnScrollListener(listener);
    }

    public void onSearchClick(OnClickListener listener) {
        llLayoutSearch.setOnClickListener(listener);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        listStore = (RecyclerView) mView.findViewById(Rconfig.getInstance().id("rv_list_store"));
        listStore.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        llLayoutSearch = (LinearLayout) mView
                .findViewById(Rconfig.getInstance()
                        .getIdLayout("layout_search"));
        tvStoreLocatorSearch = (TextView) mView
                .findViewById(Rconfig.getInstance()
                        .getIdLayout("storelocator_search"));
        tvStoreLocatorSearch.setText(SimiTranslator.getInstance().translate("Search By Area"));
        pbLoadMore = (ProgressBar) mView.findViewById(Rconfig.getInstance().id("progressBar_load"));
        pbLoadMore.setVisibility(View.GONE);
        listStoreObject = new ArrayList<>();
        adapter = new StoreAdapter(listStoreObject);
        listStore.setAdapter(adapter);
    }

    @Override
    public void drawView(SimiCollection collection) {
        // TODO Auto-generated method stub
        ArrayList<SimiEntity> entities = collection.getCollection();
        if (entities != null) {
            for (int i = 0; i < entities.size(); i++) {
                StoreObject store = (StoreObject) entities.get(i);
                listStoreObject.add(store);
            }

            if (listStoreObject.size() > 0) {
                adapter.notifyDataSetChanged();
                StoreLocatorConfig.mapController.addMarker(listStoreObject);
            }
        }
    }

    @Override
    public void visibleSearchLayout(boolean visible) {
        // TODO Auto-generated method stub
        if (visible == true) {
            llLayoutSearch.setVisibility(View.VISIBLE);
        } else {
            llLayoutSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoadMore(boolean isLoading) {
        // TODO Auto-generated method stub
        if (isLoading == true) {
            pbLoadMore.setVisibility(View.VISIBLE);
        } else {
            pbLoadMore.setVisibility(View.GONE);
        }
    }

    @Override
    public ArrayList<StoreObject> getListStore() {
        // TODO Auto-generated method stub
        return listStoreObject;
    }

    @Override
    public Location getCurrentLocation() {
        // TODO Auto-generated method stub
        GPSTracker gpsTracker = new GPSTracker(mContext);
        Location location = gpsTracker.getLocation();
        return location;
    }

}
