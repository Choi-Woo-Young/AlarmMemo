package it.feio.android.alarmmemo.async.bus;

import android.util.Log;
import it.feio.android.alarmmemo.utils.Constants;


public class NavigationUpdatedNavDrawerClosedEvent {

	public final Object navigationItem;


	public NavigationUpdatedNavDrawerClosedEvent(Object navigationItem) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.navigationItem = navigationItem;
	}
}
