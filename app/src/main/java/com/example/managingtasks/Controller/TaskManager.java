package com.example.managingtasks.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;


import com.example.managingtasks.R;
import com.google.android.material.tabs.TabLayout;

public class TaskManager extends AppCompatActivity {
    public static final String EXTRA_USER = "com.example.taskmanager.controller.EXTRA_USER";

    private String userID;
    private ViewPager viewPager;
    private  PagerAdapter adapter;

    public static Intent newIntent(FragmentActivity context, String userId) {
        Intent intent = new Intent(context, TaskManager.class);
        intent.putExtra(EXTRA_USER, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tak_manager);

        viewPager = findViewById(R.id.pager);
        userID = getIntent().getStringExtra(EXTRA_USER);

        adapter = new PagerAdapter(getSupportFragmentManager(), userID);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


}
