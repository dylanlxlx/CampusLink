package com.dylanlxlx.campuslink.string;

public class DefaultString {
    private String default_avatar;

    private String url;

    private String defaule_name;

    private String default_phone;

    private String default_remarks;


    public DefaultString() {
        this.default_avatar = "https://x-web-tlias.oss-cn-beijing.aliyuncs.com/defaultAvatar.png";
        this.url = "http://8.130.145.46:8081";
        this.defaule_name = "闹大别肘";
        this.default_phone = "12345678901";
        this.default_remarks = "What can I sya? Man Ba OUT!";
    }

    public String getDefaultAvatar() {
        return this.default_avatar;
    }

    public String getUrl() {
        return this.url;
    }

    public String getDefaultName() {
        return this.defaule_name;
    }

    public String getDefaultPhone() {
        return this.default_phone;
    }

    public String getDefaultRemarks() {
        return this.default_remarks;
    }

}
