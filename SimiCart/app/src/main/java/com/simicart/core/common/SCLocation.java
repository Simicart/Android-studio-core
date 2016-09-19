package com.simicart.core.common;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.simicart.core.config.DataLocal;

/**
 * Created by frank on 9/19/16.
 */
public class SCLocation {

    public static int LOCATION_REQUEST = 999;

    private GoogleApiClient mGoogleClient;
    private SCLocationCallBack mCallBack;
    private LocationRequest locationRequest;
    private Activity mContext;

    public SCLocation(Activity activity) {
        mContext = activity;
        createGoogleClient();
    }

    protected void createGoogleClient() {
        mGoogleClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("SCLocation", "------> NO PERMISSION");
                    if (null != mCallBack) {
                        mCallBack.getLocationComplete(false);
                    }
                    return;
                }
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleClient);
                if (null != lastLocation) {
                    String latitude = String.valueOf(lastLocation.getLatitude());
                    String longtitude = String.valueOf(lastLocation.getLongitude());

                    Log.e("SCLocation ", " +++++++++++++ LATITUDE " + latitude + " : LONGTITUDE " + longtitude);
                    DataLocal.latitude = latitude;
                    DataLocal.longtitude = longtitude;
                    if (null != mCallBack) {
                        mCallBack.getLocationComplete(true);
                    }
                } else {
                    Log.e("SCLocation ", " +++++++++++++ NULL");
                    if (null != mCallBack) {
                        mCallBack.getLocationComplete(false);
                    }
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.e("SCLocation ", " +++++++++++++ onConnectionSuspended");
                mGoogleClient.connect();

            }
        }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.e("SCLocation ", " +++++++++++++ onConnectionFailed");
                if (null != mCallBack) {
                    mCallBack.getLocationComplete(false);
                }

            }
        }).addApi(LocationServices.API).build();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        //  builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.e("SCLocation ", " +++++++++++++ SUCCUSS");
                        if (null != mCallBack) {
                            mCallBack.getLocationComplete(false);
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e("SCLocation ", " +++++++++++++ RESOLUTION_REQUIRED");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(mContext, LOCATION_REQUEST);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("SCLocation ", " +++++++++++++ RESOLUTION_REQUIRED" + e.getMessage());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("SCLocation ", " +++++++++++++ SETTINGS_CHANGE_UNAVAILABLE");
                        if (null != mCallBack) {
                            mCallBack.getLocationComplete(false);
                        }
                        break;
                }
            }
        });
    }

    public void updateLocation() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (null != mCallBack) {
                mCallBack.getLocationComplete(false);
            }
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, locationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (null != location) {
                    String latitude = String.valueOf(location.getLatitude());
                    String longtitude = String.valueOf(location.getLongitude());

                    Log.e("SCLocation ", " +++++++++++++ LATITUDE " + latitude + " : LONGTITUDE " + longtitude);
                    DataLocal.latitude = latitude;
                    DataLocal.longtitude = longtitude;
                    if (null != mCallBack) {
                        mCallBack.getLocationComplete(true);
                    }
                } else {
                    if (null != mCallBack) {
                        mCallBack.getLocationComplete(false);
                    }
                }
            }
        }).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {

            }
        });
    }

    public void connect() {
        mGoogleClient.connect();
    }

    public void disconnect() {
        Log.e("SCLocation ", " +++++++++++++  disconnect");
        mGoogleClient.disconnect();
    }

    public void setCallBack(SCLocationCallBack callBack) {
        mCallBack = callBack;
    }
}
