package com.example.irec;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
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
import java.util.Set;

public class GetContacts {
    private static Context context;
    private ArrayList<TempNames> contactsIds;
    private ArrayList<Contact> contactsObjects;
    private String mode;
    private ArrayList<Contact> contactsEditions;
    // Create a projection that limits the result Cursor
    // to the required columns.
    private String[] projectionForContactNames = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
    };
    public GetContacts (Context context, String mode){
        this.context = context;
        this.mode = mode;
    }


    public void GetTempNames () {
        // Get a Cursor over the Contacts Provider.
        Cursor cursor =
                context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
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
    }


    public String getContacts() {
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
                    context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
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



            return new Gson().toJson(contactsObjects);


    }


/*
    public String getContactsEditions(){

            WorkWithDbClass myDbManager = new WorkWithDbClass(context);
            ArrayList<Contact> ContactsFromMyDb = myDbManager.getContactsFromMyDb();
            int contactsFromProviderCount = contactsObjects.size();
            int contactsFromDbCount = ContactsFromMyDb.size();
            int contactsAreEqualCasesCount = 0;
            for(Contact contactFromProvider: contactsObjects){
                for (Contact contactFromDb: ContactsFromMyDb){
                    if(contactFromDb.equals(contactFromProvider)){
                        contactsAreEqualCasesCount++;


                        ContactsEdition contactEdition = new ContactsEdition();
                        contactsEditions.add(contactEdition);
                    }
                    else{
                        contactFromDb
                    }
                    if (contactsAreEqualCasesCount>contactsFromDbCount)
                }
                if contactsAreEqualCasesCount
            }
            return contactsEditions.size()!=0?(new Gson().toJson(contactsObjects)):"None";

    }
*/

    public static void regContentObserver() {
/*// creates and starts a new thread set up as a looper
        HandlerThread thread = new HandlerThread("MyHandlerThread");
        thread.start();

// creates the handler using the passed looper
        Handler handler = new Handler(thread.getLooper());*/

        MyContentObserver contentObserver = new MyContentObserver();
        context.getContentResolver().registerContentObserver(
                ContactsContract.Data.CONTENT_URI,
                true,
                contentObserver);
        //context.getContentResolver().notifyChange();
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
            Log.d("onChange Check uriS", "onChange: ContentIbserver onChange ran");
            ArrayList<Contact> contactsEditions = new ArrayList<>();
            for (Uri iterable_element : uris) {
                String[] projection = new String[]{
                        ContactsContract.Data.CONTACT_ID,
                        ContactsContract.Data.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };
                try {

                    Set<String> parNames= iterable_element.getQueryParameterNames();
                    for (String parName: parNames){
                        Log.d("test", "run: "+parName);
                    }
                    Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                            projection,null,null,null, null);
                    if(cursor!=null)
                    {
                        for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ) {
                            String idStr = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Data.CONTACT_ID));
                            String nameStr = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME));
                            // Extract the phone number.
                            String numberStr = cursor.getString(cursor.getColumnIndexOrThrow(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
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
                            Contact contact = new Contact(idStr, nameStr, numberStr, mode);
                            Log.d("contactChange", "onChange: "+contact.getId()+"  "+contact.getName()+"  "+contact.getNumber()+"   "+contact.getMode());

                            contactsEditions.add(contact);

                        }

                    }
                } catch (IllegalArgumentException e){
                    Log.d("logging exception", "onChange: "+e.getMessage());
                }


            }
            String dataToSend = new Gson().toJson(contactsEditions);
            Intent intent = new Intent(context, RegularSendingService.class);
            intent.putExtra("amSending", "contactsEdition");
            intent.putExtra("contactsData", dataToSend);
            context.startService(intent);



            //Log.d(this. class.getSimpleName(), "A change has happened");
        }

        @Override
        public void onChange(boolean selfChange,
                             Uri uri,
                             int flags)
        {
            super.onChange(selfChange, uri, flags);

            Log.d("uri check", "onChange: "+uri.toString());
            Log.d("onChange Check 1 uri", "onChange: ContentIbserver onChange ran");
            String query= uri.getQuery();
            Log.d("test", "run: "+query);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();

                    try {
                        String[] projection = new String[]{
                                ContactsContract.Data.CONTACT_ID,
                                ContactsContract.Data.DISPLAY_NAME,
                                ContactsContract.CommonDataKinds.Phone.NUMBER,
                                ContactsContract.Data.CONTACT_LAST_UPDATED_TIMESTAMP
                        };
                        long now = new Date().getTime();
                        /*Set<String> parNames= uri.getQueryParameterNames();
                        for (String parName: parNames){
                            Log.d("test", "run: "+parName);
                        }*/
                        String where = ContactsContract.Data.CONTACT_LAST_UPDATED_TIMESTAMP+">?";
                        String tenSecAgo = String.valueOf(now -(1000*10));
                        String[] whereArg = {tenSecAgo};
                       //CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=? OR " + CallLog.Calls.TYPE + "=?";
                        //
                        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                                projection,where,whereArg,null, null);
                        Log.d("onChange cursor check", "onChange: "+cursor);
                        if(cursor!=null)
                        {
                            cursor.moveToFirst();
                            String idStr = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                            String nameStr = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME));
                            // Extract the phone number.
                            String numberStr = cursor.getString(cursor.getColumnIndexOrThrow(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Data.CONTACT_LAST_UPDATED_TIMESTAMP));
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
                            Contact contact = new Contact(idStr, nameStr, numberStr, mode);


                            Date dateNow = new Date(timestamp);
                            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                            String formattedDate = formatter.format(dateNow);
                            Log.d("contactChange", "onChange: "+contact.getId()+"  "+contact.getName()+"  "+contact.getNumber()+"   "+contact.getMode()+"  "+formattedDate);
                            /*String dataToSend = new Gson().toJson(contact);

                            Intent intent = new Intent(context, RegularSendingService.class);
                            intent.putExtra("amSending", "contactsEdition");
                            intent.putExtra("contactsData", dataToSend);
                            context.startService(intent);*/

                        }
                    }catch (IllegalArgumentException e){
                        Log.d("logging exception", "onChange: "+e.getMessage());
                    }



                    Looper.loop();
                }}).start();




        }

        //noti
    }

    /**/
    public ArrayList<Contact> getContactsArraylist(){

        return contactsObjects;
    }



}
