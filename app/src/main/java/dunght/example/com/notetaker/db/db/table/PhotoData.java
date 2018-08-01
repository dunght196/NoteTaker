package dunght.example.com.notetaker.db.db.table;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import dunght.example.com.notetaker.db.DatabaseManager;


public class PhotoData {

    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private static PhotoData instance = null;

    public PhotoData(Context context) {
        sqLiteOpenHelper = new DatabaseManager(context);
    }

    public static PhotoData Instance(Context context) {
        if(instance == null) {
            instance = new PhotoData(context);
        }
        return  instance;
    }

    public void add(String photo,int id ) {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseManager.COLUMN_ID, id);
        contentValues.put(DatabaseManager.COLUMN_URI, photo);
        sqLiteDatabase.insert(DatabaseManager.TABLE_PHOTO_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public void delete(int id) {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        sqLiteDatabase.delete(DatabaseManager.TABLE_PHOTO_NAME, DatabaseManager.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }

    public ArrayList<String> getAllPhotoByIDNote(int id) {
        ArrayList<String> arrPhoto = new ArrayList<>();
        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseManager.TABLE_PHOTO_NAME + " WHERE " + DatabaseManager.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            do {
                arrPhoto.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();

        return arrPhoto;
    }
}
