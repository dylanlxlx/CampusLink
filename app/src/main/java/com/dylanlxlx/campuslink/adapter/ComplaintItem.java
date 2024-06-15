package com.dylanlxlx.campuslink.adapter;

public class ComplaintItem {
    private int id;
    private String type;
    private int infoId;
    private String infoName;
    private int complainId;
    private String complainName;
    private String state;
    private String reason;
    private Product product;

    // 构造方法
    public ComplaintItem(int id, String type, int infoId, String infoName, int complainId, String complainName,
                         String state, String reason, Product product) {
        this.id = id;
        this.type = type;
        this.infoId = infoId;
        this.infoName = infoName;
        this.complainId = complainId;
        this.complainName = complainName;
        this.state = state;
        this.reason = reason;
        this.product = product;
    }

    // Getter 方法
    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getInfoId() {
        return infoId;
    }

    public String getInfoName() {
        return infoName;
    }

    public int getComplainId() {
        return complainId;
    }

    public String getComplainName() {
        return complainName;
    }

    public String getState() {
        return state;
    }

    public String getReason() {
        return reason;
    }

    public Product getProducts() {
        return product;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setInfoId(int infoId) {
        this.infoId = infoId;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public void setComplainId(int complainId) {
        this.complainId = complainId;
    }

    public void setComplainName(String complainName) {
        this.complainName = complainName;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}