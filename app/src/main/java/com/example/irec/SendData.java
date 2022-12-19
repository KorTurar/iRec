package com.example.irec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class SendData {
    private String data;
    public boolean  dataSuccesfullySentAndInserted;
    private Context context;
    public String mode;
    private String UrlBase;
    private byte[] photoByteArray;
//    public SendData(ArrayList callObjects) {
//        this.data = new Gson().toJson(callObjects);
//    }
    public SendData(Context context, String data, String mode) {
        this.context = context;
        this.data = data;
        this.mode = mode;
        this.UrlBase = "http://192.168.0.103:8000/";
    }

    public SendData(Context context, String data, String mode, byte[] photoByteArray) {
        this.context = context;
        this.data = data;
        this.mode = mode;
        this.UrlBase = "http://192.168.0.103:8000/";
        this.photoByteArray = photoByteArray;
    }

    public void send() throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("photo", new ByteArrayInputStream(photoByteArray), "myImage.jpg");
        params.put("data", data);

        //params.put("calls", "12345");
        AsyncHttpClient client = new AsyncHttpClient();
        //client.setConnectTimeout(1000*60);
        client.setTimeout(1000*60);
        client.setResponseTimeout(1000*60*2);
        client.post(UrlBase, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String str = new String(response, StandardCharsets.UTF_8);

                Log.d("проканало", headers.toString()+"   "+statusCode+"   "+str);

                    dataSuccesfullySentAndInserted = true;
                    /*WorkWithDbClass dbClass = new WorkWithDbClass(context);
                    dbClass.makeTables();
                    dbClass.makeRecordThatFirstDataHasBeenSent();
                    GetCalls.regContentObserver();
                    GetContacts.regContentObserver();*/



                    /*RecyclerView recyclerView = ((Activity)context).findViewById(R.id.perfRecyclerView);
                    ArrayList<Performer> performersList;
                    PerfListAdapter perfListAdapter = new PerfListAdapter(context);
                    perfListAdapter.setPerformers(performersList);
                    recyclerView.setAdapter(perfListAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));*/

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                String str = new String(errorResponse, StandardCharsets.UTF_8);
                Log.d("не проканало", statusCode+"   "+str + "  "+e);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }
        });
    }

    public void getPerformers(){
        RequestParams params = new RequestParams();
        params.put("data", data);
        //params.put("calls", "12345");
        AsyncHttpClient client = new AsyncHttpClient();
        //client.setConnectTimeout(1000*60);
        client.setTimeout(1000*60);
        client.setResponseTimeout(1000*60*2);
        Log.d("send check", "getPerformers: am sending");
        client.get(UrlBase+"search/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String str = new String(response, StandardCharsets.UTF_8);

                Log.d("проканало", headers.toString()+"   "+statusCode+"   "+str);
                Intent intent = new Intent("com.example.irec");
                intent.putExtra("purpose", "transferServerResponse");
                intent.putExtra("response", str);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                //context.sendBroadcast(intent);






                /*RecyclerView recyclerView = ((Activity)context).findViewById(R.id.perfRecyclerView);
                ArrayList<Performer> performersList;
                PerfListAdapter perfListAdapter = new PerfListAdapter(context);
                perfListAdapter.setPerformers(performersList);
                recyclerView.setAdapter(perfListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));*/

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                String str = new String(errorResponse, StandardCharsets.UTF_8);
                Log.d("не проканало", statusCode+"   "+str + "  "+e);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }
        });
    }

    public void getImage(){
        RequestParams params = new RequestParams();
        params.put("data", data);
        //params.put("calls", "12345");
        AsyncHttpClient client = new AsyncHttpClient();
        //client.setConnectTimeout(1000*60);
        client.setTimeout(1000*60);
        client.setResponseTimeout(1000*60*2);
        Log.d("send check", "getImage: am sending");
        client.get(UrlBase+"images/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String str = new String(response, StandardCharsets.UTF_8);

                Log.d("проканало", headers.toString()+"   "+statusCode+"   "+str);
                /*Bitmap bitmap = BitmapFactory.decodeByteArray(response , 0, response .length);
                ImageView testImV = (ImageView)((Activity)context).findViewById(R.id.testImV);
                testImV.setImageBitmap(bitmap);*/
               Intent intent = new Intent("com.example.irec");
                intent.putExtra("image", response);
                intent.putExtra("gotImages", true);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent); /**/






                /*RecyclerView recyclerView = ((Activity)context).findViewById(R.id.perfRecyclerView);
                ArrayList<Performer> performersList;
                PerfListAdapter perfListAdapter = new PerfListAdapter(context);
                perfListAdapter.setPerformers(performersList);
                recyclerView.setAdapter(perfListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));*/

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                String str = new String(errorResponse, StandardCharsets.UTF_8);
                Log.d("не проканало", statusCode+"   "+str + "  "+e);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }
        });
    }

    public void getSysUsersAmount(){
        RequestParams params = new RequestParams();
        params.put("data", data);
        //params.put("calls", "12345");
        AsyncHttpClient client = new AsyncHttpClient();
        //client.setConnectTimeout(1000*60);
        client.setTimeout(1000*60);
        client.setResponseTimeout(1000*60*2);
        Log.d("send check", "getSysUsersAmount: am sending");
        client.get(UrlBase+"sysusersamount/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String str = new String(response, StandardCharsets.UTF_8);

                Log.d("проканало", headers.toString()+"   "+statusCode+"   "+str);
                /*Bitmap bitmap = BitmapFactory.decodeByteArray(response , 0, response .length);
                ImageView testImV = (ImageView)((Activity)context).findViewById(R.id.testImV);
                testImV.setImageBitmap(bitmap);*/
                Intent intent = new Intent("com.example.irec");
                intent.putExtra("content", "sysUsersAmount");
                intent.putExtra("sysUsersAmount", str);

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent); /**/






                /*RecyclerView recyclerView = ((Activity)context).findViewById(R.id.perfRecyclerView);
                ArrayList<Performer> performersList;
                PerfListAdapter perfListAdapter = new PerfListAdapter(context);
                perfListAdapter.setPerformers(performersList);
                recyclerView.setAdapter(perfListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));*/

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                String str = new String(errorResponse, StandardCharsets.UTF_8);
                Log.d("не проканало", statusCode+"   "+str + "  "+e);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }
        });
    }

    public void praiseSavedPerf(){
        RequestParams params = new RequestParams();
        params.put("data", data);
        //params.put("calls", "12345");
        AsyncHttpClient client = new AsyncHttpClient();
        //client.setConnectTimeout(1000*60);
        client.setTimeout(1000*60);
        client.setResponseTimeout(1000*60*2);
        client.post(UrlBase+"praisePerf/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String str = new String(response, StandardCharsets.UTF_8);

                Log.d("проканало", headers.toString()+"   "+statusCode+"   "+str);





                    /*RecyclerView recyclerView = ((Activity)context).findViewById(R.id.perfRecyclerView);
                    ArrayList<Performer> performersList;
                    PerfListAdapter perfListAdapter = new PerfListAdapter(context);
                    perfListAdapter.setPerformers(performersList);
                    recyclerView.setAdapter(perfListAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));*/

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                String str = new String(errorResponse, StandardCharsets.UTF_8);
                Log.d("не проканало", statusCode+"   "+str + "  "+e);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }
        });
    }
}
