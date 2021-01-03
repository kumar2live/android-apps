package com.mkdev.usingroom.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mkdev.usingroom.data.ContactDao;
import com.mkdev.usingroom.model.Contact;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactRoomDB extends RoomDatabase {
    public abstract ContactDao contactDao();
    private static final int NUMBER_OF_THREADS = 4;
    private static volatile ContactRoomDB INSTANCE;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ContactRoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ContactRoomDB.class) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        ContactRoomDB.class, "room_contacts_database")
                        .addCallback(sRoomDatabaseCallback)
                        .build();
            }
        }

        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                ContactDao contactDao = INSTANCE.contactDao();
                contactDao.deleteAll();

                Contact contact = new Contact("Muthu", "Software Developer");
                contactDao.insert(contact);
                contact = new Contact("Kumar", "Engineer");
                contactDao.insert(contact);
            });
        }
    };
}
