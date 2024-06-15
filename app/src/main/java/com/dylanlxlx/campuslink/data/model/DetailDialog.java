package com.dylanlxlx.campuslink.data.model;

public class DetailDialog {
    private int id;
    private String content;
    private String time;

    private Boolean isLeft;

    private String avatar;

    public DetailDialog(int id, String content, String time, Boolean isLeft, String avatar) {
        this.id = id;
        this.content = content;
        this.time = time;
        this.isLeft = isLeft;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public Boolean getIsLeft() {
        return isLeft;
    }

    public String getAvatar() {
        return avatar;
    }

}
