package com.simicart.plugins.storelocator.fragment;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simicart.core.base.drawImage.SimiDrawImage;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.GPSTracker;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.storelocator.common.MathForDummies;
import com.simicart.plugins.storelocator.common.ShowMapError;
import com.simicart.plugins.storelocator.common.StoreLocatorConfig;
import com.simicart.plugins.storelocator.entity.StoreObject;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapViewFragment extends SimiFragment {
    private View view;
    SupportMapFragment mMapView;
    private Bundle bundle;
    GoogleMap ggmap;
    StoreObject storeObject;
    private int page = 0;
    List<StoreObject> store_maker;
    LatLng start;
    Activity mActivity;

    public static MapViewFragment newInstance(SimiData data) {
        MapViewFragment map = new MapViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DATA, data);
        map.setArguments(bundle);
        // map.storeObject = storeObject;
        return map;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        bundle = savedInstanceState;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(
                Rconfig.getInstance().getId("plugins_store_locator_map",
                        "layout"), null);
        if (mData != null) {
            storeObject = (StoreObject) getValueWithKey(Constants.KeyData.STORE_OBJECT);
        }

        store_maker = new ArrayList<StoreObject>();
        new InitMapAsync().execute();
//		try {
//			MapsInitializer.initialize(getActivity());
//		} catch (Exception e) {
//		}
        //mMapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(Rconfig.newInstance().getIdLayout("map"));
//		mMapView.onCreate(bundle);
//		mMapView.onResume();

        Double dLat = getLatDoubleWithStore(storeObject);
        Double dLong = getLongDoubleWithStore(storeObject);

        start = new LatLng(dLat, dLong);

        return view;
    }

    public class InitMapAsync extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            getMap();
        }

        @Override
        protected Void doInBackground(Void... params) {
            mMapView = new SupportMapFragment();
            getChildFragmentManager().beginTransaction().add(Rconfig.getInstance().getIdLayout("frame_map"), mMapView).commit();

            return null;
        }
    }

    public void getMap() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                ggmap = googleMap;
                if (ggmap == null) {
                    new ShowMapError(mActivity).showDiagloError(
                            SimiTranslator.getInstance().translate("Error"),
                            SimiTranslator.getInstance().translate(
                                    "First, You must update Google Maps."));
                    ;
                    return;
                }
                ggmap.getUiSettings().setMyLocationButtonEnabled(false);
                if (DataLocal.isAccessLocation() == true) {
                    ggmap.setMyLocationEnabled(true);
                }
                ggmap.addMarker(new MarkerOptions().position(start).icon(
                        BitmapDescriptorFactory.fromResource(Rconfig.getInstance()
                                .getIdDraw("plugins_locator_maker_store"))));
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
                        start.latitude, start.longitude));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                ggmap.moveCamera(center);
                ggmap.animateCamera(zoom);
//		ggmap.setOnCameraChangeListener(new OnCameraChangeListener() {
//
//			@Override
//			public void onCameraChange(CameraPosition camera) {
//				TaskLoadMaker loadMaker = new TaskLoadMaker();
//				loadMaker.data = putData(
//						String.valueOf(camera.target.latitude),
//						String.valueOf(camera.target.longitude),
//						String.valueOf(page * 10));
//				loadMaker.execute();
//			}
//		});
                ggmap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker maker) {
                        for (int i = 0; i < store_maker.size(); i++) {

                            StoreObject store = store_maker.get(i);
                            Double dLat = getLatDoubleWithStore(store);
                            Double dLong = getLongDoubleWithStore(store);

                            LatLng latLng = maker.getPosition();
                            Double roundLatMaker = MathForDummies.round(
                                    latLng.latitude, 5);
                            Double roundLongMaker = MathForDummies.round(
                                    latLng.longitude, 5);

                            Double roundLat = MathForDummies.round(dLat, 5);
                            Double roundLong = MathForDummies.round(dLong, 5);

                            if (roundLatMaker == roundLat
                                    && roundLongMaker == roundLong) {
                                HashMap<String, Object> hmData = new HashMap<String, Object>();
                                hmData.put(Constants.KeyData.STORE_OBJECT, store);
                                StoreDetailFragment detail = StoreDetailFragment.newInstance(new SimiData(hmData));
                                if (DataLocal.isTablet) {
                                    SimiManager.getIntance().addPopupFragment(detail);
                                } else {
                                    SimiManager.getIntance().replaceFragment(detail);
                                }
                            }

                        }
                    }
                });
                ggmap.setInfoWindowAdapter(new InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        return gotInforContent(marker);
                    }
                });
            }
        });
    }

    protected View gotInforContent(Marker marker) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        int idView = Rconfig.getInstance().layout(
                "plugins_store_locator_info_window_layout");
        View v = inflater.inflate(idView, null);
        v.setMinimumWidth(200);
        v.setBackgroundColor(getResources().getColor(android.R.color.white));
        TextView tvName = (TextView) v.findViewById(Rconfig.getInstance()
                .getIdLayout("tv_name"));
        TextView tvAddress = (TextView) v.findViewById(Rconfig.getInstance()
                .getIdLayout("tv_address"));
        CircleImageView img = (CircleImageView) v.findViewById(Rconfig
                .getInstance().getIdLayout("img_store"));

        LatLng latLngMarker = marker.getPosition();
        Double latMarker = latLngMarker.latitude;
        Double longMarker = latLngMarker.longitude;
        Location currentLocation = new Location("");
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        Location location = gpsTracker.getLocation();
        if (location != null) {
            currentLocation.setLongitude(location.getLongitude());
            currentLocation.setLatitude(location.getLatitude());
        }
        Double latCurrent = currentLocation.getLatitude();
        Double longCurrent = currentLocation.getLongitude();

        Double roundLatMarker = MathForDummies.round(latMarker, 5);
        Double roundLongMarker = MathForDummies.round(longMarker, 5);
        Double roundLatCurrent = MathForDummies.round(latCurrent, 5);
        Double roundLongCurrent = MathForDummies.round(longCurrent, 5);

        if (roundLatMarker == roundLatCurrent
                && roundLongMarker == roundLongCurrent) {
            tvName.setText(SimiTranslator.getInstance().translate("You are here"));
            tvAddress.setText("");
        } else if (roundLatMarker != roundLatCurrent
                && roundLongMarker != roundLongCurrent) {
            for (int i = 0; i < store_maker.size(); i++) {

                StoreObject store = store_maker.get(i);

                Double latStore = getLatDoubleWithStore(store);
                Double longStore = getLongDoubleWithStore(store);

                Double roundLatStore = MathForDummies.round(latStore, 5);
                Double roundLongStore = MathForDummies.round(longStore, 5);

                if (roundLatStore == roundLatMarker
                        && roundLongStore == roundLongMarker) {
                    tvName.setText(store_maker.get(i).getName());
                    tvAddress.setText(StoreLocatorConfig.convertAddress(store));

                    String urlImage = store.getImage_icon();

                    if (Utils.validateString(urlImage)) {
                        SimiDrawImage drawImage = new SimiDrawImage();
                        drawImage.drawImage(img, urlImage);
                    }
                } else {
                    img.setImageDrawable(getResources().getDrawable(
                            Rconfig.getInstance().getIdDraw(
                                    "plugins_locator_ic_store_android")));
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

    private JSONObject putData(String lat, String lng, String offset) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lat", lat);
            jsonObject.put("lng", lng);
            jsonObject.put("offset", offset);
            jsonObject.put("limit", "10");
        } catch (Exception e) {
        }
        return jsonObject;
    }

    private void addMaker() {

        for (int i = 0; i < store_maker.size(); i++) {

            StoreObject store = store_maker.get(i);
            String latitude = store.getLatitude();
            String longtitude = store.getLongtitude();

            if (Utils.validateString(latitude)
                    && Utils.validateString(longtitude)) {

                latitude = latitude.trim();
                longtitude = longtitude.trim();

                Double dLat = convertToDouble(latitude);
                Double dLong = convertToDouble(longtitude);

                LatLng end = new LatLng(dLat, dLong);
                MarkerOptions options = new MarkerOptions().position(end).icon(
                        BitmapDescriptorFactory.fromResource(Rconfig
                                .getInstance().drawable(
                                        "plugins_locator_maker_default")));
                ggmap.addMarker(options);
            }

        }

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
            Log.e("MapViewFragment ",
                    "=======================> convertToDouble "
                            + e.getMessage());
        }

        return target;
    }

//	public class TaskLoadMaker extends AsyncTask<Void, Void, JSONObject> {
//		JSONObject data;
//
//		@Override
//		protected JSONObject doInBackground(Void... params) {
//			return StoreLocatorFragment.getJon(data,
//					StoreLocatorFragment.url_list_store);
//
//		}
//
//		@Override
//		protected void onPostExecute(JSONObject result) {
//			StoreParser parser = new StoreParser();
//			List<StoreObject> stores = parser.getResult(result);
//
//			if (null != stores && stores.size() > 0) {
//				for (int i = 0; i < stores.size(); i++) {
//					StoreObject store = stores.get(i);
//
//					if (StoreLocatorFragment.check(store, store_maker) == 0) {
//
//						String langtitude = store.getLatitude();
//						String longtitude = store.getLongtitude();
//						if (Utils.validateString(langtitude)
//								&& Utils.validateString(longtitude)) {
//							langtitude = langtitude.trim();
//							longtitude = longtitude.trim();
//							Double dLang = convertToDouble(langtitude);
//							Double dLong = convertToDouble(longtitude);
//							if (dLang != 0 && dLong != 0) {
//								store_maker.add(store);
//							}
//
//						}
//					}
//
//				}
//				addMaker();
//			}
//			super.onPostExecute(result);
//		}
//
//	}

}
