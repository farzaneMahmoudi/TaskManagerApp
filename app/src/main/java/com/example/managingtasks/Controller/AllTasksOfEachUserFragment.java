package com.example.managingtasks.Controller;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.managingtasks.Model.Task;
import com.example.managingtasks.R;
import com.example.managingtasks.Repository.Repository;

import java.util.List;

import static com.example.managingtasks.Controller.TabFragment.REQUEST_CODE_DETAIL_TASK_FRAGMENT;
import static com.example.managingtasks.Controller.TabFragment.TAG_DETAIL_TASK_FRAGMENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllTasksOfEachUserFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private taskAdapter mTaskAdapter;
    private List<Task> tasksList;

    public static final String ARG_USER_ID_ALL_TASKS_USER = "arg_user_id_all_tasks_user";

    public static AllTasksOfEachUserFragment newInstance(String userID) {

        Bundle args = new Bundle();
        args.putString(ARG_USER_ID_ALL_TASKS_USER, userID);

        AllTasksOfEachUserFragment fragment = new AllTasksOfEachUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AllTasksOfEachUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tasksList = Repository.getInstance(getContext()).getUserAllTask(getArguments().getString(ARG_USER_ID_ALL_TASKS_USER));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_tasks_of_each_user, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView_all_tasks_of_each_user);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskAdapter = new taskAdapter(tasksList);
        mRecyclerView.setAdapter(mTaskAdapter);

        return view;
    }

    private class taskHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private TextView mTextViewFirstAlphabetOfTitle;
        private Task mTask;
        private ImageButton mButtonShare;

        public taskHolder(@NonNull View itemView) {
            super(itemView);
            findItemViewById(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskDetailDialogFragment taskDetailDialogFragment = TaskDetailDialogFragment.newInstance(mTask.getUser_uuid(), mTask.getTaskId().toString());
                    taskDetailDialogFragment.setTargetFragment(AllTasksOfEachUserFragment.this, REQUEST_CODE_DETAIL_TASK_FRAGMENT);
                    taskDetailDialogFragment.show(getFragmentManager(), TAG_DETAIL_TASK_FRAGMENT);
                }
            });

        }

        private void findItemViewById(@NonNull View itemView) {
            mTextViewTitle = itemView.findViewById(R.id.text_view_name_item);
            mTextViewDate = itemView.findViewById(R.id.text_view_state_item);
            mTextViewFirstAlphabetOfTitle = itemView.findViewById(R.id.text_view_first_alphabet_title);
            mButtonShare = itemView.findViewById(R.id.button_share);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

        public void bind(Task task, int position) {
            mTask = tasksList.get(position);
            mTextViewTitle.setText(task.getTitle());
            mTextViewDate.setText(mTask.getSimpleDate() + " - " + mTask.getSimpleTime());
            mTextViewFirstAlphabetOfTitle.setText(task.getTitle().charAt(0) + "");

        }
    }

    private class taskAdapter extends RecyclerView.Adapter<taskHolder> {

        private List<Task> mTasksList;


        public void setTasksList(List<Task> tasksList) {
            mTasksList = tasksList;
        }

        public taskAdapter(List<Task> taskList) {
            mTasksList = taskList;
        }

        @NonNull
        @Override
        public taskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_task_item, parent, false);
            return new taskHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(@NonNull taskHolder holder, int position) {
            holder.bind(mTasksList.get(position), position);
        }

        @Override
        public int getItemCount() {
            if (tasksList == null)
                return 0;
            else
                return mTasksList.size();
        }

    }
    public void notifyDataSetChanged() {

        tasksList = Repository.getInstance(getContext()).getUserAllTask(getArguments().getString(ARG_USER_ID_ALL_TASKS_USER));
        mTaskAdapter.setTasksList(tasksList);
        mTaskAdapter.notifyDataSetChanged();

    }

}
