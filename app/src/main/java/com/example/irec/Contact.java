package com.example.irec;

public class Contact {
    private String id;
    private String name;
    private String number;
    private String time;
    private String mode;

    public Contact(String id, String name, String number, String mode, String time) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.mode = "initial";
        this.time = time;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Contact(String id, String name, String number, String mode) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.mode = mode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean equals(Contact anotherContact){
        boolean sameId = id==anotherContact.getId();
        boolean sameName = name==anotherContact.getName();
        boolean sameNumber = number==anotherContact.getNumber();
        if (sameId&&sameName&&sameNumber){
            return true;
        }
        else{
            return false;
        }
    }
}
