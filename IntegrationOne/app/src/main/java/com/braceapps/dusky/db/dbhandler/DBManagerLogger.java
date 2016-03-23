package com.braceapps.dusky.db.dbhandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.braceapps.dusky.db.dbconfig.DbInit;
import com.braceapps.dusky.db.dbconfig.SQLQuery;

import java.util.ArrayList;

/**
 * Created by Paresh on 3/12/2015.
 */
public class DBManagerLogger {
    private SQLiteDatabase database;
    private DbInit dbHelper;

    public DBManagerLogger(Context mContext) {
        dbHelper = new DbInit(mContext);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public int getCountOfRecords() {
        open();
        Cursor mCount = database.rawQuery("select count(*) from" + SQLQuery.TABLE_REQUESTLOGGER, null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        close();
        return count;
    }

    public RequestLoggerPOJO addRequesttoLocal(String mContactNumber, String mMsg, String mtime, String mReadStatus, String mContactName, String mType, String mTitle) {
        RequestLoggerPOJO pj = null;

        open();
        ContentValues values = new ContentValues();
        values.put("contact", mContactNumber);
        values.put("contactName", mContactName);
        values.put("message", mMsg);
        values.put("time", mtime);
        values.put("readstatus", mReadStatus);
        values.put("type", mType);
        values.put("title", mTitle);

        long insertId = database.insert(SQLQuery.TABLE_REQUESTLOGGER, null,
                values);

        Cursor cursor = database.query(SQLQuery.TABLE_REQUESTLOGGER, SQLQuery.REQUESTLOGGER_ALL_COLUMNS, SQLQuery.COLUMN_INPUT_SR_ID + " = " + insertId,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        pj = cursorToInserted(cursor);


        close();

        return pj;
    }

    public RequestLoggerPOJO addToSlaveLog(String mContactNumber, String mMsg, String mtime, String mReadStatus, String mContactName, String mType, String mTitle) {
        RequestLoggerPOJO pj = null;

        open();
        ContentValues values = new ContentValues();
        values.put("contact", mContactNumber);
        values.put("contactName", mContactName);
        values.put("message", mMsg);
        values.put("time", mtime);
        values.put("readstatus", mReadStatus);
        values.put("type", mType);
        values.put("title", mTitle);

        long insertId = database.insert(SQLQuery.TABLE_SLAVELOGGER, null,
                values);

        Cursor cursor = database.query(SQLQuery.TABLE_SLAVELOGGER, SQLQuery.SLAVELOGGER_ALL_COLUMNS, SQLQuery.COLUMN_INPUT_SR_ID + " = " + insertId,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        pj = cursorToInserted(cursor);


        close();

        return pj;
    }
    public ArrayList<RequestLoggerPOJO> getCompleteSlaveLog() {
        open();
        int numRows = (int) DatabaseUtils.queryNumEntries(database, SQLQuery.TABLE_SLAVELOGGER);
        if (numRows > 0) {
            ArrayList<RequestLoggerPOJO> mlist = new ArrayList<RequestLoggerPOJO>();

            Cursor cursor = database.query(SQLQuery.TABLE_SLAVELOGGER, SQLQuery.SLAVELOGGER_ALL_COLUMNS,
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
                    RequestLoggerPOJO temppj = cursorToInserted(cursor);
                    mlist.add(temppj);
                    cursor.moveToNext();
                }

                cursor.close();
                close();
                return mlist;
            }
        } else {
            return null;
        }

    }

    public ArrayList<RequestLoggerPOJO> getCompleteSlaveLog(int to) {
        open();
        int numRows = (int) DatabaseUtils.queryNumEntries(database, SQLQuery.TABLE_SLAVELOGGER);
        if (numRows > 0) {
            ArrayList<RequestLoggerPOJO> mlist = new ArrayList<RequestLoggerPOJO>();
            SQLiteDatabase dbs = dbHelper.getReadableDatabase();
            Cursor cursor = dbs.rawQuery("Select * from  " + SQLQuery.TABLE_SLAVELOGGER + " order by id desc Limit " + String.valueOf(to) + " ;", null);
            if (cursor == null || cursor.getCount() == 0) {
                return null;
            } else {

                cursor.moveToFirst();

                while (!cursor.isAfterLast()) {
                    RequestLoggerPOJO temppj = cursorToInserted(cursor);
                    mlist.add(temppj);
                    cursor.moveToNext();
                }

                cursor.close();
                close();
                return mlist;
            }
        } else {
            return null;
        }

    }



    public void deleteSingleEntry(int id) {
        open();
        database.execSQL("DELETE FROM " + SQLQuery.TABLE_REQUESTLOGGER + " WHERE id= " + id);
        close();

    }

    public ArrayList<RequestLoggerPOJO> getAllRequests() {
        open();
        int numRows = (int) DatabaseUtils.queryNumEntries(database, SQLQuery.TABLE_REQUESTLOGGER);
        if (numRows > 0) {
            ArrayList<RequestLoggerPOJO> mlist = new ArrayList<RequestLoggerPOJO>();

            Cursor cursor = database.query(SQLQuery.TABLE_REQUESTLOGGER, SQLQuery.REQUESTLOGGER_ALL_COLUMNS,
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
                    RequestLoggerPOJO temppj = cursorToInserted(cursor);
                    mlist.add(temppj);
                    cursor.moveToNext();
                }

                cursor.close();
                close();
                return mlist;
            }
        } else {
            return null;
        }

    }

    private RequestLoggerPOJO cursorToInserted(Cursor cursor) {

        RequestLoggerPOJO pj = new RequestLoggerPOJO();
        pj.setid(cursor.getInt(0));
        pj.setContactNumber(cursor.getString(1));
        pj.setContactName(cursor.getString(2));
        pj.setMessage(cursor.getString(3));
        pj.setTime(cursor.getString(4));
        pj.setReadStatus(cursor.getString(5));
        pj.setType(cursor.getString(6));
        pj.setTitle(cursor.getString(7));
        return pj;

    }

}
