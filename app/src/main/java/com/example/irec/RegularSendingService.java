package com.example.irec;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

public class RegularSendingService extends Service {
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
                String mode = "regular";
                String amSending = intent.getStringExtra("amSending");
                Log.d("amSending check ", "run: "+amSending);
                if(amSending.equals("contactsEdition")){
                    String dataToSend = intent.getStringExtra("contactsData");

                    SendData sendData = new SendData(RegularSendingService.this, dataToSend, mode);
                    try {
                        sendData.send();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Log.d("My Regular service", "run: Regular service for contact ran");

                }
                else if (amSending.equals( "newCalls")){
                    String dataToSend = intent.getStringExtra("callsData");
                    SendData sendData = new SendData(RegularSendingService.this, dataToSend, mode);
                    try {
                        sendData.send();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Log.d("My Regular service", "run: Regular service for calls ran");
                }
                else if(amSending.equals("profileEdition")) {
                    String dataToSend = intent.getStringExtra("profileData");
                    SendData sendData = new SendData(RegularSendingService.this, dataToSend, mode);
                    try {
                        sendData.send();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Log.d("My Regular service", "run: Regular service for profile ran");
                }
                else if (amSending.equals("searchQuery")){
                    String searchQuery = intent.getStringExtra("searchQuery");
                    SendData sendData = new SendData(RegularSendingService.this, searchQuery, "search");
                    Log.d("My Regular service", "run: Regular service for search ran");
                    sendData.getPerformers();

                }
                else if (amSending.equals("imageQuery")){
                    String searchQuery = intent.getStringExtra("searchQuery");
                    SendData sendData = new SendData(RegularSendingService.this, null, null);
                    Log.d("My Regular service", "run: Regular service for search ran");
                    sendData.getImage();

                }

                else if (amSending.equals("sysUsersAmountQuery")){

                    SendData sendData = new SendData(RegularSendingService.this, null, null);
                    Log.d("My Regular service", "run: Regular service for search ran");
                    sendData.getSysUsersAmount();

                }
                /*Log.d("servive check", "onStartCommand: firstSendingService started");
                String profileData = intent.getStringExtra("profileData");

                GetContacts getContacts = new GetContacts(FirstSendingService.this, "needAllData");
                getContacts.GetTempNames();
                String contactsStr = getContacts.getContacts();


                GetCalls getCalls = new GetCalls(FirstSendingService.this, "needAllData");
                String callsStr = getCalls.GetCallsData();

                JSONObject firstDataToSend = new JSONObject();
                try {
                    firstDataToSend.put("profile", profileData);
                    firstDataToSend.put("contacts", contactsStr);
                    firstDataToSend.put("calls", callsStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String dataToSend = new Gson().toJson(firstDataToSend);
                SendData sendData = new SendData(dataToSend);
                sendData.send();

*/


                RegularSendingService.this.stopSelf();
                Looper.loop();
            }}).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
