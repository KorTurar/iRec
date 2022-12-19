package com.example.irec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class PerfListAdapter extends RecyclerView.Adapter<PerfListAdapter.ViewHolder> {

    public Context context;
    private ArrayList<Performer> performers= new ArrayList<Performer>();
    private Bitmap bmp, adjustedBmp;
    private String url = "http://192.168.0.1030:8000/images/";
    public PerfListAdapter(Context context) {
        this.context = context;
        bmp = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.random_photo);

        int x = (bmp.getWidth()-100)/2;
        int y = (bmp.getHeight()-100)/2;
        adjustedBmp = Bitmap.createBitmap(bmp, x, y, 100, 100);
    }

    @NonNull
    @Override
    public PerfListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.performer_item, parent, false);
        return new PerfListAdapter.ViewHolder(view);
    }

/*    @NonNull
    @Override
    public PerfListAdapter.ViewHolder_ForMyList onCreateViewHolder_ForMyList(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.performer_item, parent, false);
        return new PerfListAdapter.ViewHolder_ForMyList(view);
    }*/

    @Override
    public void onBindViewHolder(@NonNull PerfListAdapter.ViewHolder holder, int position) {
        //holder.hisImage.setImageURI(Uri.parse("@drawable/random_photo"));

        //holder.hisImage.setImageBitmap(adjustedBmp);
        holder.hisImage.setImageBitmap(performers.get(position).getHisImage());
        holder.fLName.setText(performers.get(position).getfName()+" "+performers.get(position).getlName());
        holder.lowestPrice.setText( performers.get(position).getLowestPrice()+"тг.");
        holder.amountOfCallsPerWeek.setText(performers.get(position).getAmountOfIncomingCallsPerWeek()+"зв./нед.");
        holder.presentsAndCommunicates.setText(performers.get(position).getPresentsAndCommunicates());/**/

        holder.openAbilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.abilitiesRelLayout.getVisibility()==View.GONE){
                    holder.abilitiesRelLayout.setVisibility(View.VISIBLE);
                }
                else{
                    holder.abilitiesRelLayout.setVisibility(View.GONE);
                }
            }
        });
        AbilListAdapter abilListAdapter = new AbilListAdapter(context);
        abilListAdapter.setAbilities(performers.get(position).getAbilities());
        //holder.abilitiesRelLayout.setVisibility(View.VISIBLE);
        holder.abilitiesRecView.setAdapter(abilListAdapter);
        holder.abilitiesRecView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        //Glide.with(context).load(url+performers.get(position).getHisImage()).into(holder.hisImage);
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPerformer(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return performers.size();
    }

    public void setPerformers(ArrayList<Performer> performers) {
        this.performers = performers;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView hisImage;
        public TextView fLName;
        public TextView lowestPrice;
        public TextView amountOfCallsPerWeek;
        public TextView presentsAndCommunicates;
        public Button invite;
        public RelativeLayout abilitiesRelLayout;
        public RelativeLayout perfItemParent;
        public RecyclerView abilitiesRecView;
        public ImageButton openAbilBtn;
        public ImageButton call;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hisImage = (ImageView) itemView.findViewById(R.id.hisImageImageView);
            fLName = (TextView) itemView.findViewById(R.id.nameTextView);
            lowestPrice = (TextView) itemView.findViewById(R.id.priceTextView);
            amountOfCallsPerWeek = (TextView) itemView.findViewById(R.id.amountOfCallsTextView);
            presentsAndCommunicates = (TextView) itemView.findViewById(R.id.presentsAndCommunicates);
            perfItemParent = (RelativeLayout) itemView.findViewById(R.id.perfItemParent);
            abilitiesRelLayout = (RelativeLayout) itemView.findViewById(R.id.widenedPerfInfo);
            abilitiesRecView = (RecyclerView) itemView.findViewById(R.id.abilitiesRecView);
            openAbilBtn = (ImageButton) itemView.findViewById(R.id.openAbil);
            call = (ImageButton) itemView.findViewById(R.id.callBtn);
        }
    }


   /* public class ViewHolder_ForMyList extends RecyclerView.ViewHolder{

        public ImageView hisImage;
        public TextView fLName;
        public TextView lowestPrice;
        public TextView amountOfCallsPerWeek;
        public TextView presentsAndCommunicates;
        public Button invite;
        public RelativeLayout abilitiesRelLayout;
        public RelativeLayout perfItemParent;
        public RecyclerView abilitiesRecView;
        public ImageButton openAbilBtn;
        public CardView call;

        public ViewHolder_ForMyList(@NonNull View itemView) {
            super(itemView);
            hisImage = (ImageView) itemView.findViewById(R.id.hisImageImageView);
            fLName = (TextView) itemView.findViewById(R.id.nameTextView);
            lowestPrice = (TextView) itemView.findViewById(R.id.priceTextView);
            amountOfCallsPerWeek = (TextView) itemView.findViewById(R.id.amountOfCallsTextView);
            presentsAndCommunicates = (TextView) itemView.findViewById(R.id.presentsAndCommunicates);
            perfItemParent = (RelativeLayout) itemView.findViewById(R.id.perfItemParent);
            abilitiesRelLayout = (RelativeLayout) itemView.findViewById(R.id.widenedPerfInfo);
            abilitiesRecView = (RecyclerView) itemView.findViewById(R.id.abilitiesRecView);
            openAbilBtn = (ImageButton) itemView.findViewById(R.id.openAbil);
            call = (CardView) itemView.findViewById(R.id.callCardView);
        }
    }*/

    public void callPerformer(View view, int position){
        WorkWithDbClass dbClass = new WorkWithDbClass(context);
        Log.d("Call check", "insideCallPerformer");
        //ViewGroup parent = (ViewGroup) view.getParent().getParent().getParent();
        //ViewGroup child = (ViewGroup) view.getParent().getParent();
        //int indexOfMyView = (parent).indexOfChild(child);
        Performer performer = performers.get(position);
        //performer.setAbilities(performers.get(position).getAbilities());

        dbClass.saveDialedPerf(performer);
        Intent intent = new Intent(Intent.ACTION_CALL);

        String phoneNumber = performer.getPhoneNumber();
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);

    }
}
