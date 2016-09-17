package com.simicart.plugins.addressautofill.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.simicart.core.base.component.SimiNavigationRowComponent;
import com.simicart.core.base.component.SimiRowComponent;
import com.simicart.core.base.component.SimiTextRowComponent;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.CountryEntity;
import com.simicart.core.customer.entity.StateEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    protected ProgressDialog pd_loading;

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

        if (mGoogleMap == null) {
            new InitMapAsync().execute();
        }

        ImageView ivCurrentLocation = (ImageView) rootView.findViewById(Rconfig.getInstance().id("bt_detect_location"));
        ivCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataLocal.isAccessLocation() == true) {
                    requestGetCurrentLocation();
                } else {
                    String[] LOCATION_PERMS = {
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                    };
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                        if (DataLocal.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) == false
                                || DataLocal.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == false) {
                            SimiManager.getIntance().getCurrentActivity().requestPermissions(LOCATION_PERMS, KeyData.REQUEST_PERMISSIONS.LOCATION_REQUEST);
                        }
                    }
                }
            }
        });

        pd_loading = ProgressDialog.show(SimiManager.getIntance().getCurrentActivity(), null, null, true, false);
        pd_loading.setContentView(Rconfig.getInstance().layout(
                "core_base_loading"));
        pd_loading.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pd_loading.setCanceledOnTouchOutside(false);
        pd_loading.setCancelable(false);
        pd_loading.dismiss();

        return rootView;
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
                        if (currrentLocation == null) {
                            currrentLocation = new Location("");
                        }
                        currrentLocation.setLatitude(arg0.latitude);
                        currrentLocation.setLongitude(arg0.longitude);
                        addMakerToCurrentLocation();
                    }
                });
                if (currrentLocation != null) {
                    addMakerToCurrentLocation();
                }
            }
        });
    }

    public void addMakerToCurrentLocation() {
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(currrentLocation.getLatitude(), currrentLocation.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(Rconfig
                        .getInstance().getIdDraw("maker_my"))));
        pd_loading.show();
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

    public void getAddressFromLocation() {
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            List<Address> addresses = geocoder.getFromLocation(currrentLocation.getLatitude(), currrentLocation.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                returnedAddress = addresses.get(0);
            }
        } catch (IOException e) {
            Log.e("getAddressFromLocation", e.getMessage());
            Address address = getAddressFromGoogleMap(currrentLocation.getLatitude(), currrentLocation.getLongitude());
            if (address != null) {
                returnedAddress = address;
            }
        }
    }

    public void showPickedAddress() {
        mStreet = returnedAddress.getAddressLine(0) + ", "
                + returnedAddress.getAddressLine(1) + ", "
                + returnedAddress.getAddressLine(2);
        mCity = returnedAddress.getAdminArea();
        mPostalCode = returnedAddress.getPostalCode();
        mCountryID = returnedAddress.getCountryCode();

        for (SimiRowComponent row : mListRowComponent) {
            if (row.getKey().equals("country_name")) {
                if (Utils.validateString(mCountryID)) {
                    String countryName = getCountryName(mCountryID);
                    row.setValue(countryName);
                    row.updateView();
                }
            } else if (row.getKey().equals("city")) {
                if (Utils.validateString(mCity)) {
                    row.setValue(mCity);
                    ((SimiTextRowComponent) row).changeValue(mCity);
                }
            } else if (row.getKey().equals("street")) {
                if (Utils.validateString(mStreet)) {
                    row.setValue(mStreet);
                    ((SimiTextRowComponent) row).changeValue(mStreet);
                }
            } else if (row.getKey().equals("zip")) {
                if (Utils.validateString(mPostalCode)) {
                    row.setValue(mPostalCode);
                    ((SimiTextRowComponent) row).changeValue(mPostalCode);
                }
            } else if (row.getKey().equals("state_name")) {
                if (Utils.validateString(mCountryID)) {
                    ArrayList<String> states = getStateFromCountry(
                            getCountryName(mCountryID));
                    if (null != states && states.size() > 0) {
                        String state = states.get(0);
                        for (String item : states) {
                            if (Utils.validateString(item) && item.equals(mCity)) {
                                state = item;
                            }
                        }
                        row.setValue(state);
                        row.updateView();
                    } else {
                        ((SimiNavigationRowComponent) row).enableEdit();
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
                if (countryAllowed.getStateList() != null) {
                    for (StateEntity state : countryAllowed.getStateList()) {
                        states.add(state.getName());
                    }
                }
                return states;
            }
        }
        return states;
    }

    public Address getAddressFromGoogleMap(double lat, double lng) {

        String url = String
                .format(Locale.ENGLISH, "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="
                        + Locale.getDefault().getCountry(), lat, lng);

        HttpURLConnection urlConnection = null;
        StringBuilder stringBuilder = new StringBuilder();
        Address address = new Address(Locale.getDefault());
        try {
            URL url_connection = new URL(url);
            urlConnection = (HttpURLConnection) url_connection
                    .openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);
            int b;
            while ((b = isw.read()) != -1) {
                stringBuilder.append((char) b);
            }

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());

            if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    Log.e("abc", result.toString());
                    parseAddressData(address, result);
                }
            }
        } catch (Exception e) {
            Log.e("getLocationFromString ", "Exception " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return address;
    }

    public void parseAddressData(Address address, JSONObject object) {
        try {
            if (object.has("types")) {
                JSONArray typesArr = object.getJSONArray("types");
                String formattedAddress = null;
                if (object.has("formatted_address")) {
                    formattedAddress = object.getString("formatted_address");
                }
                if (typesArr.length() > 0 && formattedAddress != null) {
                    String type = typesArr.getString(0);
                    if (type.equals("street_address") || type.equals("route")) {
                        address.setAddressLine(0, formattedAddress);
                        if (object.has("address_components")) {
                            JSONArray componentsArr = object.getJSONArray("address_components");
                            for (int i = 0; i < componentsArr.length(); i++) {
                                JSONObject component = componentsArr.getJSONObject(i);
                                if (component.has("types")) {
                                    JSONArray arr = component.getJSONArray("types");
                                    if (arr.length() > 0) {
                                        String componentType = arr.getString(0);
                                        if (componentType.equals("country")) {
                                            if (component.has("short_name")) {
                                                address.setCountryCode(component.getString("short_name"));
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (type.equals("postal_code")) {
                        address.setPostalCode(getOnlyNumberics(formattedAddress));
                    } else if (type.equals("administrative_area_level_1")) {
                        String[] addressSplited = formattedAddress.split(", ");
                        address.setAdminArea(addressSplited[0]);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getOnlyNumberics(String str) {

        if (str == null) {
            return null;
        }

        StringBuffer strBuff = new StringBuffer();
        char c;

        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);

            if (Character.isDigit(c)) {
                strBuff.append(c);
            }
        }
        return strBuff.toString();
    }

    public class InitMapAsync extends AsyncTask<Void, Void, Void> {

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

    public class LocationAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            getAddressFromLocation();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pd_loading.dismiss();
            if (returnedAddress != null) {
                showPickedAddress();
            }
        }
    }

}
