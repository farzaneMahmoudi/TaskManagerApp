package com.example.managingtasks.Controller;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.managingtasks.Model.User;
import com.example.managingtasks.R;
import com.example.managingtasks.Repository.Repository;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private Button mButtonSignUp;
    private EditText mEditTextUserName;
    private EditText mEditTextPassword;


    public static SignUpFragment newInstance() {

        Bundle args = new Bundle();

        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        findView(view);

        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextUserName.getText().toString().matches("") || mEditTextPassword.getText().toString().matches(""))
                    Toast.makeText(getActivity(), R.string.TOAST_COMPLETE_ALL_FIELDS, Toast.LENGTH_SHORT).show();
                else if (Repository.getInstance(getActivity()).isExistUser(mEditTextUserName.getText().toString())) {
                    Toast.makeText(getActivity(), R.string.TOAST_USERNAME_IS_EXIST, Toast.LENGTH_SHORT).show();
                } else {

                    User user = new User(mEditTextUserName.getText().toString(), mEditTextPassword.getText().toString());
                    Repository.getInstance(getActivity()).insertUser(user);
                    Intent intent = TaskManager.newIntent(getActivity(), user.getUser_uuid());
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    private void findView(View view) {

        mButtonSignUp = view.findViewById(R.id.button_sign_up);
        mEditTextUserName = view.findViewById(R.id.username_edit_text);
        mEditTextPassword = view.findViewById(R.id.password_edit_text);
    }
}
