package com.example.sqlitetest.displayContactActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitetest.R;
import com.example.sqlitetest.utility.Contact;
import com.example.sqlitetest.utility.Description;
import com.example.sqlitetest.utility.Helper;

import java.util.ArrayList;
import java.util.List;

public class DisplayContactAdapter extends RecyclerView.Adapter<DisplayContactAdapter.DisplayContactViewHolder> implements Filterable {
    List<Description> _descriptions;
    List<Description> _descriptionsFull;
    Context context;
    boolean _multiSelect = false;
    List<Description> selectedItems = new ArrayList<Description>();

    public DisplayContactAdapter(Contact contact, Context context) {
            this._descriptions = contact.getDescriptions();
            this.context = context;
            this._descriptionsFull = new ArrayList<>(contact.getDescriptions());
        }
        @NonNull
        @Override
        public DisplayContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.contact_display_row, parent, false);
            return new DisplayContactViewHolder(view);
        }

        void selectItem(DisplayContactViewHolder holder, Description description) {
            if(selectedItems.contains(description)) {
                selectedItems.remove(description);
                holder.content.setAlpha(1.0f);
                holder.time.setAlpha(1.0f);
            } else {
                selectedItems.add(description);
                holder.content.setAlpha(0.3f);
                holder.time.setAlpha(0.3f);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final DisplayContactViewHolder holder, int position) {
            final Description description = _descriptions.get(position);
            holder.content.setText(description.getContent());
            holder.time.setText(Helper.unixToDMY(description.getLastmodified()));

            //reset selection when descriptions are modified
            holder.content.setAlpha(1.0f);
            holder.time.setAlpha(1.0f);

            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(_multiSelect) {
                        if(selectedItems.size() == 1 && selectedItems.get(0) == description) {
                            _multiSelect = false;
                        }
                        selectItem(holder, description);
                    }
                }
            });
            holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(!_multiSelect) {
                        _multiSelect = true;
                        selectItem(holder, description);
//                        DisplayContactActivity.changeToolbar();
                    }
                    return true;
                }
            });

    }
    void set_descriptions(List<Description> descriptions) {
        _descriptions = descriptions;
        _descriptionsFull = new ArrayList<>(descriptions);
    }
    void clearSelectedItems() {
        selectedItems.clear();
        _multiSelect = false;
    }
    @Override
    public int getItemCount() {
        return _descriptions.size();
    }

    //filter search results
    @Override
    public Filter getFilter() {
        return descriptionFilter;
    }

    private Filter descriptionFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Description> filteredList = new ArrayList<Description>();
            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(_descriptionsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Description description : _descriptionsFull) {
                    if(description.getContent().toLowerCase().contains(filterPattern)) {
                        filteredList.add(description);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _descriptions.clear();
            _descriptions.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class DisplayContactViewHolder extends RecyclerView.ViewHolder {
        public static final String EXTRA_MESSAGE = "com.example.sqlitetest.MESSAGE";
        TextView content, time;
        ConstraintLayout constraintLayout;

        public DisplayContactViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.contactContent);
            time = itemView.findViewById(R.id.timeView);
            constraintLayout = itemView.findViewById(R.id.contactDisplayRow);
        }
    }
}
