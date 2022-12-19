package com.example.irec;



import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import java.io.FileNotFoundException;

public class SendingService extends JobIntentService {


    static void enqueueWork(Context context, Intent work){
        Log.d("check before", "enqueueWork: "+work.toString());
        enqueueWork(context, SendingService.class, 123, work);
        Log.d("check after", "enqueueWork: "+work.toString());
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String str = intent.getStringExtra("calls");

//        for (int i=0;i<10;i++){
//            Log.d("checking loop", "onHandleWork: "+str+" - "+i);
//            SystemClock.sleep(1000);
//        }
        Log.d("проверка на null", "onHandleWork: "+str);
        SendData sendData = new SendData(SendingService.this,str, "first");
        Log.d("проверка на null", "onHandleWork: "+sendData.toString());
        try {
            sendData.send();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onStopCurrentWork() {
        return super.onStopCurrentWork();
    }

    //    @Override
//    public void onStart(Intent intent, int startid) {
//        //intent.getS
//        //SendData sendData = new SendData();
//        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
//    }
}
