package com.dylanlxlx.campuslink.data;

import android.util.Log;

import com.dylanlxlx.campuslink.string.DefaultString;

public class User {
    private int id;
    private String username;
    private String name;
    private String phone;
    private double money;
    private int age;
    private String avatar;
    private String remarks;
    private int role;
    private String createTime;
    private String updateTime;

    private DefaultString defaultString;

    // Constructor, getters and setters

    public User(int id, String username, String name, String phone, double money, int age, String avatar, String remarks, int role, String createTime, String updateTime) {
        defaultString = new DefaultString();
        this.id = id;
        this.username = username;
        this.name = name == null ? defaultString.getDefaultName() : name;
        this.phone = phone == null ? defaultString.getDefaultPhone() : phone;
        this.money = money;
        this.age = age;
        this.avatar = avatar == null ? defaultString.getDefaultAvatar() : avatar;
        this.remarks = remarks == null ? defaultString.getDefaultRemarks() : remarks;
        this.role = role;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public double getMoney() {
        return money;
    }

    public int getAge() {
        return age;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getRemarks() {
        return remarks;
    }

    public int getRole() {
        return role;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }
}

