package com.simicart.plugins.addressautofill.fragment;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simicart.core.base.component.SimiNavigationRowComponent;
import com.simicart.core.base.component.SimiRowComponent;
import com.simicart.core.base.component.SimiTextRowComponent;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.CountryEntity;
import com.simicart.core.customer.entity.StateEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martial on 9/5/2016.
 */
public class AddressAutoFillFragment extends SimiFragment {

    protected TextView tvLabel;
    protected GoogleApiClient mGoogleApiClient;
    protected Location currrentLocation;
    protected GoogleMap mGoogleMap;
    protected SupportMapFragment map;
    protected Address returnedAddress;
    protected ArrayList<SimiRowComponent> mListRowComponent;
    protected ArrayList<CountryEntity> mListCountry;

    protected String mStreet, mCity, mPostalCode, mCountryID;

    public static AddressAutoFillFragment newInstance() {
        return new AddressAutoFillFragment();
    }

    public void setListRowComponent(ArrayList<SimiRowComponent> mListRowComponent) {
        this.mListRowComponent = mListRowComponent;
    }

    public void setListCountry(ArrayList<CountryEntity> mListCountry) {
        this.mListCountry = mListCountry;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(Rconfig.getInstance().layout("plugins_addressautofill_map"), container, false);

        tvLabel = (TextView) rootView.findViewById(Rconfig.getInstance().id("tv_label"));
        tvLabel.setTextColor(AppColorConfig.getInstance().getContentColor());
        tvLabel.setText(SimiTranslator.getInstance().translate("Touch the map until you get your desired address"));

        new InitMapAsync().execute();

        ImageView ivCurrentLocation = (ImageView) rootView.findViewById(Rconfig.getInstance().id("bt_detect_location"));
        ivCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DataLocal.isAccessLocation() == true) {
                    requestGetCurrentLocation();
                } else {
                    String[] LOCATION_PERMS={
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                    };
                    if(Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                        if (DataLocal.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) == false
                                || DataLocal.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == false) {
                            SimiManager.getIntance().getCurrentActivity().requestPermissions(LOCATION_PERMS, KeyData.REQUEST_PERMISSIONS.LOCATION_REQUEST);
                        }
                    }
                }
            }
        });

        return rootView;
    }

    public class InitMapAsync extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            getMap();
        }

        @Override
        protected Void doInBackground(Void... params) {
            map = new SupportMapFragment();
            getChildFragmentManager().beginTransaction().replace(Rconfig.getInstance().getIdLayout("address_frame_map"), map).commit();

            return null;
        }
    }

    public void getMap() {
        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                if (DataLocal.isAccessLocation() == true) {
                    mGoogleMap.setMyLocationEnabled(true);
                }
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
                mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng arg0) {
                        mGoogleMap.clear();
                        if(currrentLocation == null) {
                            currrentLocation = new Location("");
                        }
                        currrentLocation.setLatitude(arg0.latitude);
                        currrentLocation.setLongitude(arg0.longitude);
                        addMakerToCurrentLocation();
                    }
                });
                if(currrentLocation != null) {
                    addMakerToCurrentLocation();
                }
            }
        });
    }

    public void addMakerToCurrentLocation() {
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(currrentLocation.getLatitude(), currrentLocation.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(Rconfig
                        .getInstance().getIdDraw("maker_my"))));
        new LocationAsync().execute();
    }

    public void requestGetCurrentLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        currrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (currrentLocation != null) {
                            mGoogleMap.clear();
                            addMakerToCurrentLocation();
                            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
                                    currrentLocation.getLatitude(), currrentLocation.getLongitude()));
                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
                            mGoogleMap.moveCamera(center);
                            mGoogleMap.animateCamera(zoom);
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

    public class LocationAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            getAddressFromLocation();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(returnedAddress != null) {
                showPickedAddress();
            }
        }
    }

    public void getAddressFromLocation() {
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            List<Address> addresses = geocoder.getFromLocation(currrentLocation.getLatitude(), currrentLocation.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                returnedAddress = addresses.get(0);
            }
        } catch (IOException e) {

        }
    }

    public void showPickedAddress() {
        mStreet = returnedAddress.getAddressLine(0) + ", "
                + returnedAddress.getAddressLine(1) + ", "
                + returnedAddress.getAddressLine(2);
        mCity = returnedAddress.getAdminArea();
        mPostalCode = returnedAddress.getPostalCode();
        mCountryID = returnedAddress.getCountryCode();

        for(SimiRowComponent row : mListRowComponent) {
            if(row.getKey().equals("country_name")) {
                if(Utils.validateString(mCountryID)) {
                    String countryName = getCountryName(mCountryID);
                    row.setValue(countryName);
                    row.updateView();
                }
            } else if(row.getKey().equals("city")) {
                if(Utils.validateString(mCity)) {
                    row.setValue(mCity);
                    ((SimiTextRowComponent)row).changeValue(mCity);
                }
            } else if(row.getKey().equals("street")) {
                if(Utils.validateString(mStreet)) {
                    row.setValue(mStreet);
                    ((SimiTextRowComponent)row).changeValue(mStreet);
                }
            } else if(row.getKey().equals("zip")) {
                if(Utils.validateString(mPostalCode)) {
                    row.setValue(mPostalCode);
                    ((SimiTextRowComponent)row).changeValue(mPostalCode);
                }
            } else if(row.getKey().equals("state_name")) {
                if(Utils.validateString(mCountryID)) {
                    ArrayList<String> states = getStateFromCountry(
                            getCountryName(mCountryID));
                    if (null != states && states.size() > 0) {
                        String state = states.get(0);
                        for(String item : states) {
                            if(Utils.validateString(item) && item.equals(mCity)) {
                                state = item;
                            }
                        }
                        row.setValue(state);
                        row.updateView();
                    } else {
                        ((SimiNavigationRowComponent)row).enableEdit();
                    }
                }
            }
        }
    }

    private String getCountryName(String country_code) {
        String country_name = "";
        for (int i = 0; i < mListCountry.size(); i++) {
            if (country_code.toLowerCase().equals(
                    mListCountry.get(i).getCode().toLowerCase())) {
                country_name = mListCountry.get(i).getName();
            }
        }
        return country_name;
    }

    public ArrayList<String> getStateFromCountry(String country) {
        ArrayList<String> states = new ArrayList<String>();
        for (CountryEntity countryAllowed : mListCountry) {
            if (countryAllowed.getName().equals(country)) {
                if(countryAllowed.getStateList() != null) {
                    for (StateEntity state : countryAllowed.getStateList()) {
                        states.add(state.getName());
                    }
                }
                return states;
            }
        }
        return states;
    }

}
