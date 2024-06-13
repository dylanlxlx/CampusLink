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

public class UserInputFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText roleEditText;
    private Button submitButton;

    private OnUserInputListener mListener;

    public interface OnUserInputListener {
        void onUserInputSubmit(String username, String password, String name, int role);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserInputListener) {
            mListener = (OnUserInputListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserInputListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_input, container, false);

        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        nameEditText = view.findViewById(R.id.nameEditText);
        roleEditText = view.findViewById(R.id.roleEditText);
        submitButton = view.findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String name = nameEditText.getText().toString().trim();

                // 输入验证
                if (!isValidUsername(username)) {
                    usernameEditText.setError("Username must be between 5 and 18 characters");
                    return;
                }
                if (!isValidPassword(password)) {
                    passwordEditText.setError("Password must be between 5 and 18 characters");
                    return;
                }
                if (name.isEmpty()) {
                    nameEditText.setError("Name cannot be empty");
                    return;
                }
                int role = Integer.parseInt(roleEditText.getText().toString().trim());
                if (role < 1 || role > 2) {
                    roleEditText.setError("Role must be 1 or 2");
                    return;
                }
                if (mListener != null) {
                    mListener.onUserInputSubmit(username, password, name, role);
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

    private boolean isValidUsername(String username) {
        return username.length() >= 5 && username.length() <= 18;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 5 && password.length() <= 18;
    }
}
