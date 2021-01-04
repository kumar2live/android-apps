package com.mkdev.usingdb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mkdev.usingdb.data.DatabaseHandler;
import com.mkdev.usingdb.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsList extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> contactArrayList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        listView = findViewById(R.id.contactsListView);
        contactArrayList = new ArrayList<>();

        getContacts();
    }

    private void getContacts() {
        DatabaseHandler db = new DatabaseHandler(ContactsList.this);
        List<Contact> contactList = db.getContacts();

        for (Contact contact: contactList) {
            Log.d("ContactsList", "contact: " + contact.getName());
            contactArrayList.add(contact.getName());
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactArrayList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ContactsList", "onItemClick: " + position);
            }
        });
    }
}