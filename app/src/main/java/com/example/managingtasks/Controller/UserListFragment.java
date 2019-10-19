package com.example.managingtasks.Controller;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.managingtasks.Model.User;
import com.example.managingtasks.R;
import com.example.managingtasks.Repository.Repository;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment {


    public static final String ARGS_USER_ID_ADMIN = "ARGS_USER_ID_ADMIN";

    private RecyclerView mRecyclerView;
    private List<User> userList;
    private UserAdapter mUserAdapter;

    public static UserListFragment newInstance() {

        Bundle args = new Bundle();
        UserListFragment fragment = new UserListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public UserListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userList = Repository.getInstance(getContext()).getAllUsers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView_users_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUserAdapter = new UserAdapter(userList);
        mRecyclerView.setAdapter(mUserAdapter);

        return view;

    }

    private class userHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewUsername;
        private TextView mTextViewNumOfTasks;
        private TextView mTextViewRegisterTime;
        private ImageButton mImageButtonDelete;
        private User mUser;

        public userHolder(@NonNull View itemView) {
            super(itemView);

            findView(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = AllTasksOfEachUser.newIntent(getActivity(), mUser.getUser_uuid());
                    startActivity(intent);
                }
            });

            mImageButtonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Are you sure you want to delete this user?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Repository.getInstance(getActivity()).deleteUser(mUser);
                                    mUserAdapter.setUserListAdapter(Repository.getInstance(getContext()).getAllUsers());
                                    mUserAdapter.notifyDataSetChanged();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });

        }

        public void bind(User user) {
            mUser = user;
            mTextViewUsername.setText(user.getUsername());
            mTextViewNumOfTasks.setText(Repository.getInstance(getContext()).getUserAllTask(user.getUser_uuid()).size() + "");
            mTextViewRegisterTime.setText(user.getRegisterTime());
        }

        private void findView(@NonNull View itemView) {
            mTextViewNumOfTasks = itemView.findViewById(R.id.edit_text_num_of_tasks);
            mTextViewRegisterTime = itemView.findViewById(R.id.edit_text_register_time);
            mTextViewUsername = itemView.findViewById(R.id.edit_text_username);
            mImageButtonDelete = itemView.findViewById(R.id.image_button_delete);
        }

    }

    private class UserAdapter extends RecyclerView.Adapter<userHolder> {

        List<User> mUserListAdapter;

        public void setUserListAdapter(List<User> userList) {
            mUserListAdapter = userList;
        }


        public UserAdapter(List<User> userList) {
            mUserListAdapter = userList;
        }


        @NonNull
        @Override
        public userHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_user_item, parent, false);
            return new userHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull userHolder holder, int position) {
            holder.bind(mUserListAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return mUserListAdapter.size();
        }
    }


}
