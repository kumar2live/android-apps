package com.mkdev.usingdb.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mkdev.usingdb.MainActivity;
import com.mkdev.usingdb.R;
import com.mkdev.usingdb.model.Contact;
import com.mkdev.usingdb.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(@Nullable Context context) {
        super(context, Utils.DATABASE_NAME, null, Utils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DEV_CONTACTS_TABLE = "CREATE TABLE " + Utils.TABLE_NAME +
                "(" + Utils.KEY_ID + " INTEGER PRIMARY KEY," + Utils.KEY_NAME +
                " TEXT," + Utils.KEY_PHONE_NUMBER + " TEXT" + ")";
        db.execSQL(CREATE_DEV_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf(R.string.db_drop);
        db.execSQL(DROP_TABLE, new String[]{Utils.DATABASE_NAME});

        onCreate(db);
    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Utils.KEY_NAME, contact.getName());
        values.put(Utils.KEY_PHONE_NUMBER, contact.getPhone());

        db.insert(Utils.TABLE_NAME, null, values);
        db.close();
    }

    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                Utils.TABLE_NAME,
                new String[]{Utils.KEY_ID, Utils.KEY_NAME, Utils.KEY_PHONE_NUMBER},
                Utils.KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Contact contact = new Contact();
        contact.setId(Integer.parseInt(cursor.getString(0)));
        contact.setName(cursor.getString(1));
        contact.setPhone(cursor.getString(2));

        return contact;
    }

    public int getContactsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + Utils.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }

    public List<Contact> getContacts() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + Utils.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        if(cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhone(cursor.getString(2));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Utils.KEY_NAME, contact.getName());
        values.put(Utils.KEY_PHONE_NUMBER, contact.getPhone());

        return db.update(Utils.TABLE_NAME, values, Utils.KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Utils.TABLE_NAME, Utils.KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
        db.close();
    }
}
