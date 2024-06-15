// MyFragmentPresenter.java
package com.dylanlxlx.campuslink.presenter;

import android.os.Handler;
import android.os.Looper;

import com.dylanlxlx.campuslink.adapter.Order;
import com.dylanlxlx.campuslink.adapter.Product;
import com.dylanlxlx.campuslink.client.ApiClient;
import com.dylanlxlx.campuslink.contract.MyFragmentContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MyFragmentPresenter implements MyFragmentContract.Presenter {
    private final MyFragmentContract.View view;
    private final ApiClient apiClient;

    public MyFragmentPresenter(MyFragmentContract.View view) {
        this.view = view;
        this.apiClient = new ApiClient();
    }

    @Override
    public void loadUserGoods() {
        new Thread(() -> {
            try {
                JSONObject userSelf = apiClient.getUserSelf();
                int userId = userSelf.getJSONObject("data").getInt("id");

                apiClient.queryUserGoods(userId, new ApiClient.QueryGoodsCallback() {
                    @Override
                    public void onSuccess(JSONArray data) {
                        ArrayList<Product> products = new ArrayList<>();
                        try {
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject productObject = data.getJSONObject(i);
                                Product product = new Product(
                                        productObject.getInt("id"),
                                        productObject.getInt("userId"),
                                        productObject.getString("category"),
                                        productObject.getString("title"),
                                        productObject.getString("description"),
                                        productObject.getString("status"),
                                        productObject.getDouble("price"),
                                        productObject.getInt("num"),
                                        productObject.getInt("sales"),
                                        productObject.optString("image", null),
                                        productObject.getString("createTime"),
                                        productObject.getString("updateTime")
                                );
                                products.add(product);
                            }
                        } catch (JSONException e) {
                            new Handler(Looper.getMainLooper()).post(() -> view.showError(e.getMessage()));
                        }
                        new Handler(Looper.getMainLooper()).post(() -> view.displayProducts(products));
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        new Handler(Looper.getMainLooper()).post(() -> view.showError(errorMessage));
                    }
                });
            } catch (IOException | JSONException e) {
                new Handler(Looper.getMainLooper()).post(() -> view.showError(e.getMessage()));
            }
        }).start();
    }

    @Override
    public void loadSoldOrders() {
        new Thread(() -> {
            apiClient.getSoldOrders(new ApiClient.SoldOrdersCallback() {
                @Override
                public void onSuccess(JSONArray ordersArray) {
                    ArrayList<Order> orders = new ArrayList<>();
                    ArrayList<Product> products = new ArrayList<>();
                    try {
                        for (int i = 0; i < ordersArray.length(); i++) {
                            JSONObject orderObject = ordersArray.getJSONObject(i);
                            JSONObject goodsObject = orderObject.getJSONObject("goods");
                            Product goods = new Product(
                                    goodsObject.getInt("id"),
                                    goodsObject.getInt("userId"),
                                    goodsObject.getString("category"),
                                    goodsObject.getString("title"),
                                    goodsObject.getString("description"),
                                    goodsObject.getString("status"),
                                    goodsObject.getDouble("price"),
                                    goodsObject.getInt("num"),
                                    goodsObject.getInt("sales"),
                                    goodsObject.optString("image", null),
                                    goodsObject.getString("createTime"),
                                    goodsObject.getString("updateTime")
                            );
                            products.add(goods);
                            Order order = new Order(
                                    orderObject.getInt("id"),
                                    orderObject.getInt("goodsId"),
                                    orderObject.getInt("sellId"),
                                    orderObject.getString("sellName"),
                                    orderObject.getInt("buyId"),
                                    orderObject.getString("buyName"),
                                    orderObject.getString("orderId"),
                                    orderObject.getDouble("price"),
                                    orderObject.getString("status"),
                                    orderObject.getString("time"),
                                    orderObject.getString("address"),
                                    goods
                            );
                            orders.add(order);
                        }
                    } catch (JSONException e) {
                        new Handler(Looper.getMainLooper()).post(() -> view.showError(e.getMessage()));
                    }
                    new Handler(Looper.getMainLooper()).post(() -> view.displayProducts(products));
                }

                @Override
                public void onFailure(String errorMessage) {
                    new Handler(Looper.getMainLooper()).post(() -> view.showError(errorMessage));
                }
            });
        }).start();
    }

    @Override
    public void loadBoughtOrders() {
        new Thread(() -> {
            apiClient.getBoughtOrders(new ApiClient.BoughtOrdersCallback() {
                @Override
                public void onSuccess(JSONArray ordersArray) {
                    ArrayList<Product> products = new ArrayList<>();
                    try {
                        for (int i = 0; i < ordersArray.length(); i++) {
                            JSONObject orderObject = ordersArray.getJSONObject(i);
                            JSONObject goodsObject = orderObject.getJSONObject("goods");
                            Product product = new Product(
                                    goodsObject.getInt("id"),
                                    goodsObject.getInt("userId"),
                                    goodsObject.getString("category"),
                                    goodsObject.getString("title"),
                                    goodsObject.getString("description"),
                                    goodsObject.getString("status"),
                                    goodsObject.getDouble("price"),
                                    goodsObject.getInt("num"),
                                    goodsObject.getInt("sales"),
                                    goodsObject.optString("image", null),
                                    goodsObject.getString("createTime"),
                                    goodsObject.getString("updateTime")
                            );
                            products.add(product);
                        }
                    } catch (JSONException e) {
                        new Handler(Looper.getMainLooper()).post(() -> view.showError(e.getMessage()));
                    }
                    new Handler(Looper.getMainLooper()).post(() -> view.displayProducts(products));
                }

                @Override
                public void onFailure(String errorMessage) {
                    new Handler(Looper.getMainLooper()).post(() -> view.showError(errorMessage));
                }
            });
        }).start();
    }
}
