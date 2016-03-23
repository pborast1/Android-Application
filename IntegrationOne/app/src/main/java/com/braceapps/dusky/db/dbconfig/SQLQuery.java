package com.braceapps.dusky.db.dbconfig;

/**
 * Created by Paresh on 1/20/2015.
 */
public interface SQLQuery {


    static final String TABLE_CONTACTLIST = "SUMMARY_TABLE";
    static final String TABLE_MESSAGELIST = "MESSAGE_TABLE";
    static final String TABLE_CALL_LOG = "CALLLOG_TABLE";
    static final String TABLE_REQUESTLOGGER = "REQUEST_LOGGER";
    static final String TABLE_SLAVELOGGER = "SLAVE_LOGGER";

    public static final String COLUMN_INPUT_SR_ID = "id";


    static final String DB_NAME = "LATTITUDE.db";
    static final int DB_VERSION = 1;

    static final String CREATE_TABLE_MESSAGELIST = "CREATE  TABLE "
            + TABLE_MESSAGELIST + "(" + COLUMN_INPUT_SR_ID
            + " integer primary key autoincrement, " + "contact"
            + "  , "
            + "contactname"
            + "  , "
            + "message "
            + ", " +
            "time , " +
            "readstatus" +
            ")";
    static final String CREATE_TABLE_CONTACTLIST = "CREATE  TABLE "
            + TABLE_CONTACTLIST + "(" + "contact"
            + "  primary key , " + "lastmessage "
            + ", " +
            "time" +
            ")";

    static final String CREATE_TABLE_CALL_LOG = "CREATE  TABLE "
            + TABLE_CALL_LOG + "(" + COLUMN_INPUT_SR_ID
            + " integer primary key autoincrement, " + "contact"
            + "  , "
            + "contactname"
            + "  , "
            + "type"
            + " , "
            + "time"
            + ")";

    static final String CREATE_TABLE_REQUESTLOGGER = "CREATE  TABLE "
            + TABLE_REQUESTLOGGER + "(" + COLUMN_INPUT_SR_ID
            + " integer primary key autoincrement, " + "contact"
            + "  , "
            + "contactname"
            + "  , "
            + "message "
            + ", "
            + "time"
            + " , "
            + "readstatus"
            + " , "
            + "type"
            + " , "
            + "title" +
            ")";
    static final String CREATE_TABLE_SLAVElOGGER = "CREATE  TABLE "
            + TABLE_SLAVELOGGER + "(" + COLUMN_INPUT_SR_ID
            + " integer primary key autoincrement, " + "contact"
            + "  , "
            + "contactname"
            + "  , "
            + "message "
            + ", "
            + "time"
            + " , "
            + "readstatus"
            + " , "
            + "type"
            + " , "
            + "title" +
            ")";


    static final String[] MESSAGELIST_ALL_COLUMNS = {COLUMN_INPUT_SR_ID, "contact", "contactname", "message", "time", "readstatus"};
    static final String[] CONTACTLIST_ALL_COLUMNS = {"contact", "contactname", "message", "time"};
    static final String[] CALL_LOG_ALL_COLUMNS = {COLUMN_INPUT_SR_ID, "contact", "contactname", "type", "time"};
    static final String[] REQUESTLOGGER_ALL_COLUMNS = {COLUMN_INPUT_SR_ID, "contact", "contactname", "message", "time", "readstatus", "type", "title"};
    static final String[] SLAVELOGGER_ALL_COLUMNS = {COLUMN_INPUT_SR_ID, "contact", "contactname", "message", "time", "readstatus", "type", "title"};


    static final String DELETE_ALL_ENTRIES_TABLE_CONTACTLIST = "DROP TABLE IF EXISTS " + TABLE_CONTACTLIST;
    static final String DELETE_ALL_ENTRIES_TABLE_MESSAGELIST = "DROP TABLE IF EXISTS " + TABLE_MESSAGELIST;
    static final String DELETE_ALL_ENTRIES_TABLE_CALL_LOG = "DROP TABLE IF EXISTS " + TABLE_CALL_LOG;
    static final String DELETE_ALL_ENTRIES_TABLE_REQUESTLOGGER = "DROP TABLE IF EXISTS " + TABLE_REQUESTLOGGER;
    static final String DELETE_ALL_ENTRIES_TABLE_SLAVELOGGER = "DROP TABLE IF EXISTS " + TABLE_SLAVELOGGER;


}
