package com.mkdev.usingroom.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mkdev.usingroom.model.Contact;
import com.mkdev.usingroom.utils.ContactRoomDB;

import java.util.List;

public class ContactRepository {
    private ContactDao contactDao;
    private LiveData<List<Contact>> contactsList;

    public ContactRepository(Application application) {
        ContactRoomDB contactRoomDB = ContactRoomDB.getDatabase(application);
        contactDao = contactRoomDB.contactDao();
        contactsList = contactDao.getAllContacts();
    }

    public LiveData<List<Contact>> getAllData() {
        return contactsList;
    }

    public LiveData<Contact> getData(int id) {
        return contactDao.getContact(id);
    }

    public void update(Contact contact) {
        ContactRoomDB.databaseWriteExecutor.execute(() -> {
            contactDao.update(contact);
        });
    }

    public void delete(Contact contact) {
        ContactRoomDB.databaseWriteExecutor.execute(() -> {
            contactDao.delete(contact);
        });
    }

    public void insert(Contact contact) {
        ContactRoomDB.databaseWriteExecutor.execute(() -> {
            contactDao.insert(contact);
        });
    }
}
