package com.example.managingtasks.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.managingtasks.R;

public class UsersListActivity extends AppCompatActivity {

    public static Intent newIntent(FragmentActivity context) {
        Intent intent = new Intent(context, UsersListActivity.class);
    //    intent.putExtra(EXTRA_USER, userId);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

      FragmentManager fragmentManager = getSupportFragmentManager();
      Fragment fragment = fragmentManager.findFragmentById(R.id.container_user_list_fragment);
        if (fragment == null)
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container_user_list_fragment, UserListFragment.newInstance(/*getIntent().getStringExtra(EXTRA_USER)*/))
                    .commit();
    }
}
