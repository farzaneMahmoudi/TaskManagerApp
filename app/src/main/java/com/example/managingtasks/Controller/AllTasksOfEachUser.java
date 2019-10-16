package com.example.managingtasks.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.managingtasks.R;

import static com.example.managingtasks.Controller.TaskManager.EXTRA_USER;

public class AllTasksOfEachUser extends AppCompatActivity {

    public static Intent newIntent(FragmentActivity context, String userId) {
        Intent intent = new Intent(context, AllTasksOfEachUser.class);
        intent.putExtra(EXTRA_USER, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks_of_each_user);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container_all_task_of_each_user_fragment);
        if (fragment == null)
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container_all_task_of_each_user_fragment,
                            AllTasksOfEachUserFragment.newInstance(getIntent().getStringExtra(EXTRA_USER)))
                    .commit();
    }
}
