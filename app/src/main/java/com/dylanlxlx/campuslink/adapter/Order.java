package com.dylanlxlx.campuslink.adapter;

public class Order {
    private int id;
    private int goodsId;
    private int sellId;
    private String sellName;
    private int buyId;
    private String buyName;
    private String orderId;
    private double price;
    private String status;
    private String time;
    private String address;
    private Product goods;

    public Order(int id, int goodsId, int sellId, String sellName, int buyId, String buyName,
                 String orderId, double price, String status, String time, String address, Product goods) {
        this.id = id;
        this.goodsId = goodsId;
        this.sellId = sellId;
        this.sellName = sellName;
        this.buyId = buyId;
        this.buyName = buyName;
        this.orderId = orderId;
        this.price = price;
        this.status = status;
        this.time = time;
        this.address = address;
        this.goods = goods;
    }

    public int getId() {
        return id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public int getSellId() {
        return sellId;
    }

    public String getSellName() {
        return sellName;
    }

    public int getBuyId() {
        return buyId;
    }

    public String getBuyName() {
        return buyName;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public String getAddress() {
        return address;
    }

    public Product getGoods() {
        return goods;
    }
}
