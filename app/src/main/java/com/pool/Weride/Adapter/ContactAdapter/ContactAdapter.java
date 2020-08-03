package com.pool.Weride.Adapter.ContactAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.pool.Weride.Model.Contact;
import com.pool.Weride.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder_Inner> implements android.widget.Filterable {
    
    
    // stores and recycles views as they are scrolled off screen
    class ViewHolder_Inner extends RecyclerView.ViewHolder  {
        /*inner class */
        public ImageView imageView;
        public TextView nameTextview;
        public TextView contactTextview;
        public TextView idTextview;
        private android.widget.LinearLayout ll_main;
        
        
        public ViewHolder_Inner(View itemView) {
            super(itemView);
            ll_main = itemView.findViewById(R.id.ll_main);
            imageView = itemView.findViewById(R.id.tvContactImage);
            nameTextview = itemView.findViewById(R.id.tvContactName);
            contactTextview = itemView.findViewById(R.id.tvPhoneNumber);
            idTextview =itemView.findViewById(R.id.textViewId);
        }
    }
    
    
    List<Contact> modelList;
    List<Contact> filterlist;
    
    private ViewHolder_Inner viewHolder_inner;
    
    public ContactAdapter(List< Contact> items)
    {
        this.modelList = items;
        filterlist =new java.util.ArrayList<>(modelList);
    }
    
    // inflates the row layout from xml when needed
    @Override
    public ViewHolder_Inner onCreateViewHolder( ViewGroup viewGroup, int position)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.list_items, viewGroup,false);
        ViewHolder_Inner viewHolder = new ViewHolder_Inner(v);
        return viewHolder;
    }
    
    
    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder_Inner viewHolder_inner, final int position)
    {
        final  Contact contactlist = modelList.get(position);
        // viewHolder_inner.name
        viewHolder_inner.imageView.setImageResource(contactlist.getImageresource());
        viewHolder_inner.nameTextview.setText(contactlist.getName());
        viewHolder_inner.contactTextview.setText(contactlist.getContact());
        viewHolder_inner.idTextview.setText(Integer.toString(contactlist.getIdtext()));
        
    }
    /*getItemCount() - returns The number of items currently available in adapter*/
    @Override
    public int getItemCount()
    {
        // list.size();
        return modelList.size();
    }
    
    
    /**
     * <p>Returns a filter that can be used to constrain data with a filtering
     * pattern.</p>
     *
     * <p>This method is usually implemented by {@link Adapter}
     * classes.</p>
     *
     * @return a filter used to constrain data
     */
    
    @Override
    public android.widget.Filter getFilter() {
        return examplefilter;
    }
    
    private android.widget.Filter examplefilter = new android.widget.Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List< Contact> filteredlist = new java.util.ArrayList<>();
            
            if (constraint==null|| constraint.length()==0){
            filteredlist.addAll(filterlist);
            }else {
                String filterpattern = constraint.toString().trim();
                for ( Contact contact:filteredlist ) {
                    if (contact.getContact().toLowerCase().contains(filterpattern)){
                        filteredlist.add(contact);
                    }
                }
            
            }
            FilterResults results = new FilterResults();
            results.values = filteredlist;
            return results;
            
        }
    
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        modelList.clear();
        modelList.addAll((List)results.values);
        notifyDataSetChanged();
        }
        
    };
    
   
}
