package com.example.irec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private String dbName;
    private String thisAppsTable;
    private ImageView myImageView;
    private Button takePhotoBtn;
    private Bitmap adjustedBmp;
    private String file_path;
    private EditText myNumberEdTxt;
    private EditText fName;
    private EditText lName;
    private CheckBox checkBox;
    private RelativeLayout ICanRelativeLayoutContainer;
    private ImageButton btnAddAbility;
    private LayoutInflater vi;
    private View v;
    private ViewGroup insertPoint;
    private int addAbilityViewIndex;
    private ArrayList<View[]> allLinesViews;
    private View[] oneLineViews;
    private ArrayList<ArrayList<String>> allLinesData;
    private ArrayList<String> oneLineData;
    private Bundle b;
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    private Uri imageUri;
    final String[] NECESSARY_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS,Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS};
    private TextView myBirthDateView;
    private RadioGroup sexRGroup;
    private RadioButton sexRButton;
    private Button sendFirstDataBtn;
    private ImageView profileImV;
    private ImageView searchImV;
    private ImageView listsImV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        dbName = "IREC_DB";
        db = openOrCreateDatabase(dbName, this.MODE_PRIVATE,
                null);
        thisAppsTable = "iRecData";
        myImageView = findViewById(R.id.myImageView);
        takePhotoBtn = findViewById(R.id.takePhotoBtn);
        myNumberEdTxt = findViewById(R.id.edTxtMyNumber);
        fName = findViewById(R.id.edTxtMyFName);
        lName = findViewById(R.id.edTxtMyLName);
        checkBox = findViewById(R.id.checkICan);
        ICanRelativeLayoutContainer = findViewById(R.id.ICanLayoutContainer);
        btnAddAbility = findViewById(R.id.addAbilityBtn);
        addAbilityViewIndex=0;
        insertPoint = (ViewGroup) findViewById(R.id.abilitiesLinearLayout);
        allLinesViews = new ArrayList<>();
        oneLineViews = new View[2];
        allLinesData = new ArrayList();
        oneLineData = new ArrayList();
        b = new Bundle();
        myBirthDateView = findViewById(R.id.myBirthDate);
        sendFirstDataBtn = findViewById(R.id.sendFirstDataBtn);
        profileImV = findViewById(R.id.profileImV);
        searchImV = findViewById(R.id.searchImV);
        listsImV = findViewById(R.id.listsImV);
        Calendar dateAndTime=Calendar.getInstance();

        WorkWithDbClass dbClass = new WorkWithDbClass(this);
        dbClass.makeTables();
        //dbClass.deleteMyTables();

        regContentObserver();


        searchImV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        listsImV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MyPerfsActivity.class);
                startActivity(intent);
            }
        });

        if(isFirstStartOfApp()){
            sendFirstDataBtn.setText("Отправить");
        }
        else
        {
            sendFirstDataBtn.setText("Сохранить");
        }

        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePhoto();
            }
        });


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ICanRelativeLayoutContainer.setVisibility(View.VISIBLE);

                }
            }
        });

        btnAddAbility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateFormItem();
            }
        });



        sendFirstDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String myNumber = String.valueOf(myNumberEdTxt.getText());
                //String imei = getPhoneID();
                String fNameStr = String.valueOf(fName.getText());
                String lNameStr = String.valueOf(lName.getText());
                String birthDateStr = String.valueOf(myBirthDateView.getText());
                int selectedId = sexRGroup.getCheckedRadioButtonId();
                sexRButton = (RadioButton) findViewById(selectedId);
                String sex = String.valueOf(sexRButton.getText());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                adjustedBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Bundle b = new Bundle();
                b.putByteArray("myImage",byteArray);
                b.putString("number", myNumber);
                //b.putString("imei", imei);
                b.putString("fName", fNameStr);
                b.putString("lName", lNameStr);
                b.putString("birthDate", birthDateStr);
                b.putString("sex", sex);
                // get selected radio button from radioGroup

                for (View[] oneline: allLinesViews){


                    Spinner spinner = (Spinner) oneline[0];
                    EditText doThatEdtxt = (EditText) oneline[1];
                    String spinnersSelectedId = String.valueOf( spinner.getSelectedItemId());
                    String doThatStr  = String.valueOf(doThatEdtxt.getText());

                    oneLineData.add(spinnersSelectedId);
                    oneLineData.add(doThatStr);
                    allLinesData.add(oneLineData);


                }

                b.putParcelable("abilities", (Parcelable) allLinesData);
                Intent intent = new Intent(GetMyFirstDataActivity.this, MainActivity.class);
                intent.putExtras(b);
                startActivity(intent);*/

                String mode = "";
                WorkWithDbClass dbClass = new WorkWithDbClass(ProfileActivity.this);
                if(dbClass.isFirstStartOfApp()){
                    mode = "first";
                }
                else {
                    mode = "editions";
                }
                getProfileAndSendIntentToServiceToSendData(mode);
                //надо отразить в базе что мы отправили первые данные
                // Создайте новую строку со значениями для вставки.
//                ContentValues newValues = new ContentValues();
//                // Задайте значения для каждой строки.
//                newValues.put("haveSentFirstData", true);
//                //[ ... Повторите для каждого столбца ... ]
//                // Вставьте строку в вашу базу данных.
//                db.insert(thisAppsTable, null, newValues);
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);


            }
        });

        class AbilitiesList implements Parcelable {

            ArrayList<Integer> arrayList;
            ArrayList<ArrayList<Integer>> arrayLists;

            public AbilitiesList() {
                arrayLists = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    ArrayList<Integer> temp = new ArrayList<>();
                    for (int j = 0; j < 10; j++) {
                        temp.add(j);
                    }
                    arrayLists.add(temp);
                }

                arrayList = new ArrayList<>();
                for (int i = 100; i < 104; i++) {
                    arrayList.add(i);
                }
            }

            protected AbilitiesList(Parcel in) {
                arrayList = new ArrayList<>();
                in.readList(arrayList, Integer.class.getClassLoader());
                arrayLists = new ArrayList<>();
                in.readList(arrayLists, ArrayList.class.getClassLoader());
            }

            public final Creator<AbilitiesList> CREATOR = new Creator<AbilitiesList>() {
                @Override
                public AbilitiesList createFromParcel(Parcel in) {
                    return new AbilitiesList(in);
                }

                @Override
                public AbilitiesList[] newArray(int size) {
                    return new AbilitiesList[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeList(arrayList);
                dest.writeList(arrayLists);
            }

            @Override
            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < arrayList.size(); i++) {
                    stringBuilder.append(arrayList.get(i) + " ");
                }
                for (int i = 0; i < arrayLists.size(); i++) {
                    for (int j = 0; j < arrayLists.get(i).size(); j++) {
                        stringBuilder.append(arrayLists.get(i).get(j) + " ");
                    }
                }
                return stringBuilder.toString();
            }
        }

        myBirthDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                             public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String myBidthDateStr = dayOfMonth + " | "+(monthOfYear+1)+" | "+year;
                                myBirthDateView.setText(myBidthDateStr);
                                myBirthDateView.setTextColor(Color.parseColor("#69f189"));
                            }
                        },
                       1990,
                        11,
                        10)
                        .show();
            }
        });
        ActivityCompat.requestPermissions(
                this,
                NECESSARY_PERMISSIONS, 123);

        Log.d("tableExistsCheck", "onCreate: "+(isFirstStartOfApp()?"false":"true"));

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
    public void getMode(){
        if(isFirstStartOfApp()){

            db.execSQL(" CREATE TABLE "+ thisAppsTable + "("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "haveSentFirstData BOOLEAN NOT NULL, " +
                    "contactId TEXT NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "number TEXT NOT NULL);"
            );

        }
    }

    public void CreateFormItem(){


      /*  vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.i_can_item, null);

        // fill in any details dynamically here
        TextView textView =  v.findViewById(R.id.labelForSell);
        textView.setText("Я могу ");
        Spinner dropdown = v.findViewById(R.id.spinnerICan);

        //create a list of items for the spinner.
        String[] items = new String[]{"продать", "сделать", "выполнить работы", "найти"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

// insert into main view

        insertPoint.addView(v, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //get the spinner from the xml.


        //addAbilityViewIndex++;
*/

        LinearLayout abilityLinearLayout = new LinearLayout(this);
        abilityLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout priceLinearLayout = new LinearLayout(this);
        priceLinearLayout.setOrientation(LinearLayout.HORIZONTAL);


        //TextView iCan = new TextView(this);
        //iCan.setText("Я могу");
        Spinner spinner = new Spinner(this);
        String[] items = new String[]{"... продать", "... сделать", "... выполнить", "... найти"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        oneLineViews[0]=spinner;
        EditText doThatEdTxt = new EditText(this);
        doThatEdTxt.setText("торт");
//        String[] goods = new String[]{"наушники", "шины", "кроссовки", "запчасти"};
//        int randGood = Random.int;
        //linearLayout.addView(iCan, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        abilityLinearLayout.addView(spinner, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        abilityLinearLayout.addView(doThatEdTxt, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView forLabelTxtV = new TextView(this);
        forLabelTxtV.setText("за");
        EditText priceFromEdTxt = new EditText(this);
        priceFromEdTxt.setHint("5000тг");

        TextView hyphenTxtV = new TextView(this);
        hyphenTxtV.setText(" - ");

        EditText priceToEdTxt = new EditText(this);
        priceToEdTxt.setHint("15000тг");

        priceLinearLayout.addView(forLabelTxtV, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        priceLinearLayout.addView(priceFromEdTxt, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        priceLinearLayout.addView(hyphenTxtV, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        priceLinearLayout.addView(priceToEdTxt, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        insertPoint.addView(abilityLinearLayout, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        insertPoint.addView(priceLinearLayout, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        oneLineViews[1]=doThatEdTxt;
        allLinesViews.add(oneLineViews);


    }

    //.getSelectedItem().toString(); чтобы достать данные спиннера


    public void TakePhoto(){
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.Images.Media.TITLE, "my_image.jpg");
//        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "for_irecProfile");
//        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fname_" +
//                String.valueOf(System.currentTimeMillis()) + ".jpg"));
//        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

                //use imageUri here to access the image

                Bundle extras = data.getExtras();

                //Log.e("URI", imageUri.toString());

                Bitmap bmp = (Bitmap) extras.get("data");
                Log.d("takePhoto", "onActivityResult: "+bmp.toString());

                // here you will get the image as bitmap
                //myImageView.setImageURI(imageUri);
                Log.d("bmp sizes", "onActivityResult: "+bmp.getHeight()+"   "+bmp.getWidth());
                if(bmp.getWidth()>100)
                {
                    int x = (bmp.getWidth()-100)/2;
                    int y = (bmp.getHeight()-100)/2;
                    adjustedBmp = Bitmap.createBitmap(bmp, x, y, 100, 100);
                    myImageView.setImageBitmap(adjustedBmp);
                }
                else {
                    myImageView.setImageBitmap(bmp);
                }

                file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/photoFolder";
                File dir = new File(file_path);
                if(!dir.exists())
                    dir.mkdirs();
                File file = new File(dir, "myImage.jpg");
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                try {
                    fOut.flush();
                    fOut.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                Log.d("sizes", "onActivityResult: "+height+"  "+width);
                myImageView.setMaxHeight(height);
                myImageView.setMaxWidth(width);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public boolean isFirstStartOfApp(){

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + thisAppsTable + "'", null);

        return cursor.getCount()==0;
    }

    public void getProfileAndSendIntentToServiceToSendData(String mode) {
        GetProfile getProfile = new GetProfile(ProfileActivity.this, adjustedBmp ,allLinesViews);
        String profileData = getProfile.collectData();
        byte[] photoByteArray = getProfile.getPhotoByteArray();


        //String dataToSend = new Gson().toJson(firstDataToSend);
        if(mode == "first"){
            Class<FirstSendingService> intentsDestination = FirstSendingService.class;
        }
        else if(mode == "editions")
        {
            Class<RegularSendingService> intentsDestination = RegularSendingService.class;
        }
        Intent intent = new Intent(ProfileActivity.this, FirstSendingService.class);
        intent.putExtra("profileData", profileData);
        intent.putExtra("photo", photoByteArray);
        intent.putExtra("amSending", "profileEdition");
        startService(intent);
    }



}