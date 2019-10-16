package com.example.managingtasks.Controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managingtasks.Model.User;
import com.example.managingtasks.R;
import com.example.managingtasks.Repository.Repository;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Button mButtonLogin;
    private Button mButtonSignUp;
    private EditText mEditTextUserName;
    private EditText mEditTextPassword;
    private TextView mTextViewAdmin;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        findView(view);
        initListener();

        String text = "login as admin";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (mEditTextUserName.getText().toString().matches("") || mEditTextPassword.getText().toString().matches(""))
                    Toast.makeText(getActivity(), R.string.TOAST_COMPLETE_ALL_FIELDS, Toast.LENGTH_SHORT).show();
                else if (!mEditTextUserName.getText().toString().equals("admin") || !mEditTextPassword.getText().toString().equals("123123")) {
                    Toast.makeText(getActivity(), R.string.TOAST_NOT_MATCH, Toast.LENGTH_SHORT).show();
                }
               else if (mEditTextUserName.getText().toString().equals("admin") && mEditTextPassword.getText().toString().equals("123123")) {
                    //User user = Repository.getInstance(getActivity()).getUser("admin");
                    Intent intent = UsersListActivity.newIntent(getActivity());
                    startActivity(intent);
                }

            }

        };
        ss.setSpan(clickableSpan, 9, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextViewAdmin.setText(ss);
        mTextViewAdmin.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }

    private void initListener() {
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextUserName.getText().toString().matches("") || mEditTextPassword.getText().toString().matches(""))
                    Toast.makeText(getActivity(), R.string.TOAST_COMPLETE_ALL_FIELDS, Toast.LENGTH_SHORT).show();
                else if (Repository.getInstance(getActivity()).isExistUser(mEditTextUserName.getText().toString()) == false)
                    Toast.makeText(getActivity(), R.string.TOAST_USER_NOT_EXIST, Toast.LENGTH_SHORT).show();
                else if (Repository.getInstance(getActivity()).isExistUser(mEditTextUserName.getText().toString()) &&
                        Repository.getInstance(getActivity()).validateUser(mEditTextUserName.getText().toString(), mEditTextPassword.getText().toString())) {

                    User user = Repository.getInstance(getActivity()).getUser(mEditTextUserName.getText().toString());
                    Intent intent = TaskManager.newIntent(getActivity(), user.getUser_uuid());
                    startActivity(intent);
                } else if (Repository.getInstance(getActivity()).isExistUser(mEditTextUserName.getText().toString()) == true &&
                        Repository.getInstance(getActivity()).validateUser(mEditTextUserName.getText().toString(), mEditTextPassword.getText().toString()) == false)
                    Toast.makeText(getActivity(), R.string.TOAST_NOT_MATCH, Toast.LENGTH_SHORT).show();
            }


        });
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container_fragment, SignUpFragment.newInstance())
                        .commit();
            }
        });
    }

    private void findView(View view) {
        mButtonLogin = view.findViewById(R.id.button_login);
        mButtonSignUp = view.findViewById(R.id.button_sign_up);
        mEditTextUserName = view.findViewById(R.id.username_edit_text);
        mEditTextPassword = view.findViewById(R.id.password_edit_text);
        mTextViewAdmin = view.findViewById(R.id.textview_login_as_admin);
    }

}
