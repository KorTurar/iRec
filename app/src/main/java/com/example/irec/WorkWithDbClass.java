package com.example.irec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.CallLog;
import android.util.Log;

import java.util.ArrayList;

public class WorkWithDbClass {
    //получаем переменную db,
    //узнаём есть ди таблицы,
    //создаём нужные таблицы,
    //сохраняем контакты если нет интерента для немедленной отправки,
    //сохраняем инфу что первые данные переданы,
    //вытаскиваем сохранённые контакты если есть,
    //сохраняем дату и время последнего звонка отправленного на сервер
    //вытаскиваем дату и время последнего звонка
    //сохраняем списки спецов полученные от сервера,
    //сохраняем тех спецов к которым обращались

    private Context context;
    private SQLiteDatabase db;
    private String dbName = "IREC_DB";
    private String haveSentFirstDataTable = "haveSentFirstData";
    private String contactsTable = "contacts";
    private String lastCallTable = "lastCall";
    private String membersFromServerListTable = "membersFromServerList";
    private String calledMembersTable = "calledMembers";
    private String myMembersTable = "myMembers";
    private String myProfileTable = "myProfile";
    private ArrayList<Contact> contactsObjectsFromMyDb;
    public WorkWithDbClass(Context context){
        this.context = context;
        this.db = context.openOrCreateDatabase(dbName, context.MODE_PRIVATE,
                null);
    }

    public boolean isFirstStartOfApp(){

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + haveSentFirstDataTable + "'", null);

        return cursor.getCount()==0;
    }

    public boolean haveSentFirstData (){
        Cursor cursor = db.query(haveSentFirstDataTable, new String[]{"haveSentFirstData"},null,null,null, null, null);
        if (cursor!=null) {
            //cursor.getCount();
            cursor.moveToFirst();
            Boolean haveSentFirstData = cursor.getInt(cursor.getColumnIndex("haveSentFirstData"))==1;
            return haveSentFirstData;
        }
        return false;
    }

    public boolean noOtherTables(){
        Cursor contactsTableExistsCursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + contactsTable + "'", null);
        Cursor membersFromServerListTableExistsCursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + membersFromServerListTable + "'", null);
        Cursor myMembersTableExistsCursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + myMembersTable + "'", null);


        return contactsTableExistsCursor.getCount()==0||membersFromServerListTableExistsCursor.getCount()==0||myMembersTableExistsCursor.getCount()==0;
    }

    public void makeTables(){
        String hQuery = " CREATE TABLE IF NOT EXISTS "+ haveSentFirstDataTable + " ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "haveSentFirstData INTEGER); ";
        /*String cQuery = "CREATE TABLE "+ contactsTable + " ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "contactId INTEGER(4),"+
                "name TEXT(50),"+
                "phoneNumber TEXT(20));";
        String fromSLQuery = "CREATE TABLE "+ membersFromServerListTable + " ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "listId INTEGER(7),"+
                "name TEXT(50),"+
                "phoneNumber_1 TEXT(20)," +
                "phoneNumber_2 TEXT(20)," +
                "phoneNumber_3 TEXT(20)," +
                "phoneNumber_4 TEXT(20)," +
                "phoneNumber_5 TEXT(20)," +
                "ability_1 TEXT(100),"+
                "ability_2 TEXT(100),"+
                "ability_3 TEXT(100),"+
                "ability_4 TEXT(100),"+
                "ability_5 TEXT(100),"+
                "ability_6 TEXT(100),"+
                "ability_7 TEXT(100));";*/

        String calledMQuery = "CREATE TABLE IF NOT EXISTS "+ calledMembersTable + " ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "photoUri TEXT(30),"+
                "fName Text(20),"+
                "lName Text(20),"+
                "phoneNumber_1 TEXT(20),"+
                "ability_1 TEXT(100),"+
                "ability_2 TEXT(100),"+
                "ability_3 TEXT(100),"+
                "ability_4 TEXT(100),"+
                "ability_5 TEXT(100),"+
                "ability_6 TEXT(100),"+
                "ability_7 TEXT(100));";


        String myMQuery = "CREATE TABLE IF NOT EXISTS "+ myMembersTable + " ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "photoUri TEXT(30),"+
                "fName Text(20),"+
                "lName Text(20),"+
                "phoneNumber_1 TEXT(20),"+
                "ability_1 TEXT(100),"+
                "ability_2 TEXT(100),"+
                "ability_3 TEXT(100),"+
                "ability_4 TEXT(100),"+
                "ability_5 TEXT(100),"+
                "ability_6 TEXT(100),"+
                "ability_7 TEXT(100));";
        String lCallQuery =  "CREATE TABLE IF NOT EXISTS "+ lastCallTable + " ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "lastCallDateAndTime Text(25)); ";
        String myProfileQuery = "CREATE TABLE IF NOT EXISTS "+ myProfileTable + " ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "photoUri TEXT(30),"+
                "fName Text(20),"+
                "lName Text(20),"+
                "phoneNumber_1 TEXT(20),"+
                "ability_1 TEXT(100),"+
                "ability_2 TEXT(100),"+
                "ability_3 TEXT(100),"+
                "ability_4 TEXT(100),"+
                "ability_5 TEXT(100),"+
                "ability_6 TEXT(100),"+
                "ability_7 TEXT(100));";
        db.execSQL(hQuery);
        db.execSQL(calledMQuery);
        db.execSQL(myMQuery);
        db.execSQL(lCallQuery);
        db.execSQL(myProfileQuery);
    }

    public void saveContactsFromMyPhone(ArrayList<Contact> contactsObjects){
        for (Contact contact: contactsObjects){
            // Создайте новую строку со значениями для вставки.
            ContentValues newValues = new ContentValues();
            // Задайте значения для каждой строки.
            newValues.put("contactId", contact.getId());
            newValues.put("name", contact.getName());
            newValues.put("phoneNumber", contact.getNumber());
            //[ ... Повторите для каждого столбца ... ]
            // Вставьте строку в вашу базу данных.
            db.insert(contactsTable, null, newValues);
        }
    }

    public void makeRecordThatFirstDataHasBeenSent(){
        ContentValues newValues = new ContentValues();
        // Задайте значения для каждой строки.
        newValues.put("haveSentFirstData", 1);
        //[ ... Повторите для каждого столбца ... ]
        // Вставьте строку в вашу базу данных.
        db.insert(haveSentFirstDataTable, null, newValues);
    }

    public ArrayList<Contact> getContactsFromMyDb(){
        Cursor cursor = db.query(contactsTable, new String[]{"contactId", "name", "phoneNumber"},null,null,null, null, null);
        if (cursor!=null){
            //cursor.getCount();
            for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ) {
                String contactIdStr = cursor.getString(cursor.getColumnIndex("contactId"));
                String nameStr = cursor.getString(cursor.getColumnIndex("name"));
                String phoneNumberStr = cursor.getString(cursor.getColumnIndex("phoneNumber"));
                Contact contact = new Contact(contactIdStr, nameStr, phoneNumberStr, "initial");
                contactsObjectsFromMyDb.add(contact);


            }
            return contactsObjectsFromMyDb;
        }
        return null;
    }

    /*public void saveLastCallTime (Call call){

        ContentValues newValues = new ContentValues();
        newValues.put("lastCallDateAndTime", call.getDate());
        if (.getCount()==0){
            db.insert(lastCallTable, null, newValues);
        }
        else{
            String where = KEY_ID + "=" + rowId;
            db.update(lastCallTable, newValues, where, null);
        }

    }

    public String getLastCallTime (){
        Cursor cursor = db.query(lastCallTable,"lastCallDateAndTime");
    }

    public void savePerfsListSheet(){

    }

    public ArrayList<ArrayList<Performer>> getPerfsListSheets(){

    }



    public ArrayList<Performer> getPerfs(){

    }*/
    public void saveDialedPerf(Performer performer){
        ContentValues newValues = new ContentValues();
        // Задайте значения для каждой строки.
        //newValues.put("photopUri", performer.get());
        newValues.put("fName", performer.getfName());
        newValues.put("lName", performer.getlName());
        newValues.put("phoneNumber_1", performer.getPhoneNumber());
        if(performer.getAbilities().size()<=7) {
            for (int i=0;i< performer.getAbilities().size(); i++) {
                newValues.put("ability_"+ (i + 1),performer.getAbilities().get(i));
            }
        }
        //[ ... Повторите для каждого столбца ... ]
        // Вставьте строку в вашу базу данных.
        db.insert(calledMembersTable, null, newValues);
    }


    public void deleteDialedPerf(String fName, String lName, String phoneNumber){
         String where = "fName=? AND lName=? AND phoneNumber_1=?";
         String[] whereArgs = new String[]{fName, lName, phoneNumber};
         db.delete(calledMembersTable, where,whereArgs);
    }
    public ArrayList<Performer> getDialedPerfs(){
        Cursor cursor = db.query(calledMembersTable, new String[] {"fName", "lName", "phoneNumber_1", "ability_1", "ability_2", "ability_3", "ability_4", "ability_5", "ability_6", "ability_7"},null,null,null, null, null);
        Log.d("gettingDataFromDb check", "inside getDialedPerfs");
        ArrayList<Performer> calledPerfsFromDb = new ArrayList<>();

        if (cursor!=null){
            //cursor.getCount();
            ArrayList<String> abilities=new ArrayList<>();
            for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ) {
                String fNameStr = cursor.getString(cursor.getColumnIndex("fName"));
                String lNameStr = cursor.getString(cursor.getColumnIndex("lName"));
                String phoneNumberStr = cursor.getString(cursor.getColumnIndex("phoneNumber_1"));
                for (int i=0;i<7;i++){
                    String ability = cursor.getString(cursor.getColumnIndex("ability_"+(i+1)));
                    if(ability!=null){abilities.add(ability);}
                }

                Performer performer = new Performer(fNameStr, lNameStr, phoneNumberStr, null, null, abilities,null, null, false);
                calledPerfsFromDb.add(performer);
                Log.d("dbRead check", performer.toString());

            }

        }
        return calledPerfsFromDb;
    }

    public void addChosenFromDialedPerfToSaved(Performer performer){
        ContentValues newValues = new ContentValues();
        // Задайте значения для каждой строки.
        //newValues.put("photopUri", performer.get());
        newValues.put("fName", performer.getfName());
        newValues.put("lName", performer.getlName());
        newValues.put("phoneNumber_1", performer.getPhoneNumber());
        for (int i=0;i<performer.getAbilities().size();i++){
            newValues.put("ability_"+(i+1), performer.getAbilities().get(i));
        }

        db.insert(myMembersTable, null, newValues);
    }

    public ArrayList<Performer> getSavedPerfs(){
        Cursor cursor = db.query(myMembersTable, null, null, null, null,null,null);
        ArrayList<Performer> savedPerfsFromDb = new ArrayList<>();
        if (cursor!=null){
            //cursor.getCount();
            ArrayList<String> abilities=new ArrayList<>();
            for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ) {
                String fNameStr = cursor.getString(cursor.getColumnIndex("fName"));
                String lNameStr = cursor.getString(cursor.getColumnIndex("lName"));
                String phoneNumberStr = cursor.getString(cursor.getColumnIndex("phoneNumber_1"));
                for (int i=0;i<7;i++){
                    String ability = cursor.getString(cursor.getColumnIndex("ability_"+(i+1)));
                    if(ability!=null){abilities.add(ability);}
                }
                Performer performer = new Performer(fNameStr, lNameStr, phoneNumberStr, null, null, abilities,null, null, false);
                savedPerfsFromDb.add(performer);


            }
            return savedPerfsFromDb;
        }
        return null;
    }

    public void deleteSavedPerf(String fName, String lName, String phoneNumber){
        String where = "fName=? AND lName=? AND phoneNumber_1=?";
        String[] whereArgs = new String[]{fName, lName, phoneNumber};
        db.delete(myMembersTable, where, whereArgs);
    }
    public void deleteMyTables () {
        String[] tables = new String[]{haveSentFirstDataTable, calledMembersTable, myMembersTable,   lastCallTable};
        for (String table : tables) {
            String dropQuery = "DROP TABLE IF EXISTS " + table;
            db.execSQL(dropQuery);
        }
    }


}
