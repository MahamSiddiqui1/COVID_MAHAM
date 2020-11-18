package com.akdndhrc.covid_module.CustomClass;

public class Customer {
    private String firstName = "";
    private int id = 0;
    private int profilePic = -1;

    public Customer(String firstName, int id, int profilePic) {
        this.firstName = firstName;
        this.id = id;
        this.profilePic = profilePic;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public String toString() {
        return this.firstName + " ";
    }

    public  void ABC(){

    }
}