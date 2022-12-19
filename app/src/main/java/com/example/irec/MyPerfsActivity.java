package com.example.irec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class MyPerfsActivity extends AppCompatActivity {

    private ImageView profileImV;
    private ImageView searchImV;
    private ImageView listsImV;
    private RecyclerView dialedPerfsRecView;
    private RecyclerView savedPerfsRecView;
    private ArrayList<Performer> dialedPerfs;
    private ArrayList<Performer> savedPerfs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        WorkWithDbClass dbClass = new WorkWithDbClass(this);

        dialedPerfs = dbClass.getDialedPerfs();
        savedPerfs = new ArrayList<>();
        dialedPerfsRecView = findViewById(R.id.dialedPerfsRecView);
        savedPerfsRecView = findViewById(R.id.savedPerfsRecView);
        profileImV = findViewById(R.id.profileImV);
        searchImV = findViewById(R.id.searchImV);
        listsImV = findViewById(R.id.listsImV);
        profileImV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPerfsActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        searchImV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPerfsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        MyDialedPerfsAdapter myDialedPerfsAdapter = new MyDialedPerfsAdapter(this);
        myDialedPerfsAdapter.setPerformers();
        dialedPerfsRecView.setAdapter(myDialedPerfsAdapter);
        dialedPerfsRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        MySavedPerfsAdapter mySavedPerfsAdapter = new MySavedPerfsAdapter(this);
        mySavedPerfsAdapter.setPerformers();
        savedPerfsRecView.setAdapter(mySavedPerfsAdapter);
        savedPerfsRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }
}