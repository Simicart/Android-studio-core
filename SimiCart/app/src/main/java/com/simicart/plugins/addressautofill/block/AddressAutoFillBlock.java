package com.simicart.plugins.addressautofill.block;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

/**
 * Created by Martial on 9/5/2016.
 */
public class AddressAutoFillBlock extends SimiBlock {

    protected GoogleApiClient mGoogleApiClient;
    protected Location currrentLocation = null;
    protected GoogleMap mGoogleMap;
    protected SupportMapFragment map;
    protected FragmentManager childFragmentManager;

    public AddressAutoFillBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {
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
            childFragmentManager.beginTransaction().replace(Rconfig.getInstance().getIdLayout("address_frame_map"), map).commit();

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
                mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        marker.showInfoWindow();
                        return true;
                    }
                });
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            }
        });
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

    public void setChildFragmentManager(FragmentManager childFragmentManager) {
        this.childFragmentManager = childFragmentManager;
    }
}
