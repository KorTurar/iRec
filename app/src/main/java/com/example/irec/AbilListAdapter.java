package com.example.irec;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class AbilListAdapter extends RecyclerView.Adapter<AbilListAdapter.ViewHolder> {

    public Context context;
    private ArrayList<String> abilities= new ArrayList<String>();
    public AbilListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AbilListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ability_item, parent, false);
        return new AbilListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbilListAdapter.ViewHolder holder, int position) {
        holder.abilityTextView.setText(abilities.get(position));/**/
    }

    @Override
    public int getItemCount() {
        return abilities.size();
    }

    public void setAbilities(ArrayList<String> abilities) {
        this.abilities = abilities;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView abilityTextView;
        public RelativeLayout abilItemParent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            abilityTextView = (TextView) itemView.findViewById(R.id.abilityTexView);
            abilItemParent = (RelativeLayout) itemView.findViewById(R.id.abilItemParent);
        }
    }
}