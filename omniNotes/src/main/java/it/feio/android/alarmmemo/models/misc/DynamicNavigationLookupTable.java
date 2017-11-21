/*
 * Copyright (C) 2015 Federico Iosue (federico.iosue@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.feio.android.alarmmemo.models.misc;


import android.util.Log;
import de.greenrobot.event.EventBus;
import it.feio.android.alarmmemo.async.bus.DynamicNavigationReadyEvent;
import it.feio.android.alarmmemo.async.bus.NotesUpdatedEvent;
import it.feio.android.alarmmemo.db.DbHelper;
import it.feio.android.alarmmemo.models.Note;
import it.feio.android.alarmmemo.utils.Constants;

import java.util.List;


public class DynamicNavigationLookupTable {

	private static DynamicNavigationLookupTable instance;
	int archived;
	int trashed;
	int uncategorized;
	int reminders;


	private DynamicNavigationLookupTable() {
		EventBus.getDefault().register(this);
		update();
	}


	public static DynamicNavigationLookupTable getInstance() {
		if (instance == null) {
			instance = new DynamicNavigationLookupTable();
		}
		return instance;
	}


	public void update() {
		((Runnable) () -> {
			archived = trashed = uncategorized = reminders = 0;
			List<Note> notes = DbHelper.getInstance().getAllNotes(false);
			for (int i = 0; i < notes.size(); i++) {
				if (notes.get(i).isTrashed()) trashed++;
				else if (notes.get(i).isArchived()) archived++;
				else if (notes.get(i).getAlarm() != null) reminders++;
				if (notes.get(i).getCategory() == null || notes.get(i).getCategory().getId().equals(0L)) {
					uncategorized++;
				}
			}
			EventBus.getDefault().post(new DynamicNavigationReadyEvent());
			Log.d(Constants.TAG, "Dynamic menu finished counting items");
		}).run();
	}


	public void onEventAsync(NotesUpdatedEvent event) {
		update();
	}


	public int getArchived() {
		return archived;
	}


	public int getTrashed() {
		return trashed;
	}


	public int getReminders() {
		return reminders;
	}


	public int getUncategorized() {
		return uncategorized;
	}

}