package it.feio.android.alarmmemo.async.bus;

import android.util.Log;
import it.feio.android.alarmmemo.utils.Constants;


public class NotesMergeEvent {

	public final boolean keepMergedNotes;


	public NotesMergeEvent(boolean keepMergedNotes) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.keepMergedNotes = keepMergedNotes;
	}
}
