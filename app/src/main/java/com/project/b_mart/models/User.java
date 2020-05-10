package com.project.b_mart.models;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String uid;
    private String profileImageStr;
    private String name;
    private String phone;
    private String email;
    private String address;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileImageStr() {
        return profileImageStr;
    }

    public void setProfileImageStr(String profileImageStr) {
        this.profileImageStr = profileImageStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static List<User> parseUserList(DataSnapshot dataSnapshot) {
        List<User> items = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            items.add(snapshot.getValue(User.class));
        }
        return items;
    }
}
