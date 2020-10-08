package com.example.sqlitetest.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sqlitetest.displayContactActivity.DisplayContactActivity;
import com.example.sqlitetest.R;
import com.example.sqlitetest.utility.Contact;
import com.example.sqlitetest.utility.Helper;
import java.util.ArrayList;
import java.util.List;

public class MainContactListAdapter extends RecyclerView.Adapter<MainContactListAdapter.ContactListViewHolder> implements Filterable {
    public static final String EXTRA_MESSAGE = "com.example.sqlitetest.MESSAGE";
    List<Contact> _contactList;
    List<Contact> _selectedItems = new ArrayList<Contact>();
    List<Contact> _contactListFull;
    boolean _multiSelect = false;
    Context context;

    public MainContactListAdapter(Context ct, List<Contact> contactList) {
        context = ct;
        _contactList = contactList;
        _contactListFull = new ArrayList<>(contactList);
    }

    public void set_contactList(List<Contact> contactList) {
        this._contactList = contactList;
        _contactListFull = new ArrayList<>(contactList);
    }

    @NonNull
    @Override
    public ContactListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.main_row, parent, false);
        return new ContactListViewHolder(view);
    }

    void selectItem(ContactListViewHolder holder, Contact contact) {
        if(_selectedItems.contains(contact)) {
            _selectedItems.remove(contact);
            holder.name.setAlpha(1.0f);
            holder.lastmodified.setAlpha(1.0f);
        } else {
            _selectedItems.add(contact);
            holder.name.setAlpha(0.3f);
            holder.lastmodified.setAlpha(0.3f);
        }
    }

    void clearSelectedItems() {
        _multiSelect = false;
        _selectedItems.clear();
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactListViewHolder holder, final int position) {
        final Contact contact = _contactList.get(position);
        holder.name.setText(contact.getName());
        holder.lastmodified.setText(Helper.unixToDMY(contact.getLastmodified()));
        holder.setId(contact.getID());

        //reset to unselected
        holder.name.setAlpha(1.0f);
        holder.lastmodified.setAlpha(1.0f);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_multiSelect) {
                    if(_selectedItems.size() == 1 && _selectedItems.get(0) == contact) {
                        _multiSelect = false;
                    }
                    selectItem(holder, contact);
                } else {
                    Intent intent = new Intent(v.getContext(), DisplayContactActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, contact.getID());
                    v.getContext().startActivity(intent);
                }
            }
        });
        holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!_multiSelect) {
                    _multiSelect = true;
                    selectItem(holder, contact);
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return _contactList.size();
    }

    //filter search results
    @Override
    public Filter getFilter() {
        return contactFilter;
    }

    private Filter contactFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contact> filteredList= new ArrayList<Contact>();
            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(_contactListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Contact contact : _contactListFull) {
                    if(contact.getName().toLowerCase().contains(filterPattern)) {
                       filteredList.add(contact);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _contactList.clear();
            _contactList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class ContactListViewHolder extends RecyclerView.ViewHolder {
        TextView name, lastmodified;
        int contact_id;
        public void setId(int contactid) {
            this.contact_id = contactid;
        }
        ConstraintLayout constraintLayout;

        public ContactListViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            lastmodified = itemView.findViewById(R.id.lastModified);
            constraintLayout = itemView.findViewById(R.id.constraintlayoutmyrow);
        }
    }
}
