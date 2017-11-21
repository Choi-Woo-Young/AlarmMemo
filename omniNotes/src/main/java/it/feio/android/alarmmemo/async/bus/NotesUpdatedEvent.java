package it.feio.android.alarmmemo.async.bus;

import android.util.Log;
import it.feio.android.alarmmemo.utils.Constants;


/**
 * Created by fede on 18/04/15.
 */
public class NotesUpdatedEvent {

	public NotesUpdatedEvent() {
		Log.d(Constants.TAG, this.getClass().getName());
	}
}
