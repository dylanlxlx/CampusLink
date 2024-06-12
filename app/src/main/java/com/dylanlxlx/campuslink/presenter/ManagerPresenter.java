package com.dylanlxlx.campuslink.presenter;

import com.dylanlxlx.campuslink.client.ApiClient;
import com.dylanlxlx.campuslink.contract.ManagerContract;

public class ManagerPresenter implements ManagerContract.Presenter {

    private final ManagerContract.View view;
    private final ApiClient apiClient;

    public ManagerPresenter(ManagerContract.View view) {
        this.view = view;
        this.apiClient = new ApiClient();
    }


}
