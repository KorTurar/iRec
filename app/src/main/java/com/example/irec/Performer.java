package com.example.irec;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Performer {

    private String fName;
    private String lName;
    private String imageName;
    private Bitmap hisImage;
    private String phoneNumber;
    private String lowestPrice;
    private float highestPrice;
    private int amountOfSuggestion;
    private String presentsAndCommunicates;
    private String amountOfIncomingCallsPerWeek;
    private boolean openForOffers;
    private ArrayList<String> abilities;

    public Performer(String fName, String lName, String phoneNumber, String imageName,  String lowestPrice, ArrayList<String> abilities, String presentsAndCommunicates, String amountOfIncomingCallsPerWeek, boolean openToOffers){
        this.fName = fName;
        this.lName = lName;
        this.imageName = "uploads/profiles/"+imageName;
        this.phoneNumber = phoneNumber;
        //this.hisImage = hisImage;
        this.lowestPrice = lowestPrice;
        this.abilities = abilities;
        this.amountOfSuggestion = this.abilities.size();
        this.presentsAndCommunicates = presentsAndCommunicates;
        this.amountOfIncomingCallsPerWeek = amountOfIncomingCallsPerWeek;
        this.openForOffers = openToOffers;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public Bitmap getHisImage() {
        return hisImage;
    }

    public void setHisImage(Bitmap hisImage) {
        this.hisImage = hisImage;
    }

    public String getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(String lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public float getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(float highestPrice) {
        this.highestPrice = highestPrice;
    }

    public int getAmountOfSuggestion() {
        return amountOfSuggestion;
    }

    public void setAmountOfSuggestion(int amountOfSuggestion) {
        this.amountOfSuggestion = amountOfSuggestion;
    }

    public String getPresentsAndCommunicates() {
        return presentsAndCommunicates;
    }

    public void setPresentsAndCommunicates(String presentsAndCommunicates) {
        this.presentsAndCommunicates = presentsAndCommunicates;
    }

    public String getAmountOfIncomingCallsPerWeek() {
        return amountOfIncomingCallsPerWeek;
    }

    public void setAmountOfIncomingCallsPerWeek(String amountOfIncomingCallsPerWeek) {
        this.amountOfIncomingCallsPerWeek = amountOfIncomingCallsPerWeek;
    }

    public boolean isOpenForOffers() {
        return openForOffers;
    }

    public void setOpenForOffers(boolean openForOffers) {
        this.openForOffers = openForOffers;
    }

    public ArrayList<String> getAbilities() {
        return abilities;
    }

    public void setAbilities(ArrayList<String> abilities) {
        this.abilities = abilities;
    }


}
