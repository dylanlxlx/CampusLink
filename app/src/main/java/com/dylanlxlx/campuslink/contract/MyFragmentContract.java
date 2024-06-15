package com.dylanlxlx.campuslink.contract;

import com.dylanlxlx.campuslink.adapter.Order;
import com.dylanlxlx.campuslink.adapter.Product;

import java.util.ArrayList;

public interface MyFragmentContract {
    interface View {
        void showError(String errorMessage);
        void showSuccess(String successMessage);
        void displayProducts(ArrayList<Product> products);
    }

    interface Presenter {
        void loadUserGoods();
        void loadSoldOrders();
        void loadBoughtOrders();
    }
}
