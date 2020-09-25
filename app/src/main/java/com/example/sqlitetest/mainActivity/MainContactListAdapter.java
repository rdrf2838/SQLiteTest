package com.example.sqlitetest.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitetest.displayContactActivity.DisplayContactActivity;
import com.example.sqlitetest.R;
import com.example.sqlitetest.utility.Contact;
import com.example.sqlitetest.utility.Helper;

import java.util.List;

public class MainContactListAdapter extends RecyclerView.Adapter<MainContactListAdapter.ContactListViewHolder> {
    public static final String EXTRA_MESSAGE = "com.example.sqlitetest.MESSAGE";
    static List<Contact> contactList;
    Context context;
    public MainContactListAdapter(Context ct, List<Contact> cL) {
        context = ct;
        contactList = cL;
    }
    public static List<Contact> getContactList() {
        return contactList;
    }

    @NonNull
    @Override
    public ContactListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new ContactListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListViewHolder holder, int position) {
        holder.name.setText(contactList.get(position).getName());
        holder.lastmodified.setText(Helper.unixToHMSDMY(contactList.get(position).getLastmodified()));
        holder.setPos(position);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ContactListViewHolder extends RecyclerView.ViewHolder {
        TextView name, lastmodified;
        int pos;
        public void setPos(int position) {
            this.pos = position;
        }

        public ContactListViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            lastmodified = itemView.findViewById(R.id.lastModified);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DisplayContactActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, pos);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
