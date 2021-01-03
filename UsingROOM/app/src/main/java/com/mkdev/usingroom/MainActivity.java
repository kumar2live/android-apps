package com.mkdev.usingroom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mkdev.usingroom.model.Contact;
import com.mkdev.usingroom.model.ContactViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 3;
    private ContactViewModel contactViewModel;
    private TextView textView;
    private FloatingActionButton fabAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        fabAddContact = findViewById(R.id.fabAddContact);

        fabAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewContact.class);
            startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);
        });

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this
                .getApplication())
                .create(ContactViewModel.class);

        contactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                StringBuilder builder = new StringBuilder();
                for (Contact contact: contacts) {
                    builder.append(" - ").append(contact.getName());
                    Log.d("Contacts", "onChanged: " + contact.getName());
                }
                textView.setText(builder.toString());
            }
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
}