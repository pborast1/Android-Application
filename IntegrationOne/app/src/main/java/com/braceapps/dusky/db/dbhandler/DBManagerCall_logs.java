package com.braceapps.dusky.db.dbhandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.braceapps.dusky.db.dbconfig.DbInit;
import com.braceapps.dusky.db.dbconfig.SQLQuery;

import java.util.ArrayList;


/**
 * Created by Paresh on 2/11/2015.
 */
public class DBManagerCall_logs {
    private SQLiteDatabase database;
    private DbInit dbHelper;

    public DBManagerCall_logs(Context mContext) {
        dbHelper = new DbInit(mContext);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public int getCountOfRecords() {
        Cursor mCount = database.rawQuery("select count(*) from" + SQLQuery.TABLE_CALL_LOG, null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        return count;
    }

    public boolean tableExists() {
        open();

        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + SQLQuery.TABLE_CALL_LOG + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                close();
                return true;
            }
            cursor.close();
        }
        close();
        return false;


    }


    public CallLogPOJO addCalltoLocal(String mContactNumber, String mtime, String mType, String mContactName) {
        CallLogPOJO pj = null;

        open();
        ContentValues values = new ContentValues();
        values.put("contact", mContactNumber);
        values.put("contactName", mContactName);

        values.put("time", mtime);
        values.put("type", mType);

        long insertId = database.insert(SQLQuery.TABLE_CALL_LOG, null,
                values);

        Cursor cursor = database.query(SQLQuery.TABLE_CALL_LOG, SQLQuery.CALL_LOG_ALL_COLUMNS, SQLQuery.COLUMN_INPUT_SR_ID + " = " + insertId,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        pj = cursorToInserted(cursor);


        close();

        return pj;
    }

    private CallLogPOJO cursorToInserted(Cursor cursor) {
        CallLogPOJO pj = new CallLogPOJO();

        pj.setContactName(cursor.getString(2));
        pj.setContactNumber(cursor.getString(1));
        pj.setTime(cursor.getString(4));
        pj.setType(cursor.getString(3));
        return pj;

    }

    public ArrayList<CallLogPOJO> getFullCallLog() {
        open();
        ArrayList<CallLogPOJO> mlist = new ArrayList<CallLogPOJO>();

        Cursor cursor = database.query(SQLQuery.TABLE_CALL_LOG, SQLQuery.CALL_LOG_ALL_COLUMNS,
                null,
                null,
                null,
                null,
                null);
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        } else {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                CallLogPOJO temppj = cursorToInserted(cursor);
                mlist.add(temppj);
                cursor.moveToNext();
            }

            cursor.close();
            close();
            return mlist;
        }

    }

    public ArrayList<CallLogPOJO> getCallLogFromAndTo(int to) {
        open();
        ArrayList<CallLogPOJO> mlist = new ArrayList<CallLogPOJO>();
        SQLiteDatabase dbs = dbHelper.getReadableDatabase();
        Cursor cursor = dbs.rawQuery("Select * from  " + SQLQuery.TABLE_CALL_LOG + " order by id desc Limit "+ String.valueOf(to) +" ;" , null);
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        } else {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                CallLogPOJO temppj = cursorToInserted(cursor);
                mlist.add(temppj);
                cursor.moveToNext();
            }

            cursor.close();
            close();
            return mlist;
        }
    }

}