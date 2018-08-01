package dunght.example.com.notetaker.db.db.table;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import dunght.example.com.notetaker.db.DatabaseManager;
import dunght.example.com.notetaker.model.Note;

public class NoteData {

    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;


    public NoteData(Context context) {
        this.context = context;
        sqLiteOpenHelper = new DatabaseManager(context);
    }

    public void add(Note note) {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseManager.COLUMN_TITLE, note.getTitle());
        contentValues.put(DatabaseManager.COLUMN_CONTENT, note.getContent());
        contentValues.put(DatabaseManager.COLUMN_DATETIME_CREATE, note.getDatetimeCreate());
        contentValues.put(DatabaseManager.COLUMN_DATE_ALARM, note.getDateAlarm());
        contentValues.put(DatabaseManager.COLUMN_TIME_ALARM, note.getTimeAlarm());
        contentValues.put(DatabaseManager.COLUMN_COLOR, note.getColor());

        sqLiteDatabase.insert(DatabaseManager.TABLE_NOTE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public void delete(Note note) {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        sqLiteDatabase.delete(DatabaseManager.TABLE_NOTE_NAME, DatabaseManager.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        sqLiteDatabase.close();
    }

    public void update(Note note) {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseManager.COLUMN_TITLE, note.getTitle());
        contentValues.put(DatabaseManager.COLUMN_CONTENT, note.getContent());
        contentValues.put(DatabaseManager.COLUMN_DATETIME_CREATE, note.getDatetimeCreate());
        contentValues.put(DatabaseManager.COLUMN_DATE_ALARM, note.getDateAlarm());
        contentValues.put(DatabaseManager.COLUMN_TIME_ALARM, note.getTimeAlarm());
        contentValues.put(DatabaseManager.COLUMN_COLOR, note.getColor());
        sqLiteDatabase.update(DatabaseManager.TABLE_NOTE_NAME, contentValues, DatabaseManager.COLUMN_ID + " = ?", new String[]{String.valueOf(note.getId())});
        sqLiteDatabase.close();
    }

    public ArrayList<Note> getAllNote() {
        ArrayList<Note> arrNote = new ArrayList<>();
        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseManager.TABLE_NOTE_NAME, null);

        if(cursor == null) {
            return  null;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = new Note();
            note.setId(cursor.getInt(0));
            note.setTitle(cursor.getString(1));
            note.setContent(cursor.getString(2));
            note.setDatetimeCreate(cursor.getString(3));
            note.setDateAlarm(cursor.getString(4));
            note.setTimeAlarm(cursor.getString(5));
            note.setColor(cursor.getInt(6));
            cursor.moveToNext();
            arrNote.add(note);
        }

        cursor.close();
        sqLiteDatabase.close();

        return arrNote;
    }

    public int getLastId() {
        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseManager.TABLE_NOTE_NAME, null);
        cursor.moveToLast();
        return cursor.getInt(0);
    }
}
