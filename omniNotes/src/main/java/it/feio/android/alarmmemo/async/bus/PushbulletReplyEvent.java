package it.feio.android.alarmmemo.async.bus;

import android.util.Log;
import it.feio.android.alarmmemo.utils.Constants;


public class PushbulletReplyEvent {

	public String message;

	public PushbulletReplyEvent(String message) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.message = message;
	}
}
