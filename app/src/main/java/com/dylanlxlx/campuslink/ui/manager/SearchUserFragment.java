package com.dylanlxlx.campuslink.ui.manager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.dylanlxlx.campuslink.R;

public class SearchUserFragment extends Fragment {

    private EditText nameEditText;
    private Button submitButton;

    private OnUserSearchListener mListener;

    public interface OnUserSearchListener {
        void onUserSearchSubmit(String name);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserSearchListener) {
            mListener = (OnUserSearchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserInputListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_user, container, false);

        nameEditText = view.findViewById(R.id.searchNameEditText);
        submitButton = view.findViewById(R.id.searchButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();

//                if (name.isEmpty()) {
//                    nameEditText.setError("name cannot be empty");
//                    return;
//                }
                if (mListener != null) {
                    mListener.onUserSearchSubmit(name);
                }
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
