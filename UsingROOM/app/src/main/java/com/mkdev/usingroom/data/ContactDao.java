package com.mkdev.usingroom.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mkdev.usingroom.model.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Contact contact);

    @Query("DELETE FROM room_contacts_table")
    void deleteAll();

    @Query("SELECT * FROM room_contacts_table ORDER BY name ASC")
    LiveData<List<Contact>> getAllContacts();

}
