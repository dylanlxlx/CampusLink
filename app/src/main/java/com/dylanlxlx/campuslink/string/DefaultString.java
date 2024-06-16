package com.dylanlxlx.campuslink.string;

public class DefaultString {
    private String default_avatar;

    private String url;

    public DefaultString() {
        this.default_avatar = "https://x-web-tlias.oss-cn-beijing.aliyuncs.com/defaultAvatar.png";
        this.url = "http://8.130.145.46";
    }

    public String getDefaultAvatar() {
        return this.default_avatar;
    }

    public String getUrl() {
        return this.url;
    }
}
