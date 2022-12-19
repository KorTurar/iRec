package com.example.irec;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import android.content.ContentResolver;
import android.database.Cursor;
import android.content.Context;

import com.google.gson.Gson;

public class GetCalls {
    //Toast.makeText(this, "poluchilos", Toast.LENGTH_SHORT).show();
    private static Context context;
    private String mode;
    private ArrayList<Call> callObjects  = new ArrayList<>();
    public GetCalls (Context context, String mode){
        this.context = context;
        this.mode = mode;
     }
    //String where = CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=?"; //query for all the calls in database


    public String GetCallsData(){
        String[] projection = {
                CallLog.Calls.DURATION,
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE
        };
        String where;
        String[] whereArgs;
        String whereForHourlyService = "("+CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=?) AND ("+CallLog.Calls.DATE+">?)";//query for last calls, not older than 60 minutes
        String whereForFirstSendService = CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=?";
        long now = new Date().getTime();
        Date dateNow = new Date(now);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String DateNowInYMD = formatter.format(dateNow);
        long timeForLastCalls = now-(1000*60*60*7); //time for calls of last 7 hours
        Date dateLastCalls = new Date(timeForLastCalls);
        //DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String DateLastCallsInYMD = formatter.format(dateLastCalls);
        Log.d("check Date", "GetCalls: "+DateNowInYMD+"  "+DateLastCallsInYMD);

        //String[] whereArgs = {String.valueOf(CallLog.Calls.OUTGOING_TYPE), String.valueOf(CallLog.Calls.INCOMING_TYPE), String.valueOf(CallLog.Calls.MISSED_TYPE)}; //args for all calls
        String[] whereArgsForHourlyService = {String.valueOf(CallLog.Calls.OUTGOING_TYPE), String.valueOf(CallLog.Calls.INCOMING_TYPE), String.valueOf(CallLog.Calls.MISSED_TYPE), String.valueOf(timeForLastCalls)};
        String[] whereArgsForFirstSendService = {String.valueOf(CallLog.Calls.OUTGOING_TYPE), String.valueOf(CallLog.Calls.INCOMING_TYPE), String.valueOf(CallLog.Calls.MISSED_TYPE)};
        // Get a Cursor over the Call Log Calls Provider.
        if(mode=="needAllData"){
            where = CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=?";
            whereArgs = new String[]{String.valueOf(CallLog.Calls.OUTGOING_TYPE), String.valueOf(CallLog.Calls.INCOMING_TYPE), String.valueOf(CallLog.Calls.MISSED_TYPE)};
        }
        else if(mode=="needLastdata"){
            where = "("+CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=?) AND ("+CallLog.Calls.DATE+">?)";//query for last calls, not older than 60 minutes
            whereArgs = new String[] {String.valueOf(CallLog.Calls.OUTGOING_TYPE), String.valueOf(CallLog.Calls.INCOMING_TYPE), String.valueOf(CallLog.Calls.MISSED_TYPE), String.valueOf(timeForLastCalls)};
        }
         Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                projection, whereForFirstSendService, whereArgsForFirstSendService, null);
        // Get the index of the columns.
         int durIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION);
         int numberIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER);
         int nameIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME);
         int callTypeIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE);
         int dateIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.DATE);
        // Initialize the result set.
        String[] result = new String[cursor.getCount()];
    // Iterate over the result Cursor.
        while (cursor.moveToNext()) {
        String durStr = cursor.getString(durIdx);
        String numberStr = cursor.getString(numberIdx);
        String nameStr = cursor.getString(nameIdx)==null?"noname":cursor.getString(nameIdx);
        String DateInMilli = cursor.getString(dateIdx);
        String callType = cursor.getString(callTypeIdx);


        //String x = "1086073200000";
        long foo = Long.parseLong(DateInMilli);

        Date date = new Date(foo);
        //DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        String DateInYMD = formatter.format(date);


        result[cursor.getPosition()] = DateInYMD +": "+ callType + "   " + numberStr + " for " + durStr + "sec" +
                ((null == nameStr) ?
                        "" : " (" + nameStr + ")");
        Call call = new Call(DateInYMD, numberStr, nameStr, durStr, callType);
        callObjects.add(call);
        Log.d("Calls", result[cursor.getPosition()]);
    }
    // Close the Cursor.
        cursor.close();
        return new Gson().toJson(callObjects);
    }


    public static void regContentObserver() {
        /*// creates and starts a new thread set up as a looper
        HandlerThread thread = new HandlerThread("MyHandlerThread");
        thread.start();

// creates the handler using the passed looper
        Handler handler = new Handler(thread.getLooper());*/

        MyContentObserver contentObserver = new MyContentObserver();
        context.getContentResolver().registerContentObserver(
                android.provider.CallLog.Calls.CONTENT_URI,
                true,
                contentObserver);
    }

    private static class MyContentObserver extends ContentObserver {
        public MyContentObserver() {
            super(null);
        }

        @Override
        public void onChange(boolean selfChange,
                             Collection<Uri> uris,
                             int flags) {
            super.onChange(selfChange, uris, flags);
            //Log.d(this. class.getSimpleName(), "A change has happened");
            ArrayList<Call> newCalls = new ArrayList<>();
            for (Uri iterable_element : uris) {
                String[] projection = {
                        CallLog.Calls.DURATION,
                        CallLog.Calls.NUMBER,
                        CallLog.Calls.CACHED_NAME,
                        CallLog.Calls.TYPE,
                        CallLog.Calls.DATE
                };
                try{
                    Cursor cursor = context.getContentResolver().query(iterable_element,
                            projection,null,null,null, null);
                    if(cursor!=null)
                    {
                        for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ) {
                            String dateStr = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
                            String typeStr = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE));
                            // Extract the phone number.
                            String numberStr = cursor.getString(cursor.getColumnIndexOrThrow(
                                    CallLog.Calls.NUMBER));
                            String tempNameStr = cursor.getString(cursor.getColumnIndexOrThrow(
                                    CallLog.Calls.CACHED_NAME));
                            String nameStr = tempNameStr == null?"noname":tempNameStr;
                            String durationStr = cursor.getString(cursor.getColumnIndexOrThrow(
                                    CallLog.Calls.DURATION));


                            String mode;
                            switch (flags) {
                                case ContentResolver.NOTIFY_INSERT:
                                    mode = "inserted";
                                    break;
                                case ContentResolver.NOTIFY_UPDATE:
                                    mode = "updated";
                                    break;
                                case ContentResolver.NOTIFY_DELETE:
                                    mode = "deleted";
                                    break;
                                default:
                                    mode = "None";
                                    break;
                            }
                            Call call = new Call(dateStr, numberStr, nameStr, durationStr, typeStr);
                            Log.d("callChange", "onChange: "+call.getDate()+"  "+call.getName()+"  "+call.getNumber()+"   "+call.getDuration()+"   "+call.getType());

                            newCalls.add(call);

                        }

                    }
                }catch (IllegalArgumentException e){
                    Log.d("logging exception", "onChange: "+e.getMessage());
                }

            }
            String dataToSend = new Gson().toJson(newCalls);

            Intent intent = new Intent(context, RegularSendingService.class);
            intent.putExtra("amSending", "newCalls");
            intent.putExtra("callsData", dataToSend);
            context.startService(intent);

            super.onChange(selfChange);


            //Log.d(this. class.getSimpleName(), "A change has happened");
        }

        @Override
        public void onChange(boolean selfChange,
                             Uri uri,
                             int flags) {
            super.onChange(selfChange, uri, flags);
            String[] projection = {
                    CallLog.Calls.DURATION,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.CACHED_NAME,
                    CallLog.Calls.TYPE,
                    CallLog.Calls.DATE
            };
            //Log.d(this. class.getSimpleName(), "A change has happened");
            Log.d("uri check", "onChange: "+uri);
            try {
                Cursor cursor = context.getContentResolver().query(uri,
                        projection,null,null,null, null);
                if(cursor!=null)
                {
                    cursor.moveToFirst();
                    String dateStr = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
                    String typeStr = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE));
                    // Extract the phone number.
                    String numberStr = cursor.getString(cursor.getColumnIndexOrThrow(
                            CallLog.Calls.NUMBER));
                    String tempNameStr = cursor.getString(cursor.getColumnIndexOrThrow(
                            CallLog.Calls.CACHED_NAME));
                    String nameStr = tempNameStr == null?"noname":tempNameStr;
                    String durationStr = cursor.getString(cursor.getColumnIndexOrThrow(
                            CallLog.Calls.DURATION));
                    String mode;
                    switch (flags) {
                        case ContentResolver.NOTIFY_INSERT:
                            mode = "inserted";
                            break;
                        case ContentResolver.NOTIFY_UPDATE:
                            mode = "updated";
                            break;
                        case ContentResolver.NOTIFY_DELETE:
                            mode = "deleted";
                            break;
                        default:
                            mode = "None";
                            break;
                    }
                    Call call = new Call(dateStr, numberStr, nameStr, durationStr, typeStr);
                    Log.d("callChange", "onChange: "+call.getDate()+"  "+call.getName()+"  "+call.getNumber()+"   "+call.getDuration()+"   "+call.getType());

                    String dataToSend = new Gson().toJson(call);

                    Intent intent = new Intent(context, RegularSendingService.class);
                    intent.putExtra("amSending", "newCalls");
                    intent.putExtra("callsData", dataToSend);
                    context.startService(intent);

                }
            }catch (IllegalArgumentException e){
                Log.d("logging exception", "onChange: "+e.getMessage());
            }

        }
    }
}
