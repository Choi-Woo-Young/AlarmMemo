package it.feio.android.alarmmemo.helpers;

import android.content.Context;

import io.nlopez.smartlocation.location.LocationProvider;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesWithFallbackProvider;

public class GeoCodeProviderFactory {

    public static LocationProvider getProvider(Context context) {
        return new LocationGooglePlayServicesWithFallbackProvider(context);
    }
}