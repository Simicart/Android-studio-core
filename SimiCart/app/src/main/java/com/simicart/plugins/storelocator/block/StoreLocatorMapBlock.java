package com.simicart.plugins.storelocator.block;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.drawImage.SimiDrawImage;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.storelocator.common.StoreLocatorConfig;
import com.simicart.plugins.storelocator.delegate.StoreLocatorMapDelegate;
import com.simicart.plugins.storelocator.entity.StoreObject;
import com.simicart.plugins.storelocator.fragment.StoreDetailFragment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreLocatorMapBlock extends SimiBlock implements StoreLocatorMapDelegate {

    protected SupportMapFragment map;
    protected Bundle bundle;
    protected ArrayList<StoreObject> listStore;
    protected GoogleMap mGoogleMap;
    protected Location currrentLocation = null;
    protected LatLng end;
    protected FragmentManager childFragmentManager;
    protected GoogleApiClient mGoogleApiClient;
    protected boolean isAddMaker = false;

    public StoreLocatorMapBlock(View view, Context context) {
        super(view, context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        listStore = new ArrayList<>();

        new InitMapAsync().execute();

    }

    public class InitMapAsync extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            getMap();
        }

        @Override
        protected Void doInBackground(Void... params) {
            map = new SupportMapFragment();
            childFragmentManager.beginTransaction().replace(Rconfig.getInstance().getIdLayout("frame_map"), map).commit();

            return null;
        }
    }

    public void getMap() {
        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                if (DataLocal.isAccessLocation() == true) {
                    mGoogleMap.setMyLocationEnabled(true);
                    requestGetCurrentLocation();
                }
                mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        marker.showInfoWindow();
                        return true;
                    }
                });
                mGoogleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker maker) {
                        onClickInfoWindow(maker);
                    }
                });
                mGoogleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        return gotInforContent(marker);
                    }
                });
                if (isAddMaker == false && listStore.size() > 0) {
                    isAddMaker = true;
                    mGoogleMap.clear();
                    addMaker(listStore);
                }
            }
        });
    }

    public void onClickInfoWindow(Marker maker) {
        for (int i = 0; i < listStore.size(); i++) {

            StoreObject store = listStore.get(i);
            String latitude = store.getLatitude();
            String longtitude = store.getLongtitude();
            if (Utils.validateString(latitude) && Utils.validateString(longtitude)) {
                latitude = latitude.trim();
                longtitude = longtitude.trim();

                String dLat = latitude;
                String dLong = longtitude;

                String makerLat = String.valueOf(maker.getPosition().latitude);
                String makerLong = String.valueOf(maker.getPosition().longitude);

                if (dLat.equals(makerLat) && dLong.equals(makerLong)) {
                    HashMap<String, Object> hmData = new HashMap<String, Object>();
                    hmData.put(Constants.KeyData.STORE_OBJECT, store);
                    StoreDetailFragment detail = StoreDetailFragment.newInstance(new SimiData(hmData));
                    if (DataLocal.isTablet) {
                        SimiManager.getIntance().addPopupFragment(detail);
                    } else {
                        SimiManager.getIntance().replaceFragment(detail);
                    }
                    return;
                }
            }
        }
    }

    public void requestGetCurrentLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        currrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (currrentLocation != null) {
                            mGoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(currrentLocation.getLatitude(), currrentLocation.getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromResource(Rconfig.getInstance().getIdDraw("maker_my"))));
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void drawView(SimiCollection collection) {
        // TODO Auto-generated method stub
    }

    private void addMaker(ArrayList<StoreObject> listStores) {
        for (int i = 0; i < listStores.size(); i++) {

            StoreObject store = listStores.get(i);
            String latitude = store.getLatitude();
            String longtitude = store.getLongtitude();
            if (Utils.validateString(latitude) && Utils.validateString(longtitude)) {

                latitude = latitude.trim();
                longtitude = longtitude.trim();
                try {

                    Double dLat = convertToDouble(latitude);
                    Double dLong = convertToDouble(longtitude);

                    LatLng end = new LatLng(dLat, dLong);
                    MarkerOptions options = new MarkerOptions().position(end).icon(BitmapDescriptorFactory
                            .fromResource(Rconfig.getInstance().drawable("plugins_locator_maker_default")));
                    mGoogleMap.addMarker(options);
                } catch (Exception e) {
                    Log.e("StoreLocatorFragment ", "=====================> addMaker Exceptin " + e.getMessage());
                }
            }
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng position;
        for (int i = 0; i < listStores.size(); i++) {
            StoreObject store = listStores.get(i);
            Log.e("abc", "Lat====" + store.getLatitude() + "====Long====" + store.getLongtitude());
            position = new LatLng(getLatDoubleWithStore(store), getLongDoubleWithStore(store));
            builder.include(new LatLng(position.latitude, position.longitude));
        }
        LatLngBounds bounds = builder.build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));

    }

    protected Double convertToDouble(String source) {
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        Double target = null;
        try {
            target = df.parse(source).doubleValue();
        } catch (ParseException e) {
            Log.e("MapViewFragment ", "=======================> convertToDouble " + e.getMessage());
        }

        return target;
    }

    protected View gotInforContent(Marker marker) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        int idView = Rconfig.getInstance().layout("plugins_store_locator_info_window_layout");
        View v = inflater.inflate(idView, null);
        v.setMinimumWidth(200);
        v.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
        TextView tvName = (TextView) v.findViewById(Rconfig.getInstance().getIdLayout("tv_name"));
        TextView tvAddress = (TextView) v.findViewById(Rconfig.getInstance().getIdLayout("tv_address"));
        CircleImageView img = (CircleImageView) v.findViewById(Rconfig.getInstance().getIdLayout("img_store"));

        LatLng latLngMarker = marker.getPosition();
        String latMarker = String.valueOf(latLngMarker.latitude);
        String longMarker = String.valueOf(latLngMarker.longitude);
        String latCurrent = "";
        String longCurrent = "";

        if (currrentLocation != null) {
            latCurrent = String.valueOf(currrentLocation.getLatitude());
            longCurrent = String.valueOf(currrentLocation.getLongitude());
        }

        if (latMarker.equals(latCurrent) && longMarker.equals(longCurrent)) {
            tvName.setText(SimiTranslator.getInstance().translate("You are here"));
            tvAddress.setText("");
        } else if (!latMarker.equals(latCurrent) && !longMarker.equals(longCurrent)) {

            for (StoreObject store : listStore) {

                String latStore = store.getLatitude();
                String longStore = store.getLongtitude();

                if (latMarker.equals(latStore) && longMarker.equals(longStore)) {
                    tvName.setText(store.getName());
                    tvAddress.setText(StoreLocatorConfig.convertAddress(store));

                    String urlImage = store.getImage_icon();

                    if (Utils.validateString(urlImage)) {
                        SimiDrawImage drawImage = new SimiDrawImage();
                        drawImage.drawImage(img, urlImage);
                    }

                    return v;

                } else {
                    img.setImageDrawable(mContext.getResources()
                            .getDrawable(Rconfig.getInstance().getIdDraw("plugins_locator_ic_store_android")));
                }

            }
        }

        return v;
    }

    protected Double getLatDoubleWithStore(StoreObject store) {
        String latitude = store.getLatitude();
        if (Utils.validateString(latitude)) {
            latitude = latitude.trim();

            Double dLat = convertToDouble(latitude);
            return dLat;
        }

        return null;
    }

    protected Double getLongDoubleWithStore(StoreObject store) {
        String longtitude = store.getLongtitude();
        if (Utils.validateString(longtitude)) {
            longtitude = longtitude.trim();
            Double dLong = convertToDouble(longtitude);
            return dLong;
        }

        return null;
    }

    @Override
    public void addMarkerToMap(ArrayList<StoreObject> listStores) {
        // TODO Auto-generated method stub
        listStore = listStores;

        if (mGoogleMap != null) {
            isAddMaker = true;
            mGoogleMap.clear();
            addMaker(listStores);
        }

    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public void setChildFragmentManager(FragmentManager childFragmentManager) {
        this.childFragmentManager = childFragmentManager;
    }
}
