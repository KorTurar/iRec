package com.example.irec;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.util.Iterator;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Call> callObjects;
    private ArrayList<TempNames> contactsIds;
    private ArrayList<Contact> contactsObjects;
    private RecyclerView recyclerView=null;
    private Button scheduleBtn, stopScheduleBtn;
    private String phoneID = "355773088117612";
    private ImageButton searchButton;
    private EditText searchEditText;
    private BroadcastReceiver mMessageReceiver;
    private MyContactsObserver contentObserver;
    private Bitmap hisImageBmp;
    private ArrayList<Performer> performers = new ArrayList<>();
    private ImageView profileImV;
    private ImageView searchImV;
    private ImageView listsImV;
    private MyCanvas myCanvas;
    private int sysUsersAmount;
    private RelativeLayout root;

    public int getSysUsersAmount() {
        return sysUsersAmount;
    }

    public ArrayList<Performer> getPerformers() {
        return performers;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        WorkWithDbClass dbClass = new WorkWithDbClass(this);
        //dbClass.deleteMyTables();
        dbClass.makeTables();

        final String[] NECESSARY_PERMISSIONS = new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.CALL_PHONE};
        ActivityCompat.requestPermissions(
                this,
                NECESSARY_PERMISSIONS, 123);
        regContentObserver();
        //ImageView test = findViewById(R.id.test);
        profileImV = findViewById(R.id.profileImV);
        searchImV = findViewById(R.id.searchImV);
        listsImV = findViewById(R.id.listsImV);
        root = findViewById(R.id.root);
        profileImV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        listsImV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyPerfsActivity.class);
                startActivity(intent);
            }
        });
        URL url = null;
        try {
            url = new URL("http://192.168.0.1030:8000/images/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //String url = "http://192.168.0.1030:8000/images/";
        //Glide.with(this).load(url).into(test);

        //findOutSysUsersAmount();

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get extra data included in the Intent
                //String message = intent.getStringExtra("Status");
                Log.d("intent check", "onCreate: "+intent.getStringExtra("purpose"));
                //String str = intent.getStringExtra("response");

                //test for receiving image
                /*byte[] image = intent.getByteArrayExtra("image");
                Bitmap bitmap = BitmapFactory.decodeByteArray(image , 0, image.length);
                ImageView testImV = (ImageView) findViewById(R.id.testImV);
                testImV.setImageBitmap(bitmap);*/
                /*if(intent.getStringExtra("content").equals("sysUsersAmount"))
                {
                    String sysUAStr = intent.getStringExtra("sysUsersAmount");
                    try {
                        JSONObject sysUAJSON = new JSONObject(sysUAStr);
                        sysUsersAmount = sysUAJSON.getInt("sysUsersAmount");
                        myCanvas = new MyCanvas(MainActivity.this, null);
                        if (sysUsersAmount < 5){
                            root.addView(myCanvas, 1);
                            myCanvas.setVisibility(View.VISIBLE);

                        }
                        else{
                            myCanvas.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }*/
                if (!(intent.getBooleanExtra("gotImages", false))){


                    Performer performer;
                    String str = intent.getStringExtra("response");
                    Log.d("json receive check", "onCreate: "+str);
                    try {
                        JSONObject testJSOFromServer=new JSONObject(new String(str));
                        //JSONObject jsonObject = new JSONObject(str);
                        Iterator<String> keys = testJSOFromServer.keys();

                        while(keys.hasNext()) {
                            String key = keys.next();
                            if (testJSOFromServer.get(key) instanceof JSONObject) {
                                // do something with jsonObject here

                                JSONObject profile = (JSONObject) ((JSONObject) testJSOFromServer.get(key)).get("profile");
                                JSONObject abilities = (JSONObject) ((JSONObject) testJSOFromServer.get(key)).get("abilities");
                                String fName = profile.getString("fName");
                                String lName = profile.getString("lName");
                                String phoneNumber = profile.getString("phoneNumber");
                                String imageName = profile.getString("image");
                                String lowestPrice = profile.getString("lowestPrice");
                                String callsPerWeek = profile.getString("callsPerWeek");
                                String presentsAndCommunicates = profile.getString("presentsAndCommunicates");
                                boolean openToOffers = profile.getBoolean("openToOffers");

                                ArrayList<String> abilitiesOfPerf = new ArrayList<>();

                                Ability ability=null;
                                Iterator<String> abilitiesKeys = abilities.keys();
                                String abilitiesKey = abilitiesKeys.next();
                                if((abilities.get(abilitiesKey).getClass())!=JSONObject.class){
                                    int actionId = abilities.getInt("actionId");
                                    if (actionId!=2){
                                        String subjects = abilities.getString("subjects");
                                        ability = new Ability(actionId, subjects, null, null);

                                    }
                                    if (actionId==2){
                                        JSONArray worksJSArray = abilities.getJSONArray("works");
                                        String[] works = new String[worksJSArray.length()];
                                        for (int i = 0; i < worksJSArray.length(); i++ ){
                                            works[i]= (String) worksJSArray.get(i);
                                        }
                                        ability = new Ability(actionId, null, works, null);
                                    }
                                    String abilityStr = ability.getAbilityString();
                                    abilitiesOfPerf.add(abilityStr);
                                }
                                else{
                                    Iterator<String> keysForAbilities = abilities.keys();
                                    while(keysForAbilities.hasNext()) {
                                        String keyForAbilities = keysForAbilities.next();
                                         //abilities.get(keyForAbilities);
                                        Log.d("int to JSON", "onReceive: "+(abilities.get(keyForAbilities)).toString());
                                        JSONObject singleAbility = (JSONObject) abilities.get(keyForAbilities);
                                        int actionId = singleAbility.getInt("actionId");
                                        if (actionId!=2){
                                            String subjects = singleAbility.getString("subjects");
                                            ability = new Ability(actionId, subjects, null, null);

                                        }
                                        if (actionId==2){
                                            JSONArray worksJSArray = singleAbility.getJSONArray("works");
                                            String[] works = new String[worksJSArray.length()];
                                            for (int i = 0; i < worksJSArray.length(); i++ ){
                                                works[i]= (String) worksJSArray.get(i);
                                            }
                                            ability = new Ability(actionId, null, works, null);
                                        }
                                        String abilityStr = ability.getAbilityString();
                                        abilitiesOfPerf.add(abilityStr);
                                    }
                                }

                                performer = new Performer(fName, lName,phoneNumber, imageName, lowestPrice, abilitiesOfPerf, presentsAndCommunicates, callsPerWeek, openToOffers);
                                performers.add(performer);
                            }
                        }
                        /*JSONObject perf = testJSOFromServer.getJSONObject("performer_1");
                        JSONObject prof = perf.getJSONObject("profile");
                        String image = prof.getString("image");
                        Log.d("image name from resp", "onReceive: "+image);*/
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                    sendImageQueryToServer();

                }
                else{
                    byte[] byteArray = intent.getByteArrayExtra("image");
                    hisImageBmp = BitmapFactory.decodeByteArray( byteArray, 0, byteArray.length);
                    for (Performer p: performers){
                        p.setHisImage(hisImageBmp);
                    }
                    RecyclerView recyclerView = findViewById(R.id.perfRecyclerView);
                    recyclerView.setVisibility(View.VISIBLE);
                    //ArrayList<Performer> performersList;
                    PerfListAdapter perfListAdapter = new PerfListAdapter(MainActivity.this);
                    perfListAdapter.setPerformers(performers);
                    recyclerView.setAdapter(perfListAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));


                }
                 /*TextView test = (TextView) findViewById(R.id.testTextViewForCommunicationCheck);
                String str = intent.getStringExtra("response");
                ImageView testImV = (ImageView) findViewById(R.id.testImV);
                testImV.setImageURI(Uri.parse(str));*/
             /*   try {
                    JSONObject json = new JSONObject(str);
                    String testValueFromServer = json.getString("myKey");
                    test.setText(testValueFromServer);

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/


               /*try {
                    JSONObject jsonObject = new JSONObject(str);
                    Log.d("json log show", "onReceive: "+jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                } */


                /*RecyclerView recyclerView = ((Activity)context).findViewById(R.id.perfRecyclerView);
                ArrayList<Performer> performersList;
                PerfListAdapter perfListAdapter = new PerfListAdapter(context);
                perfListAdapter.setPerformers(performersList);
                recyclerView.setAdapter(perfListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));*/
            }
        };
//        IntentFilter intFilt = new IntentFilter("com.example.irec");
//        // регистрируем (включаем) BroadcastReceiver
//        registerReceiver(mMessageReceiver, intFilt);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("com.example.irec"));/**/
        searchButton = (ImageButton) findViewById(R.id.searchPerfsImageButton);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendQueryToServer();
            }
        });
        /*Intent intent = getIntent();
        if (intent!=null) {
            Log.d("intent check", "onCreate: " + intent.getStringExtra("purpose"));
            TextView test = (TextView) findViewById(R.id.testTextViewForCommunicationCheck);
            String str = intent.getStringExtra("response");

            try {
                JSONObject json = new JSONObject(str);
                String testValueFromServer = json.getString("myKey");
                test.setText(testValueFromServer);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

        /*recyclerView = findViewById(R.id.recView);
        scheduleBtn = findViewById(R.id.btnSchedule);
        stopScheduleBtn = findViewById(R.id.btnScheduleStop);
        scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ScheduleJob();
                }
            }
        });

        stopScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    CancelJob();
                }
            }
        });*/
        Log.d("current api level", "onCreate: " + Integer.valueOf(Build.VERSION.SDK_INT));


        int CallLogReadPerm = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG);
        int ContactsReadPerm = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);
        int PhoneStateReadPerm = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        int PhoneNumberReadPerm = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS);
        int SMSReadPerm = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_NUMBERS);
/*        if (CallLogReadPerm == 0 && ContactsReadPerm == 0 && PhoneStateReadPerm == 0 && PhoneNumberReadPerm == 0 && SMSReadPerm == 0) {

            //Permission is granted
            try {
                Log.d("phoneID", "onCreate: " + getPhoneID());
                Log.d("phoneNumber", "onCreate: " + getPhoneNumber());

                //GetCalls();
                //showCallsAsList();
                //enqueueWork();
                //sendDataToServer();
                //GetContacts();
                //showContactsNamesAsList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            //ask for permission

            ActivityCompat.requestPermissions(
                    this,
                    NECESSARY_PERMISSIONS, 123);
            try {
                Log.d("phoneID", "onCreate: " + getPhoneID());
                Log.d("phoneNumber", "onCreate: " + getPhoneNumber());
                //GetCalls();
                //showCallsAsList();
                //enqueueWork();
                //sendDataToServer();
                //GetContacts();
                //showContactsNamesAsList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        //new DownloadImageTask((ImageView) findViewById(R.id.testImV)).execute("http://192.168.0.103:8000/uploads/profiles/20210628_204330.jpg");

    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Log.d("intent check", "onCreate-onnewintent: "+intent.getStringExtra("purpose"));
        //TextView test = (TextView) findViewById(R.id.testTextViewForCommunicationCheck);
        String str = intent.getStringExtra("response");

        try {
            JSONObject json = new JSONObject(str);
            String testValueFromServer = json.getString("myKey");
            //test.setText(testValueFromServer);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void GetCalls() {

        String[] projection = {
                CallLog.Calls.DURATION,
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE
        };
        // Return only outgoing calls.
        //String where = CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=?"; //query for all the calls in database
        String where = "(" + CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=?) AND (" + CallLog.Calls.DATE + ">?)";//query for last calls, not older than 60 minutes
        long now = new Date().getTime();
        Date dateNow = new Date(now);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String DateNowInYMD = formatter.format(dateNow);
        long timeForLastCalls = now - (1000 * 60 * 60 * 12); //time for calls of last 12 hours
        Date dateLastCalls = new Date(timeForLastCalls);
        //DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String DateLastCallsInYMD = formatter.format(dateLastCalls);
        Log.d("check Date", "GetCalls: " + DateNowInYMD + "  " + DateLastCallsInYMD);

        //String[] whereArgs = {String.valueOf(CallLog.Calls.OUTGOING_TYPE), String.valueOf(CallLog.Calls.INCOMING_TYPE), String.valueOf(CallLog.Calls.MISSED_TYPE)}; //args for all calls
        String[] whereArgs = {String.valueOf(CallLog.Calls.OUTGOING_TYPE), String.valueOf(CallLog.Calls.INCOMING_TYPE), String.valueOf(CallLog.Calls.MISSED_TYPE), String.valueOf(timeForLastCalls)};
        // Get a Cursor over the Call Log Calls Provider.
        Cursor cursor =
                getContentResolver().query(CallLog.Calls.CONTENT_URI,
                        projection, where, whereArgs, null);
        // Get the index of the columns.
        int durIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION);
        int numberIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER);
        int nameIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME);
        int callTypeIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE);
        int dateIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.DATE);
        // Initialize the result set.
        String[] result = new String[cursor.getCount()];
        callObjects = new ArrayList<>();
        // Iterate over the result Cursor.
        while (cursor.moveToNext()) {
            String durStr = cursor.getString(durIdx);
            String numberStr = cursor.getString(numberIdx);
            String nameStr = cursor.getString(nameIdx) == null ? "noname" : cursor.getString(nameIdx);
            String DateInMilli = cursor.getString(dateIdx);
            String callType = cursor.getString(callTypeIdx);


            //String x = "1086073200000";
            long foo = Long.parseLong(DateInMilli);

            Date date = new Date(foo);
            //DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

            String DateInYMD = formatter.format(date);


            result[cursor.getPosition()] = DateInYMD + ": " + callType + "   " + numberStr + " for " + durStr + "sec" +
                    ((null == nameStr) ?
                            "" : " (" + nameStr + ")");
            Call call = new Call(DateInYMD, numberStr, nameStr, durStr, callType);
            callObjects.add(call);
            Log.d("Calls", result[cursor.getPosition()]);
        }
        // Close the Cursor.
        cursor.close();

    }

    public void GetContacts() {
        // Create a projection that limits the result Cursor
        // to the required columns.
        String[] projectionForContactNames = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        // Get a Cursor over the Contacts Provider.
        Cursor cursor =
                getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                        projectionForContactNames, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            // Get the index of the columns.
            int nameIdx =
                    cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
            int idIdx =
                    cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
            // Initialize the result set.
            String[] stringResultForContactNames = new String[cursor.getCount()];
            contactsIds = new ArrayList();
            // Iterate over the result Cursor.
            while (cursor.moveToNext()) {
                // Extract the name.
                String name = cursor.getString(nameIdx);
                // Extract the unique ID.
                String id = cursor.getString(idIdx);
                stringResultForContactNames[cursor.getPosition()] = name + " (" + id + ")";
                if (name != null && id != null) {
                    TempNames tempNames = new TempNames(id, name);
                    Log.d("getting temp names", tempNames.getName());
                    contactsIds.add(tempNames);
                }
            }
            // Close the Cursor.
            cursor.close();
        }



        /**/
        // Create a new Cursor searching for the data associated
        // with the returned Contact ID.

        // Return all the PHONE data for the contact.
        for (TempNames tempName : contactsIds) {
            String where = ContactsContract.Data.CONTACT_ID +
                    " = " + tempName.getId() + " AND " +
                    ContactsContract.Data.MIMETYPE + " = '" +
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE +
                    "'";
            String[] projection = new String[]{
                    ContactsContract.Data.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            };
            Cursor dataCursor =
                    getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                            projection, where, null, null);
            // Get the indexes of the required columns.
            int nameFullContactIdx =
                    dataCursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME);
            int phoneIdx = dataCursor.getColumnIndexOrThrow(
                    ContactsContract.CommonDataKinds.Phone.NUMBER);
            String[] stringResultForContactData = new String[dataCursor.getCount()];
            contactsObjects = new ArrayList<>();
            while (dataCursor.moveToNext()) {
                // Extract the name.
                String name = dataCursor.getString(nameFullContactIdx);
                // Extract the phone number.
                String number = dataCursor.getString(phoneIdx);
                stringResultForContactData[dataCursor.getPosition()] = name + " (" + number + ")";

                Contact contact = new Contact(tempName.getId(), name, number, "initial");
                contactsObjects.add(contact);
                Log.d("getting full contact", contact.getId() + " " + contact.getName() + " " + contact.getNumber());
            }
            dataCursor.close();
        }





/*                ContentResolver cr = getContentResolver();
                String[] stringResultForContactData = null;
        // Find a contact using a partial name match

        // Create a projection of the required column names.
                String[] projectionForContactData = new String[] {
                        ContactsContract.Contacts._ID
                };
        // Get a Cursor that will return the ID(s) of the matched name.
                Cursor idCursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        projectionForContactData, null, null, null);
        // Extract the first matching ID if it exists.
                String id = null;
                if (idCursor.moveToFirst()) {
                    int idIdx =
                            idCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
                    id = idCursor.getString(idIdx);
                }
        // Close that Cursor.
                idCursor.close();*/


    }

    public void showCallsAsList() {
        CallsAdapter callsAdapter = new CallsAdapter(this);
        callsAdapter.setCalls(callObjects);
        recyclerView.setAdapter(callsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }


    public void showContactsNamesAsList() {
        ContactNamesAdapter contactNamesAdapter = new ContactNamesAdapter(this);
        contactNamesAdapter.setContactsIds(contactsIds);
        //Log.d("check adapter", ContactNamesAdapter.toString());
        recyclerView.setAdapter(contactNamesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    public void showContactsAsList() {
        CallsAdapter callsAdapter = new CallsAdapter(this);
        callsAdapter.setCalls(callObjects);
        recyclerView.setAdapter(callsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    public void sendDataToServer() throws IOException {
        String data = new Gson().toJson(callObjects);
        Log.d("size", "sendDataToServer: " + callObjects.size());
        /*String queryUrl = "http://192.168.0.103:8000/";

        URL url = new URL(queryUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);

        OutputStream oStream = conn.getOutputStream();
        oStream.write(data.getBytes("UTF-8"));
        oStream.close();*/

        RequestParams params = new RequestParams();
        params.put("calls", data);
        //params.put("calls", "12345");
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://192.168.0.103:8000/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String str = new String(response, StandardCharsets.UTF_8);
                Log.d("проканало", headers.toString() + "   " + statusCode + "   " + str);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                String str = new String(errorResponse, StandardCharsets.UTF_8);
                Log.d("не проканало", statusCode + "   " + str + "  " + e);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

    public void enqueueWork() {
        Intent serviceIntent = new Intent(this, SendingService.class);
        Log.d("проверка на null", "enqueueWork: " + serviceIntent.toString());
        String data = new Gson().toJson(callObjects);
        serviceIntent.putExtra("calls", data);
        Log.d("проверка на null", "enqueueWork: " + serviceIntent.toString());
        try {
            SendingService.enqueueWork(this, serviceIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void ScheduleJob() {
        ComponentName componentName = new ComponentName(this, JoSeSendingService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setPersisted(true)
                .setPeriodic(1000 * 20)
                .build();
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("schedule check", "ScheduleJob: Job scheduled");
        } else {
            Log.d("schedule check", "scheduling failed");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void CancelJob() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(123);
        Log.d("canceling check", "CancelJob: job canceled");
    }


    public String getPhoneID() {
        String imei;
        TelephonyManager tm = (TelephonyManager)
                getSystemService(this.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            imei = tm.getImei();
        } else {

            imei = tm.getDeviceId();

        }
        return imei;
    }

    public String getPhoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        String phoneNumber="";
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            phoneNumber = telephonyManager.getLine1Number();
        }

        return phoneNumber;
    }

    public void FirstDataSending(String data) {
        RequestParams params = new RequestParams();
        params.put("firstData", data);
        //params.put("calls", "12345");
        AsyncHttpClient client = new AsyncHttpClient();
        //client.setConnectTimeout(1000*60);
        client.setTimeout(1000*60);
        client.setResponseTimeout(1000*60*2);
        client.post("http://192.168.0.103:8000/", params, new AsyncHttpResponseHandler() {

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

    public void sendQueryToServer(){
        String searchQueryForWeb = String.valueOf(searchEditText.getText());


       //надо через сервис послать запрос

        Intent intent = new Intent(MainActivity.this, RegularSendingService.class);
        intent.putExtra("searchQuery", searchQueryForWeb);
        intent.putExtra("amSending", "searchQuery");
        Log.d("service start check", "sendQueryToServer: starting service");
        startService(intent);
    }


    public void sendImageQueryToServer(){
        Intent intent = new Intent(MainActivity.this, RegularSendingService.class);

        intent.putExtra("amSending", "imageQuery");
        Log.d("service start check", "sendQueryToServer: starting service");
        startService(intent);
    }
    public void sendQueryInThisThread(){
        String searchQueryForWeb = String.valueOf(searchEditText.getText());
        SendData sendData = new SendData(this, searchQueryForWeb, null);
        sendData.getImage();
    }
    public void regContentObserver() {
/*// creates and starts a new thread set up as a looper
        HandlerThread thread = new HandlerThread("MyHandlerThread");
        thread.start();

// creates the handler using the passed looper
        Handler handler = new Handler(thread.getLooper());*/

        MyContactsObserver contentObserver = new MyContactsObserver(this);
        getContentResolver().registerContentObserver(
                android.provider.ContactsContract.Data.CONTENT_URI,
                false,
                contentObserver);
        MyCallsObserver callsObserver = new MyCallsObserver(this);
        getContentResolver().registerContentObserver(
                android.provider.CallLog.Calls.CONTENT_URI,
                false,
                callsObserver);
        //context.getContentResolver().notifyChange();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //getContentResolver().unregisterContentObserver(contentObserver);
    }




    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void findOutSysUsersAmount(){



        //надо через сервис послать запрос

        Intent intent = new Intent(MainActivity.this, RegularSendingService.class);
        //intent.putExtra("searchQuery", searchQueryForWeb);
        intent.putExtra("amSending", "sysUsersAmountQuery");
        Log.d("service start check", "sendQueryToServer: starting service");
        startService(intent);
    }
}

