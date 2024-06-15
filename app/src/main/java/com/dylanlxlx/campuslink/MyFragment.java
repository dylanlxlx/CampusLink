package com.dylanlxlx.campuslink;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dylanlxlx.campuslink.adapter.Order;
import com.dylanlxlx.campuslink.adapter.Product;
import com.dylanlxlx.campuslink.adapter.ProductInMyAdpter;
import com.dylanlxlx.campuslink.client.ApiClient;
import com.dylanlxlx.campuslink.contract.MyFragmentContract;
import com.dylanlxlx.campuslink.presenter.MyFragmentPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyFragment extends Fragment implements View.OnClickListener, MyFragmentContract.View {
    ApiClient apiClient;

    private LinearLayout mPublished, mSold, mBought;
    private ImageView mPublishedIcon, mSoldIcon, mBoughtIcon;
    private TextView mPublishedText, mSoldText, mBoughtText;
    private RecyclerView mRvProducts;
    private FloatingActionButton mAddButton;
    private ProductInMyAdpter productAdapter;

    private MyFragmentPresenter presenter;

    int flag = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        apiClient = new ApiClient();
        presenter = new MyFragmentPresenter(this);
        initView(view);
        initListener();
        mRvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        updateButtonColors();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        // 默认显示 mPublished 的商品
        flag = 1;
        updateButtonColors();
        presenter.loadUserGoods();
    }


    private void initView(View view) {
        mRvProducts = view.findViewById(R.id.rv_products);
        mPublished = view.findViewById(R.id.published);
        mSold = view.findViewById(R.id.sold);
        mBought = view.findViewById(R.id.bought);
        mRvProducts = view.findViewById(R.id.rv_products);
        mAddButton = view.findViewById(R.id.add_button);
        mPublished = view.findViewById(R.id.published);
        mPublishedIcon = view.findViewById(R.id.published_icon);
        mPublishedText = view.findViewById(R.id.published_text);
        mSold = view.findViewById(R.id.sold);
        mSoldIcon = view.findViewById(R.id.sold_icon);
        mSoldText = view.findViewById(R.id.sold_text);
        mBought = view.findViewById(R.id.bought);
        mBoughtIcon = view.findViewById(R.id.bought_icon);
        mBoughtText = view.findViewById(R.id.bought_text);
    }

    private void initListener() {
        mPublished.setOnClickListener(this);
        mSold.setOnClickListener(this);
        mBought.setOnClickListener(this);
        mAddButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mPublished) {
            // Handle clicks for mPublished
            flag = 1;
            updateButtonColors();
            presenter.loadUserGoods();
        } else if (view == mSold) {
            // Handle clicks for mSold
            flag = 2;
            updateButtonColors();
            presenter.loadSoldOrders();
        } else if (view == mBought) {
            // Handle clicks for mBought
            flag = 3;
            updateButtonColors();
            presenter.loadBoughtOrders();
        } else if (view == mAddButton) {
            // Handle clicks for mAddButton
            Intent intent = new Intent(getActivity(), PublishActivity.class);
            startActivity(intent);
        }
    }

    private void updateButtonColors() {
        if (flag == 1) {
            mPublishedText.setTextColor(Color.BLACK);
            mPublishedIcon.setColorFilter(Color.BLACK);

            mSoldText.setTextColor(Color.GRAY);
            mSoldIcon.setColorFilter(Color.GRAY);

            mBoughtText.setTextColor(Color.GRAY);
            mBoughtIcon.setColorFilter(Color.GRAY);
        } else if (flag == 2) {
            mPublishedText.setTextColor(Color.GRAY);
            mPublishedIcon.setColorFilter(Color.GRAY);

            mSoldText.setTextColor(Color.BLACK);
            mSoldIcon.setColorFilter(Color.BLACK);

            mBoughtText.setTextColor(Color.GRAY);
            mBoughtIcon.setColorFilter(Color.GRAY);
        } else if (flag == 3) {
            mPublishedText.setTextColor(Color.GRAY);
            mPublishedIcon.setColorFilter(Color.GRAY);

            mSoldText.setTextColor(Color.GRAY);
            mSoldIcon.setColorFilter(Color.GRAY);

            mBoughtText.setTextColor(Color.BLACK);
            mBoughtIcon.setColorFilter(Color.BLACK);
        }
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String successMessage) {
        Toast.makeText(getActivity(), successMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayProducts(ArrayList<Product> products) {
        productAdapter = new ProductInMyAdpter(getActivity(), products);
        mRvProducts.setAdapter(productAdapter);
    }
}