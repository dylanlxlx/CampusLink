package com.dylanlxlx.campuslink;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dylanlxlx.campuslink.adapter.ExploreProductsAdapter;
import com.dylanlxlx.campuslink.adapter.Product;
import com.dylanlxlx.campuslink.adapter.ProductInMyAdpter;
import com.dylanlxlx.campuslink.client.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MyFragment extends Fragment implements View.OnClickListener {
    ApiClient apiClient;

    private LinearLayout mPublished;
    private LinearLayout mSold;
    private LinearLayout mBought;
    private RecyclerView mRvProducts;
    private FloatingActionButton mAddButton;
    private ProductInMyAdpter productAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        apiClient = new ApiClient();
        initView(view);
        initListener();
        mRvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        loadProducts();

        return view;
    }

    private void loadProducts() {
        new Thread(() -> {
            try {
                ArrayList<Product> productList = apiClient.getRandomProducts();
                requireActivity().runOnUiThread(() -> {
                    productAdapter = new ProductInMyAdpter(getActivity(), productList);
                    mRvProducts.setAdapter(productAdapter);
                });
            } catch (IOException | JSONException e) {
                Log.e("ProductFragment", "Failed to load products");
            }
        }).start();
    }

    private void initView(View view) {
        mRvProducts = view.findViewById(R.id.rv_products);
        mPublished = view.findViewById(R.id.published);
        mSold = view.findViewById(R.id.sold);
        mBought = view.findViewById(R.id.bought);
        mRvProducts = view.findViewById(R.id.rv_products);
        mAddButton = view.findViewById(R.id.add_button);
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
        } else if (view == mSold) {
            // Handle clicks for mSold
        } else if (view == mBought) {
            // Handle clicks for mBought
        } else if (view == mAddButton) {
            // Handle clicks for mAddButton
        }
    }
}