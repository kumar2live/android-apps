package com.mkdev.usingroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mkdev.usingroom.model.Contact;
import com.mkdev.usingroom.model.ContactViewModel;

public class NewContact extends AppCompatActivity {
    public static final String NAME_REPLY = "name_reply";
    public static final String OCCUPATION_REPLY = "occupation_reply";
    private EditText textContactName;
    private EditText textOccupation;
    private Button buttonAddContact;
    private Button buttonUpdate;
    private Button buttonDelete;
    private int contactId;
    private Boolean isEdit = false;

    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(
                NewContact.this
                .getApplication())
                .create(ContactViewModel.class);

        textContactName = findViewById(R.id.textContactName);
        textOccupation = findViewById(R.id.textOccupation);
        buttonAddContact = findViewById(R.id.buttonAddContact);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        contactId = 0;

        Bundle data = getIntent().getExtras();

        if(getIntent().hasExtra(MainActivity.CONTACT_ID)) {
            contactId = getIntent().getIntExtra(MainActivity.CONTACT_ID, 0);
            contactViewModel.getContact(contactId).observe(this, contact -> {
                if (contact != null) {
                    textContactName.setText(contact.getName());
                    textOccupation.setText(contact.getOccupation());
                }
            });

            isEdit = true;
        }

        if(isEdit) {
            buttonAddContact.setVisibility(View.GONE);
        } else {
            buttonUpdate.setVisibility(View.GONE);
            buttonDelete.setVisibility(View.GONE);
        }

        buttonAddContact.setOnClickListener(v -> {
            Intent replyIntent = new Intent();

            if(!TextUtils.isEmpty(textContactName.getText()) && !TextUtils.isEmpty(textOccupation.getText())) {
                String name = textContactName.getText().toString();
                String occupation = textOccupation.getText().toString();

                replyIntent.putExtra(NAME_REPLY, name);
                replyIntent.putExtra(OCCUPATION_REPLY, occupation);

                setResult(RESULT_OK, replyIntent);
                finish();

//                Contact contact = new Contact(name, occupation);
//                contactViewModel.insert(contact);
            } else  {
                setResult(RESULT_CANCELED, replyIntent);
//                Toast.makeText(NewContact.this, R.string.empty, Toast.LENGTH_SHORT).show();
            }
        });

        buttonDelete.setOnClickListener(v -> {
            contactViewModel.getContact(contactId).observe(this, contact -> {
                if (contact != null) {
                    Log.d("buttonDelete", "onCreate: " + "buttonDelete");
                    contactViewModel.delete(contact);
                    finish();
                }
            });
        });

        buttonUpdate.setOnClickListener(v -> {
            String name = textContactName.getText().toString();
            String occupation = textOccupation.getText().toString();

            if(!TextUtils.isEmpty(textContactName.getText()) && !TextUtils.isEmpty(textOccupation.getText())) {
                Contact contact = new Contact();
                contact.setId(contactId);
                contact.setName(name);
                contact.setOccupation(occupation);
                contactViewModel.update(contact);

                finish();

            } else  {
                Toast.makeText(NewContact.this, R.string.empty, Toast.LENGTH_SHORT).show();
            }
        });
    }
}