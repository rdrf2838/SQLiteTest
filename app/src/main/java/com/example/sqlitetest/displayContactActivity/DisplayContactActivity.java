package com.example.sqlitetest.displayContactActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sqlitetest.R;
import com.example.sqlitetest.mainActivity.MainActivity;
import com.example.sqlitetest.mainActivity.MainContactListAdapter;
import com.example.sqlitetest.utility.Contact;

import java.util.List;

public class DisplayContactActivity extends AppCompatActivity {
    List<Contact> contactList;
    int position;
    Contact contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);

        Intent intent = getIntent();
        position = intent.getIntExtra(MainContactListAdapter.EXTRA_MESSAGE, 0);
        contactList = MainContactListAdapter.getContactList();
        contact = contactList.get(position);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_displayContact);
        DisplayContactAdapter myAdapter = new DisplayContactAdapter(contact, this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView nameView = (TextView) findViewById((R.id.displayContactName));
        nameView.setText(contact.getName());
    }

    public void removeContact(View view) {
        MainActivity.db.deleteContact(contactList.get(position));
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    public void addDescription(View view) {
        EditText editText = (EditText) findViewById(R.id.editTextTextMultiLine);
        String description = editText.getText().toString();
        contact.addDescription(description);
        MainActivity.db.updateContact(contact);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}