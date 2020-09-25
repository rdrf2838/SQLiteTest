package com.example.sqlitetest.displayContactActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitetest.R;
import com.example.sqlitetest.utility.Contact;

import java.util.List;

public class DisplayContactAdapter extends RecyclerView.Adapter<DisplayContactAdapter.DisplayContactViewHolder>  {
    List<String> descriptions;
    Context context;

    public DisplayContactAdapter(Contact contact, Context context) {
            this.descriptions = contact.getDescriptions();
            this.context = context;
        }
        @NonNull
        @Override
        public DisplayContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.contact_display_row, parent, false);
            return new DisplayContactViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DisplayContactViewHolder holder, int position) {
            holder.description.setText(descriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return descriptions.size();
    }

    public class DisplayContactViewHolder extends RecyclerView.ViewHolder {
        public static final String EXTRA_MESSAGE = "com.example.sqlitetest.MESSAGE";
        TextView description;

        public DisplayContactViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.contactDescription);
        }
    }
}
