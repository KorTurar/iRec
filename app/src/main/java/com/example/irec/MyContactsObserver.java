package com.example.irec;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class MyContactsObserver extends ContentObserver {
    private Context context;
    public MyContactsObserver(Context context) {
        super(null);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange,
                         Collection<Uri> uris,
                         int flags) {
        super.onChange(selfChange, uris, flags);
        Log.d("onChange Check", "onChange: ContentIbserver onChange ran");
        ArrayList<Contact> contactsEditions = new ArrayList<>();
        for (Uri iterable_element : uris) {
            String[] projection = new String[]{
                    ContactsContract.Data.CONTACT_ID,
                    ContactsContract.Data.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.Data.CONTACT_LAST_UPDATED_TIMESTAMP
            };
            String[] projectionDeleted = new String[]{
                    ContactsContract.Data.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            };
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
            long now = new Date().getTime();
            String where = ContactsContract.Data.CONTACT_LAST_UPDATED_TIMESTAMP+">?";
            String whereDeleted = ContactsContract.RawContacts.DELETED+"=? OR "+ ContactsContract.RawContacts.DIRTY + "=?";
            String tenSecAgo = String.valueOf(now -(1000*10));
            String[] whereArg = {tenSecAgo};
            String[] whereArgDeleted = {"1", "1"};
            Cursor cursor;
            //CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=?";
            if(mode!="deleted")
            {

                cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                        projection,null,null,ContactsContract.Data.CONTACT_LAST_UPDATED_TIMESTAMP + " DESC limit 1;", null);
            }
            else {
                cursor = context.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI,
                        projectionDeleted,whereDeleted,whereArgDeleted,null, null);
            }
            cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    projection,where,whereArg,null, null);
            if(cursor!=null && cursor.moveToFirst() )
            {
                for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ) {
                    String idStr = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Data.CONTACT_ID));
                    String nameStr = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME));
                    // Extract the phone number.
                    String numberStr = cursor.getString(cursor.getColumnIndexOrThrow(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    long timestamp = now;

                    Date dateNow = new Date(timestamp);
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    String savingTime = formatter.format(dateNow);
                    Contact contact = new Contact(idStr, nameStr, numberStr, mode, savingTime);

                    Log.d("contactChange", "onChange: "+contact.getId()+"  "+contact.getName()+"  "+contact.getNumber()+"   "+contact.getMode());

                    contactsEditions.add(contact);

                }

            }
            cursor.close();
        }
        String dataToSend = new Gson().toJson(contactsEditions);
        Intent intent = new Intent(context, RegularSendingService.class);
        intent.putExtra("amSending", "contactsEdition");
        intent.putExtra("mode", "regular");
        intent.putExtra("contactsData", dataToSend);
        context.startService(intent);/**/




    }

    @Override
    public void onChange(boolean selfChange,
                         Uri uri,
                         int flags)
    {
        super.onChange(selfChange, uri, flags);
        String[] projection = new String[]{
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Data.CONTACT_LAST_UPDATED_TIMESTAMP
        };
        String[] projectionDeleted = new String[]{
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        Log.d("uri check", "onChange: "+uri);
        Log.d("onChange Check", "onChange: ContentIbserver onChange ran");


        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
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
                long now = new Date().getTime();
                String where = ContactsContract.Data.CONTACT_LAST_UPDATED_TIMESTAMP+">?";
                String whereDeleted = ContactsContract.RawContacts.DELETED+"=? OR "+ ContactsContract.RawContacts.DIRTY + "=?";
                String tenSecAgo = String.valueOf(now -(1000*10));
                String[] whereArg = {tenSecAgo};
                String[] whereArgDeleted = {"1", "1"};
                //CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=?";
                //
                Cursor cursor;
                if(mode!="deleted")
                {
                    cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                            projection,null,null,ContactsContract.Data.CONTACT_LAST_UPDATED_TIMESTAMP + " DESC limit 1;", null);
                }
                else {
                    cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                            projectionDeleted,whereDeleted,whereArgDeleted,null, null);
                }

                Log.d("onChange cursor check", "onChange: "+cursor);
                if(cursor!=null && cursor.moveToFirst() )
                {
                    String idStr="";
                    //cursor.moveToFirst();
                    if(mode!="deleted") {
                        idStr = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                    }

                    String nameStr = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME));
                    // Extract the phone number.
                    String numberStr = cursor.getString(cursor.getColumnIndexOrThrow(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    long timestamp = now;


                    Date dateNow = new Date(timestamp);
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    String savingTime = formatter.format(dateNow);
                    Contact contact = new Contact(idStr, nameStr, numberStr, mode, savingTime);



                    Log.d("contactChange", "onChange: "+contact.getId()+"  "+contact.getName()+"  "+contact.getNumber()+"   "+contact.getMode()+"  "+savingTime);


                    String dataToSend = new Gson().toJson(contact);

                    Intent intent = new Intent(context, RegularSendingService.class);
                    intent.putExtra("amSending", "contactsEdition");
                    intent.putExtra("mode", "regular");
                    intent.putExtra("contactsData", dataToSend);
                    context.startService(intent);/**/

                }
                cursor.close();

                Looper.loop();
            }}).start();




    }

    //noti
}