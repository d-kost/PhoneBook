package com.phonebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    final static String DB_NAME = "phonebook.db";
    final static String TABLE = "phone";
    final static String CREATE = "CREATE TABLE " + TABLE + " ( `_id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`name` TEXT NOT NULL, `number` TEXT NOT NULL, `viber` TEXT, `telegram` TEXT, `whatsapp` TEXT )";
    private final static int DB_VERSION = 1;


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
        db.execSQL("INSERT INTO phone (name, number, viber, telegram, whatsapp) " +
                "VALUES ('Anna', '79501112233', 'Viber', '', 'WhatsApp')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP DATABASE " + DB_NAME);
        this.onCreate(db);


    }
}
