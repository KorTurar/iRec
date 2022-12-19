package com.example.irec;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MySavedPerfsAdapter extends RecyclerView.Adapter<MySavedPerfsAdapter.ViewHolder> {

    public Context context;
    private ArrayList<Performer> performers= new ArrayList<Performer>();
    private ArrayList<Performer> savedPerfs;
    WorkWithDbClass dbClass;
    private Bitmap bmp, adjustedBmp;
    //private String url = "http://192.168.0.1030:8000/images/";
    public MySavedPerfsAdapter(Context context) {

        this.context = context;
        dbClass = new WorkWithDbClass(context);
        bmp = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.random_photo);

        int x = (bmp.getWidth()-100)/2;
        int y = (bmp.getHeight()-100)/2;
        adjustedBmp = Bitmap.createBitmap(bmp, x, y, 100, 100);

        this.savedPerfs = dbClass.getSavedPerfs();
    }

    @NonNull
    @Override
    public MySavedPerfsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_saved_perf_item, parent, false);
        return new MySavedPerfsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MySavedPerfsAdapter.ViewHolder holder, int position) {
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
                callPerformer(v);
            }
        });

        holder.deleteSavedPerfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSavedFromDb(view);
            }
        });

        holder.praiseSavedPerfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                praiseSavedPerf(view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return performers.size();
    }

    public void setPerformers() {
        this.performers = dbClass.getSavedPerfs();
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
        public CardView call;
        public Button deleteSavedPerfBtn;
        public Button praiseSavedPerfBtn;

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
            call = (CardView) itemView.findViewById(R.id.callCardView);
            deleteSavedPerfBtn = (Button) itemView.findViewById(R.id.delete);
            praiseSavedPerfBtn = (Button) itemView.findViewById(R.id.praiseSavedPerf);
        }
    }

    public void callPerformer(View view){


        int indexOfMyView = ((ViewGroup) view.getParent().getParent().getParent()).indexOfChild((ViewGroup) view.getParent().getParent());
        Performer performer = performers.get(indexOfMyView);
        String phoneNumber = performer.getPhoneNumber();

        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);

    }

    public void deleteSavedFromDb(View view){

        int indexOfMyView = ((ViewGroup) view.getParent().getParent().getParent().getParent()).indexOfChild((ViewGroup) view.getParent().getParent().getParent());
        Performer performer = performers.get(indexOfMyView);
        String fName = performer.getfName();
        String lName = performer.getlName();
        String phoneNumber = performer.getPhoneNumber();

        dbClass.deleteSavedPerf(fName, lName, phoneNumber);
        Toast.makeText(context, fName+" "+lName+" удалён из сохранённых", Toast.LENGTH_SHORT).show();
    }

    public void praiseSavedPerf(View view){
        int indexOfMyView = ((ViewGroup) view.getParent().getParent().getParent().getParent()).indexOfChild((ViewGroup) view.getParent().getParent().getParent());
        Performer performer = performers.get(indexOfMyView);

        JSONObject jsonObject = new JSONObject((Map) performer);


        SendData sendData = new SendData(context, jsonObject.toString(), null);
        sendData.praiseSavedPerf();
    }

    /*public void addToSavedPerfsList(View view){

        int indexOfMyView = ((ViewGroup) view.getParent().getParent().getParent().getParent()).indexOfChild((ViewGroup) view.getParent().getParent().getParent());
        Performer performer = performers.get(indexOfMyView);
        String fName = performer.getfName();
        String lName = performer.getlName();
        String phoneNumber = performer.getPhoneNumber();

        dbClass.addChosenFromDialedPerfToSaved(fName, lName, phoneNumber);

        savedPerfs.add(new Performer(fName, lName, phoneNumber, null, null, null, null, null, false));
        Toast.makeText(context, fName+" "+lName+" добавлен в список сохранённых", Toast.LENGTH_SHORT).show();
    }*/
}
