package com.mkdev.usingdb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mkdev.usingdb.data.DatabaseHandler;
import com.mkdev.usingdb.model.Contact;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonInitiate;
    private Button buttonGetContacts;
    private Button buttonAddContact;
    private Button buttonGetContact;
    private Button buttonUpdateContact;
    private Button buttonDeleteContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonInitiate = findViewById(R.id.buttonInitiate);
        buttonInitiate.setOnClickListener(this);
        buttonGetContacts = findViewById(R.id.buttonGetContacts);
        buttonGetContacts.setOnClickListener(this);
        buttonAddContact = findViewById(R.id.buttonAddContact);
        buttonAddContact.setOnClickListener(this);
        buttonGetContact = findViewById(R.id.buttonGetContact);
        buttonGetContact.setOnClickListener(this);
        buttonUpdateContact = findViewById(R.id.buttonUpdateContact);
        buttonUpdateContact.setOnClickListener(this);
        buttonDeleteContact = findViewById(R.id.buttonDeleteContact);
        buttonDeleteContact.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonInitiate:
                break;
            case R.id.buttonGetContacts:
                getContacts();
                break;
            case R.id.buttonAddContact:
                addContact();
                break;
            case R.id.buttonGetContact:
                getContact();
                break;
            case R.id.buttonUpdateContact:
                updateContact();
                break;
            case R.id.buttonDeleteContact:
                deleteContact();
                break;
        }
    }

    private void updateContact() {
        DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
        Contact contactData = databaseHandler.getContact(1);

        contactData.setName("Muthu");
        int updatedRow = databaseHandler.updateContact(contactData);
        Log.d("MainActivity", "updateContact: " + updatedRow);
    }

    private void deleteContact() {
        DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
        Contact contactData = databaseHandler.getContact(1);

        databaseHandler.deleteContact(contactData);
    }

    private void getContact() {
        DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);

        Contact contactData = databaseHandler.getContact(1);

        Log.d("MainActivity", "getContact: " + contactData.getName());
    }

    private void addContact() {
        DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);

        Contact mk = new Contact();
        mk.setName("Mk");
        mk.setPhone("11111111");

        databaseHandler.addContact(mk);
    }

    private void getContacts() {
        DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);

        Contact mk = new Contact();
        List<Contact> contactList = databaseHandler.getContacts();

        for (Contact contact: contactList) {
            Log.d("MainActivity", "addContact: " + contact.getName());
        }

        getContactsCount();
    }

    private void getContactsCount() {
        DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
        int dataCount = databaseHandler.getContactsCount();

        Log.d("MainActivity", "getContactsCount: " + dataCount);
        Log.d("MainActivity", "getContactsCount: " + buttonGetContacts.getText());

        if (dataCount > 0)
            buttonGetContacts.setText(buttonGetContacts.getText() + " (" + dataCount + ")");
    }


}