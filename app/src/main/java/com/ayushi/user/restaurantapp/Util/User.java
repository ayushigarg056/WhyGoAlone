package com.ayushi.user.restaurantapp.Util;

/**
 * Created by user on 05-Mar-18.
 */

public class User {

    String user;
    String email;
    String password;
    String age;
    String sex;
    String phonenumber;
    String city;

    public User(String user, String email, String password, String age, String sex, String phonenumber, String city) {
        this.user = user;
        this.email = email;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.phonenumber = phonenumber;
        this.city = city;
    }

    public String getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getCity() {
        return city;
    }
}
