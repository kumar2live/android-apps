package com.mkdev.usingroom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mkdev.usingroom.adapter.RecyclerViewAdapter;
import com.mkdev.usingroom.model.Contact;
import com.mkdev.usingroom.model.ContactViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener {
    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 3;
    private static final String TAG = "MainActivity";
    private ContactViewModel contactViewModel;
//    private TextView textView;
    private FloatingActionButton fabAddContact;
    private RecyclerView contactsListView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LiveData<List<Contact>> contactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        textView = findViewById(R.id.textView);
        fabAddContact = findViewById(R.id.fabAddContact);
        contactsListView = findViewById(R.id.contactsListView);
        contactsListView.setHasFixedSize(true);
        contactsListView.setLayoutManager(new LinearLayoutManager(this));


        fabAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewContact.class);
            startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);
        });

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this
                .getApplication())
                .create(ContactViewModel.class);

        contactViewModel.getAllContacts().observe(this, contacts -> {
//                StringBuilder builder = new StringBuilder();
//                for (Contact contact: contacts) {
//                    builder.append(" - ").append(contact.getName());
//                    Log.d("Contacts", "onChanged: " + contact.getName() + " " + contact.getOccupation());
//                }
//                textView.setText(builder.toString());
            // set up adapter

            recyclerViewAdapter = new RecyclerViewAdapter(contacts, MainActivity.this, this);
            contactsListView.setAdapter(recyclerViewAdapter);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d("main_activity", "onActivityResult: " + data.getStringExtra(NewContact.NAME_REPLY));

            Contact contact = new Contact(data.getStringExtra(NewContact.NAME_REPLY), data.getStringExtra(NewContact.OCCUPATION_REPLY));
            contactViewModel.insert(contact);
        }
    }

    @Override
    public void onContactClick(int position) {
        Log.d(TAG, "onContactClick: " + position);
    }
}