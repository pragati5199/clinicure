package com.example.clinicure.user_profile;

public class User_details {

    public  String fname,lname,address,age,contact1,contact2,gender;
    public User_details(){

    }
    public User_details(String fname, String lname, String address, String age, String contact1, String contact2, String gender){
        this.fname  = fname;
        this.lname = lname;
        this.address = address;
        this.age = age;
        this.contact1 = contact1;
        this.contact2 = contact2;
        this.gender = gender;

    }
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return age;
    }

    public void setDob(String age) { this.age = age; }

    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) { this.contact1 = contact1; }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) { this.contact2 = contact2; }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) { this.gender = gender; }

}
