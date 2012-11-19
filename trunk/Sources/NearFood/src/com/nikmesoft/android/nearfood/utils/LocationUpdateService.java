package com.nikmesoft.android.nearfood.utils;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationUpdateService extends Service {

	private final IBinder mBinder = new LocationUpdateServiceBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		// Acquire a reference to the system Location Manager
				LocationManager locationManager = (LocationManager) this
						.getSystemService(Context.LOCATION_SERVICE);

				// Define a listener that responds to location updates
				LocationListener locationListener = new LocationListener() {
					public void onLocationChanged(Location location) {
						final Location loc = location;
						if (location != null) {
							CommonUtil.toastNotify(getApplicationContext(), "Updating new location");
							Thread t = new Thread() {
								public void run() {
									double lat = (double) (loc.getLatitude() * 1E6);
									double lng = (double) (loc.getLongitude() * 1E6);

									Log.d("Longitude", "" + lng);
									Log.d("Latitude", "" + lat);
								}
							};
							t.start();

						}
					}

					public void onStatusChanged(String provider, int status,
							Bundle extras) {
					}

					public void onProviderEnabled(String provider) {
					}

					public void onProviderDisabled(String provider) {
					}
				};

				// Register the listener with the Location Manager to receive location
				// updates
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}

	public class LocationUpdateServiceBinder extends Binder {
		LocationUpdateService getService() {
			return LocationUpdateService.this;
		}
	}
	
	private void abc(){
		// Acquire a reference to the system Location Manager
				LocationManager locationManager = (LocationManager) this
						.getSystemService(Context.LOCATION_SERVICE);

				// Define a listener that responds to location updates
				LocationListener locationListener = new LocationListener() {
					public void onLocationChanged(Location location) {
						final Location loc = location;
						if (location != null) {
							CommonUtil.toastNotify(getApplicationContext(), "Updating new location");
							Thread t = new Thread() {
								public void run() {
									double lat = (double) (loc.getLatitude() * 1E6);
									double lng = (double) (loc.getLongitude() * 1E6);

									Log.d("Longitude", "" + lng);
									Log.d("Latitude", "" + lat);
								}
							};
							t.start();

						}
					}

					public void onStatusChanged(String provider, int status,
							Bundle extras) {
					}

					public void onProviderEnabled(String provider) {
					}

					public void onProviderDisabled(String provider) {
					}
				};

				// Register the listener with the Location Manager to receive location
				// updates
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
}
