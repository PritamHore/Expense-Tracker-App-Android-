package com.webguru.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
    private static final String DATABASE_NAME = "MyDB";
    private static final String CONTACTS_TABLE = "contacts";
//    private static final String MARKS_TABLE = "marks";
    private static final int DATABASE_VERSION = 1;
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_INCOME = "income";
    public static final String KEY_EXPENSE = "expense";
    public static final String KEY_DATE = "date";
    private static final String TABLE_CREATE =
            "create table contacts (_id integer primary key autoincrement, "
                    + "name text not null, income integer , expense integer , date text not null);";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context,
                    DATABASE_NAME,
                    null,
                    DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db)	{
            try {
                db.execSQL(TABLE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			/*
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			*/
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    //---closes the database---
    public void close()	{
        DBHelper.close();
    }

    //---insert few dummy contacts into the database---
//    public void insertDummyData() {
//        final String INSERT_QUERY = "INSERT INTO contacts (_id, name, coast, date) " +
//                "SELECT 1, 'Car', 200, '10/03/2021' " +
//                "UNION SELECT 2, 'Car', 200, '10/03/2021' " +
//                "UNION SELECT 3, 'Gas', 500, '11/03/2021' " +
//                "UNION SELECT 4, 'Car', 200, '10/03/2021' " +
//                "UNION SELECT 5, 'Car', 200, '10/03/2021' " +
//                "UNION SELECT 6, 'Car', 200, '10/03/2021' " +
//                "UNION SELECT 7, 'Car', 200, '10/03/2021' " +
//                "UNION SELECT 8, 'Car', 200, '10/03/2021' " +
//                "UNION SELECT 9, 'Car', 200, '10/03/2021' " +
//                "UNION SELECT 10, 'Car', 200, '10/03/2021';";
//        db.execSQL(INSERT_QUERY);
//    }

    //---insert a contact into the database---
    public long insertContact(String name, String income, String expense, String date) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_INCOME, income);
        values.put(KEY_EXPENSE, expense);
        values.put(KEY_DATE, date);
        return db.insert(CONTACTS_TABLE,null, values);
    }
    //---deletes a particular contact---
    public boolean deleteContact(long rowId) {
        int rc = db.delete(CONTACTS_TABLE,
                KEY_ROWID + "=" + rowId,
                null);
        return (rc > 0) ? true : false;
    }
    //---retrieves all the contacts---
    public Cursor getAllContacts() {
        return db.query(CONTACTS_TABLE,
                new String[]{KEY_ROWID, KEY_NAME, KEY_INCOME, KEY_EXPENSE, KEY_DATE},
                null, null, null,
                null, null);

        //db.execSQL("select * from contacts");
    }
    //---retrieves a particular contact---
    public Cursor getContact(long rowId) throws SQLException {
        return db.query(true, CONTACTS_TABLE,
                new String[] {KEY_ROWID, KEY_NAME, KEY_INCOME, KEY_EXPENSE, KEY_DATE},
                KEY_ROWID + "=" + rowId,
                null,
                null, null, null,
                null);
    }
    //---updates a contact---
    public boolean updateContact(long rowId, String name, String income, String expense, String date) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_INCOME, income);
        args.put(KEY_EXPENSE, expense);
        args.put(KEY_DATE, date);
        return db.update(CONTACTS_TABLE, args,
                KEY_ROWID + "=" + rowId,
                null) > 0;
        //return (rc > 0) ? true : false;
    }
}
