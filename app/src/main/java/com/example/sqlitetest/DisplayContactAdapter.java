package com.example.sqlitetest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DisplayContactAdapter extends RecyclerView.Adapter<DisplayContactAdapter.DisplayContactViewHolder>  {
    List<String> attributeList;
    Context context;

    public DisplayContactAdapter(Contact contact, Context context) {
        this.attributeList = contact.getAttributes();
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
        holder.attribute.setText(attributeList.get(position));
    }

    @Override
    public int getItemCount() {
        return attributeList.size();
    }

    public class DisplayContactViewHolder extends RecyclerView.ViewHolder {
        public static final String EXTRA_MESSAGE = "com.example.sqlitetest.MESSAGE";
        TextView attribute;

        public DisplayContactViewHolder(@NonNull View itemView) {
            super(itemView);
            attribute = itemView.findViewById(R.id.contactAttributes);
        }
    }
}
