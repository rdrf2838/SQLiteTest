package com.example.sqlitetest.mainActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.sqlitetest.R;
import com.example.sqlitetest.utility.Contact;
import com.example.sqlitetest.utility.DatabaseHandler;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static DatabaseHandler db;
    Button addContactButton;
    RecyclerView recyclerView;
    MainContactListAdapter myAdapter;
    List<Contact> _contacts;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    //initialises database, recyclerview, toolbar etc.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        _contacts = db.getAllContacts();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        myAdapter = new MainContactListAdapter(this, _contacts);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addContactButton = (Button) findViewById(R.id.add_contact);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

    }

    //display menu items in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    //effects of selecting menu items in toolbar

    //    All the button methods
    public void clearDatabase() {
        db.reset();
        refreshRecyclerList();
        db.reset();
    }
    public void addContact(View view) {
        EditText nameView = (EditText) findViewById(R.id.editTextTextPersonName);
        String name = nameView.getText().toString();
        nameView.getText().clear();

        hideKeyboard(this);
        nameView.clearFocus();
        addContact(name);
        refreshRecyclerList();
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void addContact(String name) {
        Contact contact = new Contact(name);
        db.addContact(contact);
        _contacts = db.getAllContacts();
    }
    public void removeContacts() {
        List<Contact> selectedContacts = myAdapter._selectedItems;
        for(Contact contact : selectedContacts) db.deleteContact(contact);
        _contacts = db.getAllContacts();
        refreshRecyclerList();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
            case R.id.action_clearDatabase:
                clearDatabase();
            case R.id.action_removeContacts:
                removeContacts();
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    public void refreshRecyclerList() {
        myAdapter.set_contactList(_contacts);
        myAdapter.clearSelectedItems();
        myAdapter.notifyDataSetChanged();
    }
}