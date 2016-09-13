package com.example.delevere.cbook.db;


        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteException;
        import android.provider.ContactsContract;

        import com.example.delevere.cbook.DataBaseHelper;

        import java.util.ArrayList;


public class ChatModel {
    private SQLiteDatabase db;
    private DataBaseHelper dbHelper;
    //private DbHelper dbHelper;
    private Context cx;

    public static final String TABLE_CHATS = "chat";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CHAT_SENT_BY = "chat_sent_by";
    public static final String COLUMN_CHAT_SENT_AT = "chatt_sent_at";
    public static final String COLUMN_CHAT_SENT_TO = "chat_sent_to";
    public static final String COLUMN_CHAT_MSG="column_chat_msg";
    public static final String COLUMN_CHAT_ID="column_chat_id";


    public static final String CREATE_CHATS = "create table "
            + TABLE_CHATS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CHAT_SENT_BY + " text null,"
            + COLUMN_CHAT_SENT_AT + " text null,"
            + COLUMN_CHAT_SENT_TO + " text null,"
            + COLUMN_CHAT_ID + " text null,"
            + COLUMN_CHAT_MSG + " text null)";

    private String[] allColumns = {
            COLUMN_ID
            , COLUMN_CHAT_SENT_BY
            , COLUMN_CHAT_SENT_AT
            , COLUMN_CHAT_SENT_TO
            , COLUMN_CHAT_ID
            , COLUMN_CHAT_MSG
    };

    public ChatModel(Context context) {
        dbHelper = new DataBaseHelper(context);
        cx = context;
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }
    

    public static ChatModel getR(Context cx) {
        ChatModel _db = new ChatModel(cx);
        try {
            _db.db = _db.dbHelper.getReadableDatabase();
        } catch (SQLiteException ex) {

        }
        return _db;
    }

    public Boolean addNewRecord(String sentBy, String sentAt, String sendTo, String chatid, String msg) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAT_SENT_BY, sentBy);
        values.put(COLUMN_CHAT_SENT_AT, sentAt);
        values.put(COLUMN_CHAT_SENT_TO, sendTo);
        values.put(COLUMN_CHAT_ID, chatid);
        values.put(COLUMN_CHAT_MSG, msg);
        ChatModel _db = getRW(cx);





        long id = _db.db.insert(TABLE_CHATS, null,
                values);
        closeDBConnection(_db);
        return (id != -1);

    }
    

    public static ChatModel getRW(Context cx) {
        ChatModel _db = new ChatModel(cx);
        try {
            _db.db = _db.dbHelper.getWritableDatabase();
        } catch (SQLException ex) {

        }
        return _db;
    }

    public ArrayList<ChatBean> getChats(String id) throws SQLException {
        ArrayList<ChatBean> drawfts = new ArrayList<>();
        ChatModel _db = getRW(cx);
        Cursor cursor = _db.db.query(TABLE_CHATS, allColumns, COLUMN_CHAT_ID + "=?", new String[]{id}, null, null, COLUMN_ID + " ASC", String.valueOf(100));
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ChatBean d = cursorToChatBean(cursor);
            drawfts.add(d);
            cursor.moveToNext();
        }
        cursor.close();
        return drawfts;

    }


    private ChatBean cursorToChatBean(Cursor cursor) {
        ChatBean ChatBean = new ChatBean();
        ChatBean.setId(cursor.getColumnIndexOrThrow(COLUMN_ID));
        ChatBean.setSentBy(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CHAT_SENT_BY)));
        ChatBean.setSentAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CHAT_SENT_AT)));
        ChatBean.setSentTo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CHAT_SENT_TO)));
        ChatBean.setMsg(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CHAT_MSG)));
        return ChatBean;
    }



    public static int uOt(Context cx, String table, ContentValues values, String whereClause, String[] whereArgs) {
        int affected = 0;
        ChatModel _db = getRW(cx);
        try {
            affected = _db.db.update(table, values, whereClause, whereArgs);
        } catch (SQLException e) {
        }
        closeDBConnection(_db);
        return affected;
    }

    public static void closeDBConnection(ChatModel _db) {
        try {
            if (_db != null && _db.db != null) {
                _db.db.close();
                _db.dbHelper.close();
                if (_db.cx != null) {
                    _db.cx = null;
                }
            }
        } catch (Exception e) {

        }
    }


}
