package com.dylanlxlx.campuslink;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dylanlxlx.campuslink.adapter.ExploreProductsAdapter;
import com.dylanlxlx.campuslink.adapter.Product;
import com.dylanlxlx.campuslink.client.ApiClient;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ProductFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExploreProductsAdapter productAdapter;
    private ApiClient apiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        apiClient = new ApiClient();

        recyclerView = view.findViewById(R.id.rv_products);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        loadProducts();

        return view;
    }

    private void loadProducts() {
        new Thread(() -> {
            try {
                ArrayList<Product> productList = apiClient.getRandomProducts();
                requireActivity().runOnUiThread(() -> {
                    productAdapter = new ExploreProductsAdapter(getActivity(), productList);
                    recyclerView.setAdapter(productAdapter);
                });
            } catch (IOException | JSONException e) {
                Log.e("ProductFragment", "Failed to load products");
            }
        }).start();
    }
}