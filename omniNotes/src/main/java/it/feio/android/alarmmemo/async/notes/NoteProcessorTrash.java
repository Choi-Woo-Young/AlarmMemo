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

package it.feio.android.alarmmemo.async.notes;

import java.util.List;

import it.feio.android.alarmmemo.OmniNotes;
import it.feio.android.alarmmemo.db.DbHelper;
import it.feio.android.alarmmemo.models.Note;
import it.feio.android.alarmmemo.utils.ReminderHelper;
import it.feio.android.alarmmemo.utils.ShortcutHelper;


public class NoteProcessorTrash extends NoteProcessor {

    boolean trash;


    public NoteProcessorTrash(List<Note> notes, boolean trash) {
        super(notes);
        this.trash = trash;
    }


    @Override
    protected void processNote(Note note) {
        if (trash) {
            ShortcutHelper.removeshortCut(OmniNotes.getAppContext(), note);
            ReminderHelper.removeReminder(OmniNotes.getAppContext(), note);
        } else {
            ReminderHelper.addReminder(OmniNotes.getAppContext(), note);
        }
        DbHelper.getInstance().trashNote(note, trash);
    }
}
