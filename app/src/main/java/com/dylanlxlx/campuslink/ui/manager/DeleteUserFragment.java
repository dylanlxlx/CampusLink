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

public class DeleteUserFragment extends Fragment {

    private EditText userIdEditText;
    private Button deleteButton;

    private OnUserDeleteListener mListener;

    public interface OnUserDeleteListener {
        void onUserDeleteSubmit(String userId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserDeleteListener) {
            mListener = (OnUserDeleteListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserDeleteListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_user, container, false);

        userIdEditText = view.findViewById(R.id.userIdEditText);
        deleteButton = view.findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = userIdEditText.getText().toString().trim();

                if (mListener != null) {
                    mListener.onUserDeleteSubmit(userId);
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
