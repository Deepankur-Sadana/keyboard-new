package com.vingeapp.android.models;

/**
 * Created by divisha on 3/19/17.
 */


public class InformationViewModel {

    public String id;
    public String name;
    public String phonenumber;
    public String email;

    public String getName() {return name;}

    public InformationViewModel() {
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {return email;
    }
}


