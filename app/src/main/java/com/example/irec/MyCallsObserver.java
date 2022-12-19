package com.example.irec;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class MyCallsObserver extends ContentObserver {

    private Context context;
    public MyCallsObserver(Context context) {
        super(null);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange,
                         Collection<Uri> uris,
                         int flags) {
        super.onChange(selfChange, uris, flags);
        Log.d("onChange Check", "onChange: ContentIbserver onChange ran");
        ArrayList<Call> callsEditions = new ArrayList<>();
        for (Uri iterable_element : uris) {
            String[] projection = new String[]{
                    CallLog.Calls.DURATION,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.CACHED_NAME,
                    CallLog.Calls.TYPE,
                    CallLog.Calls.DATE
            };
            long now = new Date().getTime();
            String where = CallLog.Calls.DATE+">?";
            String tenSecAgo = String.valueOf(now -(1000*10));
            String[] whereArg = {tenSecAgo};
            //CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=?";
            //
            Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                    projection,null,null,android.provider.CallLog.Calls.DATE + " DESC limit 1;", null);
            if(cursor!=null && cursor.moveToFirst() )
            {
                for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ) {
                    long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
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

                    Date dateNow = new Date(timestamp);
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    String callDateStr = formatter.format(dateNow);
                    Call call = new Call(callDateStr, numberStr, nameStr, durationStr, typeStr);
                    callsEditions.add(call);
                    Log.d("callChange", "onChange: "+call.getDate()+"  "+call.getName()+"  "+call.getNumber()+"   "+call.getDuration()+"   "+call.getType());





                }
                cursor.close();
            }
        }
        String dataToSend = new Gson().toJson(callsEditions);
        Intent intent = new Intent(context, RegularSendingService.class);
        intent.putExtra("amSending", "newCalls");
        intent.putExtra("mode", "regular");
        intent.putExtra("callsData", dataToSend);
        context.startService(intent); /**/




    }

    @Override
    public void onChange(boolean selfChange,
                         Uri uri,
                         int flags)
    {
        super.onChange(selfChange, uri, flags);

        Log.d("uri check", "onChange: "+uri);
        Log.d("onChange Check", "onChange: ContentIbserver onChange ran");


        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                String[] projection = new String[]{
                        CallLog.Calls.DURATION,
                        CallLog.Calls.NUMBER,
                        CallLog.Calls.CACHED_NAME,
                        CallLog.Calls.TYPE,
                        CallLog.Calls.DATE
                };
                long now = new Date().getTime();
                String where = CallLog.Calls.DATE+">?";
                String tenSecAgo = String.valueOf(now -(1000*10));
                String[] whereArg = {tenSecAgo};

                //CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=?";
                //
                Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                        projection,null,null,android.provider.CallLog.Calls.DATE + " DESC limit 1;", null);
                Log.d("onChange cursor check", "onChange: "+cursor);
                if(cursor!=null && cursor.moveToFirst() )
                {
                    //cursor.moveToFirst();
                    long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
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
                    Date dateNow = new Date(timestamp);
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    String callDateStr = formatter.format(dateNow);
                    Call call = new Call(callDateStr, numberStr, nameStr, durationStr, typeStr);
                    String dataToSend = new Gson().toJson(call);
                    Log.d("callChange", "onChange: "+call.getDate()+"  "+call.getName()+"  "+call.getNumber()+"   "+call.getDuration()+"   "+call.getType());
                    Intent intent = new Intent(context, RegularSendingService.class);
                    intent.putExtra("amSending", "newCalls");
                    intent.putExtra("mode", "regular");
                    intent.putExtra("callsData", dataToSend);
                    context.startService(intent);/**/

                }
                cursor.close();

                Looper.loop();
            }}).start();




    }

}
