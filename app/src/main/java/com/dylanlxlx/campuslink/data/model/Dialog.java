package com.dylanlxlx.campuslink.data.model;

public class Dialog {
    private String avatar;
    private String name;
    private String content;
    private String time;

    private int id;

    private Boolean isReminder;

    public Dialog(String avatar, String name, String content, String time, int id, Boolean isReminder) {
        this.avatar = avatar;
        this.name = name;
        this.content = content;
        this.time = time;
        this.id = id;
        this.isReminder = isReminder;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public Boolean getIsReminder() {
        return isReminder;
    }
}
