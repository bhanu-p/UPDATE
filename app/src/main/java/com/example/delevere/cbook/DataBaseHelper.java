package com.example.delevere.cbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.delevere.cbook.db.ChatModel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Delevere on 29-Jun-16.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "student_database";

    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_STUDENTS = "students";
    private static final String TABLE_STUDENTS_BACKUP = "studentsbackup";
    private static final String TABLE_EVENT = "events";
    private static final String TABLE_BEVENT = "broadcastevents";
    private static final String TABLE_FRIEND = "friends";
    private static final String TABLE_FRIEND_UNKOWN = "friendsunknown";
    private static final String TABLE_Chat = "chat";
    private static final String TABLE_PROFILE = "profile";

    public static final String EVENT_ID = "_id";
    public static final String EVENT_NAME = "event_name";
    public static final String EVENT_CONTACT = "contact_name";
    public static final String EVENT_PHONENUMBER = "phone_number";
    public static final String EVENT_DATE_TIME = "date_time";
    public static final String EVENT_TYPE = "image";
    public static final String EVENT_CREATED_BY = "created_by";



    public static final String BEVENT_ID = "_id";
    public static final String BEVENT_NAME = "event_name";
    public static final String BEVENT_CONTACT = "contact_name";
    public static final String BEVENT_PHONENUMBER = "phone_number";
    public static final String BEVENT_DATE_TIME = "date_time";

    public static final String FRIEND_ID = "_id";
    public static final String FRIEND_NAME = "frnd_name";
    public static final String FRIEND_NUMBER = "frnd_number";
    public static final String FRIEND_TIMESTAMP = "frnd_time";
    public static final String FRIEND_CHAT_VIEW = "frnd_count";


    public static final String PROFILE_ID = "_id";
    public static final String PROFILE_NAME = "name";
    public static final String PROFILE_PHONENUMBER = "phone_number";
    public static final String PROFILE_BIRTHDAY = "birthday";


    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONENUMBER = "phone_number";

    public static final String BACKUP_KEY_ID = "_id";
    public static final String BACKUP_KEY_NAME = "name_backup";
    public static final String BACKUP_KEY_PHONENUMBER = "phone_number_backup";

    public static String TAG = "tag";
    //private SQLiteDatabase db;

    private static final String CREATE_TABLE_STUDENTS = "CREATE TABLE "
            + TABLE_STUDENTS + "(" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
            + KEY_PHONENUMBER + " TEXT ,"+"UNIQUE ("+KEY_PHONENUMBER+", "+KEY_NAME+") ON CONFLICT REPLACE);";

    private static final String CREATE_TABLE_STUDENTS_BACKUP = "CREATE TABLE "
            + TABLE_STUDENTS_BACKUP + "(" + BACKUP_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + BACKUP_KEY_NAME + " TEXT,"
            + BACKUP_KEY_PHONENUMBER + " TEXT ,"+"UNIQUE ("+BACKUP_KEY_PHONENUMBER+", "+BACKUP_KEY_NAME+") ON CONFLICT REPLACE);";


    private static final String CREATE_TABLE_EVENT = "CREATE TABLE "
            + TABLE_EVENT + "(" + EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + EVENT_CONTACT + " TEXT,"
            + EVENT_PHONENUMBER + " TEXT,"
            + EVENT_NAME + " TEXT,"
            + EVENT_CREATED_BY + " TEXT,"
            + EVENT_TYPE + " BLOB,"
            + EVENT_DATE_TIME + " TEXT ,"+"UNIQUE ("+EVENT_DATE_TIME+", "+EVENT_PHONENUMBER+") ON CONFLICT IGNORE);";

    private static final String CREATE_TABLE_PROFILE = "CREATE TABLE "
            + TABLE_PROFILE + "(" + PROFILE_ID + " INTEGER,"
            + PROFILE_NAME + " TEXT,"
            + PROFILE_PHONENUMBER + " TEXT,"
            + PROFILE_BIRTHDAY + " TEXT"
            +");";


    private static final String CREATE_TABLE_BEVENT = "CREATE TABLE "
            + TABLE_BEVENT + "(" + BEVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + BEVENT_CONTACT + " TEXT,"
            + BEVENT_PHONENUMBER + " TEXT,"
            + BEVENT_NAME + " TEXT,"
            + BEVENT_DATE_TIME + " TEXT UNIQUE);";

    private static final String CREATE_TABLE_FRIENDS = "CREATE TABLE "
            + TABLE_FRIEND + "(" + FRIEND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FRIEND_NAME + " TEXT,"
            + FRIEND_NUMBER + " TEXT ,"
            + FRIEND_TIMESTAMP + " TEXT ,"
            + FRIEND_CHAT_VIEW + "INTEGER,"
            +"UNIQUE ("+FRIEND_NAME+", "+FRIEND_NUMBER+") ON CONFLICT REPLACE);";


    private static final String CREATE_TABLE_FRIENDS_UNKNOWN = "CREATE TABLE "
            + TABLE_FRIEND_UNKOWN + "(" + FRIEND_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + FRIEND_NAME + " TEXT,"
            + FRIEND_NUMBER + " TEXT ,"+"UNIQUE ("+FRIEND_NAME+", "+FRIEND_NUMBER+") ON CONFLICT IGNORE);";


    private static final String CREATE_CONTACTS_TRIGGER = "CREATE TRIGGER dulicate AFTER INSERT ON "
            + TABLE_STUDENTS + " BEGIN INSERT INTO " + TABLE_STUDENTS_BACKUP
            + "("+BACKUP_KEY_NAME+","+BACKUP_KEY_PHONENUMBER+") SELECT "+KEY_NAME+","+KEY_PHONENUMBER+" FROM " + TABLE_STUDENTS + "; END;";

    private static final String CREATE_TIMESTAMP_TRIGGER = "CREATE TRIGGER chattrigger AFTER INSERT ON chat BEGIN UPDATE " + TABLE_FRIEND
            + " SET "+FRIEND_TIMESTAMP+" = CURRENT_TIMESTAMP WHERE "+FRIEND_NUMBER+" = "+KEY_PHONENUMBER+" FROM " + TABLE_STUDENTS + "; END;";


    //String[] statements = new String[]{CREATE_TABLE_STUDENTS, CREATE_TABLE_EVENT};
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //SQLiteDatabase db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //for(String sql : statements){
        try {
            db.execSQL(CREATE_TABLE_STUDENTS);
            db.execSQL(CREATE_TABLE_STUDENTS_BACKUP);
            db.execSQL(CREATE_TABLE_EVENT);
            db.execSQL(CREATE_TABLE_BEVENT);
            db.execSQL(CREATE_TABLE_FRIENDS);
            db.execSQL(CREATE_TABLE_FRIENDS_UNKNOWN);
            db.execSQL(CREATE_CONTACTS_TRIGGER);

            db.execSQL(ChatModel.CREATE_CHATS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS_BACKUP);// drop table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT); // drop table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEVENT); // drop table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND_UNKOWN);// drop table if exists
        db.execSQL("DROP TRIGGER IF EXISTS duplicate");
        db.execSQL("DROP TABLE IF EXISTS " + ChatModel.TABLE_CHATS);
        onCreate(db);
    }
    public void truncatstudent(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENTS, null, null);
        db.close();

    }
    public void truncatevents(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENT, null, null);
        db.close();


    }
    public void truncatbevents(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BEVENT, null, null);


    }

    public void truncatefriends(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIEND,null,null);
    }
    public boolean inserData(String name,String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_PHONENUMBER, phone);
        long result = db.insert(TABLE_STUDENTS,null,contentValues);
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }
    public boolean inserEvent(String contactname,String phonenumber,String eventname,String datetime,String image, String created){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT_CONTACT,contactname);
        contentValues.put(EVENT_PHONENUMBER,phonenumber);
        contentValues.put(EVENT_NAME,eventname);
        contentValues.put(EVENT_TYPE, image);
        contentValues.put(EVENT_DATE_TIME, datetime);
        contentValues.put(EVENT_CREATED_BY, created);
        long result = db.insert(TABLE_EVENT,null,contentValues);
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }
    public boolean inserBroadcaseEvent(String contactname,String phonenumber,String eventname,String datetime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BEVENT_CONTACT,contactname);
        contentValues.put(BEVENT_PHONENUMBER,phonenumber);
        contentValues.put(BEVENT_NAME,eventname);
        contentValues.put(BEVENT_DATE_TIME, datetime);
        long result = db.insert(TABLE_BEVENT,null,contentValues);
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public boolean inserFriends(String name,String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FRIEND_NAME, name);
        contentValues.put(FRIEND_NUMBER, phone);
        long result = db.insert(TABLE_FRIEND,null,contentValues);
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }
    public boolean inserFriendsUnknown(String name,String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FRIEND_NAME, name);
        contentValues.put(FRIEND_NUMBER, phone);
        long result = db.insert(TABLE_FRIEND_UNKOWN,null,contentValues);
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }
    public boolean inserIntoProfile(String name,String phone,String dob){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFILE_NAME, name);
        contentValues.put(PROFILE_PHONENUMBER, phone);
        contentValues.put(PROFILE_BIRTHDAY, dob);
        long result = db.insert(TABLE_PROFILE,null,contentValues);
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }
    public Cursor getContact(){
        SQLiteDatabase  db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select phone_number from "+ TABLE_STUDENTS,null);
        return res;
    }
    public Cursor getContactName(String number){
        SQLiteDatabase db = this.getWritableDatabase();
        String num = "'%"+number+"%'";
        Cursor res = db.rawQuery("select * from students where phone_number like "+num,null);
        if (res != null) {
            res.moveToFirst();
        }
        return  res;

    }
    public Cursor getProfile(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from "+TABLE_PROFILE,null);
        if (res != null) {
            res.moveToFirst();
        }
        return  res;

    }

    public Cursor getAllEvent() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from "+ TABLE_EVENT,null);
        return res;
    }
    public Cursor getAllFriends() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res1 = db.rawQuery("select * from " + TABLE_FRIEND + " WHERE " + FRIEND_NAME + " !='You' order by " + FRIEND_NAME + " COLLATE NOCASE", null);
        //Cursor res2 = db.rawQuery("select * from " + TABLE_FRIEND_UNKOWN + " order by " + FRIEND_NAME + " COLLATE NOCASE", null);

        //MergeCursor mergeCursor = new MergeCursor(new Cursor[]{res1,res2});
        return res1;
    }
    public Cursor getAllFriendsChat() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from "+ TABLE_FRIEND +" WHERE "+FRIEND_NAME+" !='You' order by "+FRIEND_NUMBER+ " COLLATE NOCASE",null);
        return res;
    }
    public Cursor getAllContacts() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor mCursor = db.rawQuery("SELECT * from students order by name COLLATE NOCASE", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor getAllEvents() {

        SQLiteDatabase db = DataBaseHelper.this.getWritableDatabase();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dNow = new Date();
        //int mm = dNow.getMinutes();
        //dNow.setMinutes(mm-10);
        dNow.setHours(00);
        dNow.setMinutes(00);
        String formattedDate = df.format(dNow);
        Cursor mCursor1 = db.rawQuery("SELECT * from events where datetime(date_time) > '"+formattedDate+"' order by date_time asc",null);
        Cursor mCursor2 = db.rawQuery("SELECT * from events where datetime(date_time) < '"+formattedDate+"' order by date_time asc",null);
        Cursor mCursorNO = db.rawQuery("SELECT * from events order by date_time asc",null);
        if (mCursor1 != null) {
            mCursor1.moveToFirst();
        }
        if (mCursor2 != null) {
            mCursor2.moveToFirst();
        }

        MergeCursor mergeCursor = new MergeCursor(new Cursor[]{mCursor1,mCursor2});
        return mergeCursor;
    }
    public Cursor getAllBroadcastEvents() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor mCursor = db.query(TABLE_BEVENT, new String[]{BEVENT_ID, BEVENT_PHONENUMBER, BEVENT_NAME},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor getLastId() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT last_insert_rowid()", null);
        return res;
    }

    public Cursor getAllEventsSet() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select contact_name,event_name,date_time from " + TABLE_EVENT, null);

        return res;
    }
    public Cursor getEvent(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_EVENT+" WHERE "+EVENT_ID +" = "+id, null);
        return res;
    }
    public boolean deleteEvent(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_EVENT, EVENT_ID + "=" + name, null) > 0;
    }
    public boolean editEvent(String id,String name,String datetime,String eventtype){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("UPDATE "+TABLE_EVENT+" SET "
                +EVENT_NAME+" = '"+name+"',"
                +EVENT_DATE_TIME+" = '"+datetime
                +"', "+EVENT_TYPE+" = '"+eventtype
                +"' WHERE "+EVENT_ID+" = "+id,null);

        if(res != null){
            res.moveToFirst();
            res.close();
            return true;
        }else {
            return false;
        }
    }
    public Cursor getName(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c= db.rawQuery("select * from "+TABLE_STUDENTS+" where phone_number = "+phone,null);
        if(c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor fetchCountriesByName(String inputText) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dNow = new Date();
        int mm = dNow.getMinutes();
        dNow.setMinutes(mm-10);
        String formattedDate = df.format(dNow);

        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = db.rawQuery("SELECT * from events where datetime(date_time) > '"+formattedDate+"' order by date_time asc",null);

        }
        else {
            mCursor = db.rawQuery("SELECT * from events where contact_name like '%"+inputText+"%' and datetime(date_time) > '"+formattedDate+"' order by contact_name asc",null);

        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    public Cursor fetchContactByName(String inputText) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.w(TAG, inputText);
        Cursor mCursor = null;

        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = db.rawQuery("SELECT * from students order by name COLLATE NOCASE",null);

        }
        else {
            mCursor = db.rawQuery("SELECT * from students where name like '%"+inputText+"%' order by name asc",null);

        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor getDeletedContacts() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor mCursor = db.rawQuery("SELECT * FROM studentsbackup WHERE phone_number_backup NOT IN (SELECT phone_number FROM students)", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public boolean timeStamp(String number,String time){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor =db.rawQuery("UPDATE " + TABLE_FRIEND + " SET "
                + FRIEND_TIMESTAMP + " = '" + time
                + "' WHERE " + FRIEND_NUMBER + " LIKE '" + number + "'", null);
        //Cursor res = db.update(TABLE_FRIEND,)
        if(cursor != null){
            cursor.moveToLast();
            cursor.close();
            return true;
        }else {
            return false;
        }

    }
    public boolean timeStamp2(String name, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(FRIEND_TIMESTAMP, time);

        return db.update(TABLE_FRIEND, args, FRIEND_NUMBER + "=" + name, null) > 0;
    }
}

