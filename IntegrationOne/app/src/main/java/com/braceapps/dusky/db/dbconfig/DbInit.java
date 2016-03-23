package com.braceapps.dusky.db.dbconfig;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Paresh on 1/20/2015.
 */
public class DbInit extends SQLiteOpenHelper {

    public DbInit(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DbInit(Context context) {
        super(context, SQLQuery.DB_NAME, null, SQLQuery.DB_VERSION);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLQuery.CREATE_TABLE_CONTACTLIST);
        db.execSQL(SQLQuery.CREATE_TABLE_MESSAGELIST);
        db.execSQL(SQLQuery.CREATE_TABLE_CALL_LOG);
        db.execSQL(SQLQuery.CREATE_TABLE_REQUESTLOGGER);
        db.execSQL(SQLQuery.CREATE_TABLE_SLAVElOGGER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLQuery.DELETE_ALL_ENTRIES_TABLE_CONTACTLIST);
        db.execSQL(SQLQuery.DELETE_ALL_ENTRIES_TABLE_MESSAGELIST);
        db.execSQL(SQLQuery.DELETE_ALL_ENTRIES_TABLE_CALL_LOG);
        db.execSQL(SQLQuery.DELETE_ALL_ENTRIES_TABLE_REQUESTLOGGER);
        db.execSQL(SQLQuery.DELETE_ALL_ENTRIES_TABLE_SLAVELOGGER);

        onCreate(db);

    }
}
