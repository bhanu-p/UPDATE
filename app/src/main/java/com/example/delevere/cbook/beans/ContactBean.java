package com.example.delevere.cbook.beans;

public class ContactBean {
    private String name;
    private String phoneNo;

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }

    public void setPhone(String phoneNo){
        this.phoneNo=phoneNo;
    }

    public String getPhone(){
        return phoneNo;
    }
}
