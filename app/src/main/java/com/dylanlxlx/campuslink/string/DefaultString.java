package com.dylanlxlx.campuslink.string;

public class DefaultString {
    private String default_avatar;

    private String url;

    public DefaultString() {
        this.default_avatar = "https://x-web-tlias.oss-cn-beijing.aliyuncs.com/defaultAvatar.png";
        this.url = "http://47.121.131.98:8081";
    }

    public String getDefaultAvatar() {
        return this.default_avatar;
    }

    public String getUrl() {
        return this.url;
    }
}
