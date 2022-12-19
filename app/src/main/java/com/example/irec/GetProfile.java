package com.example.irec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class GetProfile {
    private Context context;
    private JSONObject profileData;
    private Bitmap adjustedBmp;
    private byte[] byteArray;
    private ArrayList<View[]> allLinesViews;
    private JSONObject oneLineData;
    private JSONObject allLinesData;

    public GetProfile(Context context, Bitmap adjustedBmp, ArrayList<View[]> allLinesViews){
        this.context = context;
        this.adjustedBmp = adjustedBmp;
        this.allLinesViews = allLinesViews;
    }



    public String collectData() {
    EditText myNumberEdTxt = (EditText) ((Activity) context).findViewById(R.id.edTxtMyNumber);
    EditText fName = (EditText) ((Activity) context).findViewById(R.id.edTxtMyFName);
    EditText lName = (EditText) ((Activity) context).findViewById(R.id.edTxtMyLName);
    TextView myBirthDateView = (TextView) ((Activity) context).findViewById(R.id.myBirthDate);
    RadioGroup sexRGroup = (RadioGroup) ((Activity) context).findViewById(R.id.SexRadioGroup);


    String myNumber = String.valueOf(myNumberEdTxt.getText());
    String imei = getPhoneID();
    String fNameStr = String.valueOf(fName.getText());
    String lNameStr = String.valueOf(lName.getText());
    String birthDateStr = String.valueOf(myBirthDateView.getText());
    int selectedId = sexRGroup.getCheckedRadioButtonId();
    RadioButton sexRButton = (RadioButton) ((Activity) context).findViewById(selectedId);
    String sex = String.valueOf(sexRButton.getText());

    oneLineData = new JSONObject();
    allLinesData = new JSONObject();

    profileData = new JSONObject();
        try {
            //profileData.put("myImage",byteArray);
            profileData.put("number", myNumber);
            profileData.put("imei", imei);
            profileData.put("fName", fNameStr);
            profileData.put("lName", lNameStr);
            profileData.put("birthDate", birthDateStr);
            profileData.put("sex", sex);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    int count = 0;
    for (View[] oneline: allLinesViews){


        Spinner spinner = (Spinner) oneline[0];
        EditText doThatEdtxt = (EditText) oneline[1];
        String spinnersSelectedId = String.valueOf( spinner.getSelectedItemId());
        String doThatStr  = String.valueOf(doThatEdtxt.getText());

        try {
            oneLineData.put("spinner", spinnersSelectedId);
            oneLineData.put("spinnerCont", doThatStr);
            allLinesData.put("ability "+count, oneLineData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        count++;

        }

        try {
            profileData.put("abilities", allLinesData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().toJson(profileData);
    }


    public String getPhoneID() {
        String imei;
        TelephonyManager tm = (TelephonyManager)
                context.getSystemService(context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            imei = tm.getImei();
        } else {

            imei = tm.getDeviceId();

        }
        return imei;
    }

    public byte[] getPhotoByteArray() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        adjustedBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byteArray = stream.toByteArray();
        return byteArray;
    }
}

