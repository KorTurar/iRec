package com.example.irec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CallsAdapter extends RecyclerView.Adapter<CallsAdapter.ViewHolder> {

    public Context context;
    private ArrayList<Call> calls= new ArrayList<Call>();
    public CallsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.callDate.setText(calls.get(position).getDate());
        holder.callType.setText(calls.get(position).getType()+": ");
        holder.callNumber.setText(calls.get(position).getNumber());
        holder.callName.setText(calls.get(position).getName());
        holder.callDuration.setText(calls.get(position).getDuration());
    }

    @Override
    public int getItemCount() {
        return calls.size();
    }

    public void setCalls(ArrayList<Call> calls) {
        this.calls = calls;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView callDate;
        public TextView callNumber;
        public TextView callName;
        public TextView callDuration;
        public TextView callType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            callDate = (TextView) itemView.findViewById(R.id.call_date);
            callType = (TextView) itemView.findViewById(R.id.call_type);
            callNumber= (TextView) itemView.findViewById(R.id.call_number);
            callName = (TextView) itemView.findViewById(R.id.call_name);
            callDuration = (TextView) itemView.findViewById(R.id.call_duration);
        }
    }
    
}


