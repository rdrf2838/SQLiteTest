package com.example.sqlitetest.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    //Table 1: name|id|lastmodified
    private static final String DATABASE_NAME = "contactsManager";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LASTMODIFIED = "lastmodified";

    //table 2: name|description
    private static final String TABLE_DESCRIPTIONS = "descriptions";
    //KEY_ID
    private static final String DESCRIPTION = "description";
    //KEY_LASTMODIFIED

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS +
                        "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                        + KEY_NAME + " TEXT," +
                        KEY_LASTMODIFIED + " INT" +
                        ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        String CREATE_DESCRIPTIONS_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_DESCRIPTIONS +
                        "(" +
                        KEY_ID + " TEXT," +
                        DESCRIPTION + " TEXT," +
                        KEY_LASTMODIFIED + " INT" +
                        ")";
        db.execSQL(CREATE_DESCRIPTIONS_TABLE);
    }

    public void reset() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESCRIPTIONS);
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
//
//        // Create tables again
//        onCreate(db);
    }


    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_LASTMODIFIED, Helper.getUnixTime());
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public Contact getContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        //query 1: get contact from table_contacts
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + KEY_ID + " = " + id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();

        //query 2: get contact's descriptions from table_descriptions
        String FILTER_DESCRIPTIONS = "SELECT " + DESCRIPTION +","+KEY_LASTMODIFIED+ " FROM " + TABLE_DESCRIPTIONS + " WHERE " + KEY_ID + " = " +id + " ORDER BY "+ KEY_LASTMODIFIED +" DESC";
        Cursor cursor2 = db.rawQuery(FILTER_DESCRIPTIONS, null);
        List<Description> descriptionList = new ArrayList<Description>();
        if(cursor2.moveToFirst()) {
            do {
                Description description = new Description(cursor2.getString(0), Integer.parseInt(cursor2.getString(1)) );
                descriptionList.add(description);
            } while (cursor2.moveToNext());
        }

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)), descriptionList);
        return contact;
    }

    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " ORDER BY "+ KEY_LASTMODIFIED +" DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setLastmodified(Integer.parseInt(cursor.getString(2)));


                List<Description> descriptionList = new ArrayList<Description>();

                String FILTER_DESCRIPTIONS = "SELECT " + DESCRIPTION +","+KEY_LASTMODIFIED+ " FROM " + TABLE_DESCRIPTIONS + " WHERE " + KEY_ID + " = " +id + " ORDER BY "+ KEY_LASTMODIFIED +" DESC";
                Cursor cursor2 = db.rawQuery(FILTER_DESCRIPTIONS, null);
                        if (cursor2.moveToFirst()) {
                            do {
                                Description description = new Description(cursor2.getString(0),Integer.parseInt(cursor2.getString(1)));
                                descriptionList.add(description);
                    } while (cursor2.moveToNext());
                }
                contact.setDescriptions(descriptionList);
                contactList.add(contact);

            } while (cursor.moveToNext());
        }

        return contactList;
    }

    public List<Contact> getAllContacts(String query) {
        List<Contact> contactList = new ArrayList<Contact>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE "+KEY_NAME + " = " +  query + "%' ORDER BY "+ KEY_LASTMODIFIED +" DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setLastmodified(Integer.parseInt(cursor.getString(2)));


                List<Description> descriptionList = new ArrayList<Description>();

                String FILTER_DESCRIPTIONS = "SELECT " + DESCRIPTION +","+KEY_LASTMODIFIED+ " FROM " + TABLE_DESCRIPTIONS + " WHERE " + KEY_ID + " = " +id + " ORDER BY "+ KEY_LASTMODIFIED +" DESC";
                Cursor cursor2 = db.rawQuery(FILTER_DESCRIPTIONS, null);
                if (cursor2.moveToFirst()) {
                    do {
                        Description description = new Description(cursor2.getString(0),Integer.parseInt(cursor2.getString(1)));
                        descriptionList.add(description);
                    } while (cursor2.moveToNext());
                }
                contact.setDescriptions(descriptionList);
                contactList.add(contact);

            } while (cursor.moveToNext());
        }

        return contactList;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, contact.getID());
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_LASTMODIFIED, Helper.getUnixTime());

        //clear old descriptions, then add again
        List<Description> descriptions = contact.getDescriptions();
        db.delete(TABLE_DESCRIPTIONS, KEY_ID + " =? ", new String[] { String.valueOf(contact.getID()) });
        for(int i = 0; i < descriptions.size(); i++) {
            Description description = descriptions.get(i);
            ContentValues values2 = new ContentValues();
            values2.put(KEY_ID, contact.getID());
            values2.put(DESCRIPTION, description._content);
            values2.put(KEY_LASTMODIFIED, description._lastmodified);
            db.insert(TABLE_DESCRIPTIONS, null, values2);
        }

        // updating rows
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.delete(TABLE_DESCRIPTIONS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

}
