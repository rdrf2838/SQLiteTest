package com.example.sqlitetest.displayContactActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.sqlitetest.R;
import com.example.sqlitetest.mainActivity.MainActivity;
import com.example.sqlitetest.mainActivity.MainContactListAdapter;
import com.example.sqlitetest.utility.Contact;
import com.example.sqlitetest.utility.DatabaseHandler;
import com.example.sqlitetest.utility.Description;
import com.example.sqlitetest.utility.Helper;

public class DisplayContactActivity extends AppCompatActivity {
    DatabaseHandler db = MainActivity.db;
    DisplayContactAdapter myAdapter;
    int _contact_id;
    Contact _contact;
    static Toolbar myToolbar;
    boolean isSelected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);

        Intent intent = getIntent();
        _contact_id = intent.getIntExtra(MainContactListAdapter.EXTRA_MESSAGE, 0);
        _contact = db.getContact(_contact_id);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_displayContact);
        myAdapter = new DisplayContactAdapter(_contact, this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle(_contact.getName());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isSelected = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display_contact_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search_2);

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

    public void addDescription(View view) {
        EditText editText = (EditText) findViewById(R.id.editTextTextMultiLine);
        String content = editText.getText().toString();
        Description description = new Description(content, Helper.getUnixTime());
        _contact.addDescription(description);
        editText.getText().clear();

        hideKeyboard(this);
        editText.clearFocus();
        db.updateContact(_contact);
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

    public void updateContact(Contact contact) {
        MainActivity.db.updateContact(contact);
        this._contact = contact;
    }
    public boolean removeContact() {
        MainActivity.db.deleteContact(_contact);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    public boolean removeDescriptions() {
        _contact.removeDescriptions(myAdapter.selectedItems);
        MainActivity.db.updateContact(_contact);
        refreshRecyclerList();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.removeContact:
                removeContact();
            case R.id.removeDescriptions:
                removeDescriptions();
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void refreshRecyclerList() {
        myAdapter.set_descriptions(_contact.getDescriptions());
        myAdapter.clearSelectedItems();
        myAdapter.notifyDataSetChanged();
    }
}