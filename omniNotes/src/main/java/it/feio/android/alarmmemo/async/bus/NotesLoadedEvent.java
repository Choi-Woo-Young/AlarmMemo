package it.feio.android.alarmmemo.async.bus;

import android.util.Log;

import it.feio.android.alarmmemo.models.Note;
import it.feio.android.alarmmemo.utils.Constants;

import java.util.ArrayList;


public class NotesLoadedEvent {

	public ArrayList<Note> notes;


	public NotesLoadedEvent(ArrayList<Note> notes) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.notes = notes;
	}
}
