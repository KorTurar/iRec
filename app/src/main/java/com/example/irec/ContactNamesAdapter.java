package com.example.irec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactNamesAdapter extends RecyclerView.Adapter<ContactNamesAdapter.ViewHolder>{

    public Context context;
    private ArrayList<TempNames> contactNames= new ArrayList<TempNames>();
    public ContactNamesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.temp_name_item, parent, false);
        return new ContactNamesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tempId.setText(contactNames.get(position).getId());
        holder.tempName.setText(contactNames.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return contactNames.size();
    }

    public void setContactsIds(ArrayList<TempNames> contactsIds) {
        this.contactNames = contactsIds;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tempId;
        private TextView tempName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tempId = (TextView) itemView.findViewById(R.id.tempId);
            tempName = (TextView) itemView.findViewById(R.id.tempName);

        }
    }
}
