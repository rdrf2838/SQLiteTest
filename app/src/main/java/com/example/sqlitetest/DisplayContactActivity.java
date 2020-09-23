package com.example.sqlitetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

public class DisplayContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);

        Intent intent = getIntent();
        int position = intent.getIntExtra(ContactListAdapter.EXTRA_MESSAGE, 0);
        List<Contact> contactList = ContactListAdapter.getContactList();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_displayContact);
        DisplayContactAdapter myAdapter = new DisplayContactAdapter(contactList.get(position), this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}