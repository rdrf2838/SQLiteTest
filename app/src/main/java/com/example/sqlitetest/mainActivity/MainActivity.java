package com.example.sqlitetest.mainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sqlitetest.R;
import com.example.sqlitetest.utility.Contact;
import com.example.sqlitetest.utility.DatabaseHandler;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static DatabaseHandler db;
    Button addContactButton;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        List<Contact> contacts = db.getAllContacts();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        MainContactListAdapter myAdapter = new MainContactListAdapter(this, contacts);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addContactButton = (Button) findViewById(R.id.add_contact);
    }

//    All the button methods
    public void clearDatabase(View view) {
        db.reset();
    }
    public void addContact(View view) {
        EditText nameView = (EditText) findViewById(R.id.editTextTextPersonName);
        String name = nameView.getText().toString();
        Contact contact = new Contact(name);
        db.addContact(contact);
        finish();
        startActivity(getIntent());
    }
}