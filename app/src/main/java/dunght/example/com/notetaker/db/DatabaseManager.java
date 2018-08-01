package dunght.example.com.notetaker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "notetaker.db";
    public static final String TABLE_NOTE_NAME = "tbnote";
    public static final String TABLE_PHOTO_NAME = "tbphoto";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_DATETIME_CREATE = "datetime_create";
    public static final String COLUMN_DATE_ALARM = "date_alarm";
    public static final String COLUMN_TIME_ALARM = "time_alarm";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_URI = "uri";
    public static final int DATABASE_VERSION = 1;



    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tbNote = "CREATE TABLE " + TABLE_NOTE_NAME + "( "
                                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                        + COLUMN_TITLE + " TEXT, "
                                        + COLUMN_CONTENT + " TEXT, "
                                        + COLUMN_DATETIME_CREATE + " TEXT, "
                                        + COLUMN_DATE_ALARM + " TEXT, "
                                        + COLUMN_TIME_ALARM + " TEXT, "
                                        + COLUMN_COLOR + " INTEGER )";

        String tbPhoto = "CREATE TABLE " + TABLE_PHOTO_NAME + "( "
                                         + COLUMN_ID + " INTEGER, "
                                         + COLUMN_URI + " TEXT )";

        sqLiteDatabase.execSQL(tbNote);
        sqLiteDatabase.execSQL(tbPhoto);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NOTE_NAME;
        String sql1 = "DROP TABLE IF EXISTS " + TABLE_PHOTO_NAME;
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql1);
        onCreate(sqLiteDatabase);
    }
}
