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
 * Created by Paresh on 1/20/2015.
 */
public class DBManagerMessage {
    private SQLiteDatabase database;
    private DbInit dbHelper;

    public DBManagerMessage(Context mContext) {
        dbHelper = new DbInit(mContext);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public int getCountOfRecords() {
        Cursor mCount = database.rawQuery("select count(*) from" + SQLQuery.TABLE_MESSAGELIST, null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        return count;
    }

    public boolean tableExists() {
        open();

        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + SQLQuery.TABLE_MESSAGELIST + "'", null);
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


    public MessagePOJO addMessagetoLocal(String mContactNumber, String mMsg, String mtime, String mReadStatus, String mContactName) {
        MessagePOJO pj = null;

        open();
        ContentValues values = new ContentValues();
        values.put("contact", mContactNumber);
        values.put("contactName", mContactName);
        values.put("message", mMsg);
        values.put("time", mtime);
        values.put("readstatus", mReadStatus);

        long insertId = database.insert(SQLQuery.TABLE_MESSAGELIST, null,
                values);

        Cursor cursor = database.query(SQLQuery.TABLE_MESSAGELIST, SQLQuery.MESSAGELIST_ALL_COLUMNS, SQLQuery.COLUMN_INPUT_SR_ID + " = " + insertId,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        pj = cursorToInserted(cursor);


        close();

        return pj;
    }

    private MessagePOJO cursorToInserted(Cursor cursor) {
        MessagePOJO pj = new MessagePOJO();

        pj.setContactNumber(cursor.getString(1));
        pj.setContactName(cursor.getString(2));
        pj.setMessage(cursor.getString(3));
        pj.setTime(cursor.getString(4));
        pj.setReadStatus(cursor.getString(5));
        return pj;

    }

    public ArrayList<MessagePOJO> getAllMessages() {
        open();
        ArrayList<MessagePOJO> mlist = new ArrayList<MessagePOJO>();

        Cursor cursor = database.query(SQLQuery.TABLE_MESSAGELIST, SQLQuery.MESSAGELIST_ALL_COLUMNS,
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
                MessagePOJO temppj = cursorToInserted(cursor);
                mlist.add(temppj);
                cursor.moveToNext();
            }

            cursor.close();
            close();
            return mlist;
        }

    }

    public ArrayList<MessagePOJO> getAllMessagesFromAndTo(int to) {
        open();
        ArrayList<MessagePOJO> mlist = new ArrayList<MessagePOJO>();
        SQLiteDatabase dbs = dbHelper.getReadableDatabase();
        Cursor cursor = dbs.rawQuery("Select * from  " + SQLQuery.TABLE_MESSAGELIST + " order by id desc Limit " + String.valueOf(to) + " ;", null);
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        } else {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                MessagePOJO temppj = cursorToInserted(cursor);
                mlist.add(temppj);
                cursor.moveToNext();
            }

            cursor.close();
            close();
            return mlist;
        }

    }


}
