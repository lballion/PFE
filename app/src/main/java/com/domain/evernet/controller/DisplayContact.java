package com.domain.evernet.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.domain.evernet.R;
import com.domain.evernet.model.Contact;
import com.domain.evernet.model.ContactAdapter;

import java.util.ArrayList;

public class DisplayContact extends AppCompatActivity {

    private String contactName;
    private int  contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);


        Bundle bundle = getIntent().getExtras();

        ArrayList<Contact> contactItemList = bundle.getParcelableArrayList("contact");

        ListView list = findViewById(R.id.contactList);
        ContactAdapter adapter = new ContactAdapter(this,contactItemList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact selectedContact;
                selectedContact = (Contact) adapter.getItem(position);
                contactName = selectedContact.getName();
                contactId = selectedContact.getId();


                Intent contactListActivity = new Intent(DisplayContact.this, DashboardActivity.class);

                Bundle bundle = new Bundle();
                bundle.putInt("id",contactId);
                bundle.putString("contact",contactName);
                contactListActivity.putExtras(bundle);

                startActivity(contactListActivity);
                finish();



            }
        });


    }
}