package com.example.managingtasks.Controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.managingtasks.Model.Task;
import com.example.managingtasks.R;
import com.example.managingtasks.Repository.Repository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {

    public static final int REQUEST_CODE_ADDTASK_FRAGMENT = 0;
    public static final String TAG_ADD_TASK_FRAGMENT = "tag_add_task_fragment";
    public static final String ARG_TAB_FRAGMENT_POSITION = "ARG_TAB_FRAGMENT_POSITION";
    public static final String LIST_TASKS = "listTasks";
    public static final String ARG_USERNAME_TAB_FRAGMENT = "ARG_USERNAME_TAB_FRAGMENT";
    public static final String TAG_DETAIL_TASK_FRAGMENT = "Tag_detail_task_fragment";
    public static final int REQUEST_CODE_DETAIL_TASK_FRAGMENT = 1;


    private RecyclerView mRecyclerView;
    private ImageView mImageView;
    private FrameLayout mFrameLayout_tab_fragment;
    private FloatingActionButton mFloatingActionButton;
    private taskAdapter mTaskAdapter;
    private List<Task> mTaskList = new ArrayList<>();

    StateTask stateTask;
    private String userID;
    private int position;

    public TabFragment() {
        // Required empty public constructor
    }

    public static TabFragment newInstance(int position, String userID) {

        Bundle args = new Bundle();
        args.putInt(ARG_TAB_FRAGMENT_POSITION, position);
        args.putString(ARG_USERNAME_TAB_FRAGMENT, userID);

        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStateTask();
        setUser();
        setPositionFragment();
        mTaskList = Repository.getInstance(getActivity().getApplicationContext()).getListTasksPerState(userID, stateTask);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        setHasOptionsMenu(true);
        findById(view);
        updateUI();

        buildRecyclerView(getActivity().getResources().getConfiguration().orientation);
        initListener();
        return view;


    }


    private void initListener() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTaskDialogFragment addTaskFragment = AddTaskDialogFragment.newInstance(position, userID);
                addTaskFragment.setTargetFragment(TabFragment.this, REQUEST_CODE_ADDTASK_FRAGMENT);
                addTaskFragment.show(getFragmentManager(), TAG_ADD_TASK_FRAGMENT);
            }
        });
    }

    private void buildRecyclerView(int i) {
        if (i == 1)
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        else if (i == 2)
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskAdapter = new taskAdapter(mTaskList);
        mRecyclerView.setAdapter(mTaskAdapter);

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



            mButtonShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.task_report_subject));
                    intent.putExtra(Intent.EXTRA_TEXT, getTaskReport(mTask));
                    intent = intent.createChooser(intent, getString(R.string.TITLE_SHARE));
                    startActivity(intent);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TaskDetailDialogFragment taskDetailDialogFragment = TaskDetailDialogFragment.newInstance(userID, mTask.getTaskId().toString());
                    taskDetailDialogFragment.setTargetFragment(TabFragment.this, REQUEST_CODE_DETAIL_TASK_FRAGMENT);
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
            mTask = task;
            mTextViewTitle.setText(task.getTitle());
            mTextViewDate.setText(mTask.getSimpleDate() + " - " + mTask.getSimpleTime());
            mTextViewFirstAlphabetOfTitle.setText(task.getTitle().charAt(0) + "");

         /*   if (position % 2 == 0)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    itemView.setBackground(getActivity().getDrawable(R.drawable.bg_edit_text));
                } else
                    itemView.setBackgroundColor(getResources().getColor(R.color.Light_green));*/

        }
    }

    private class taskAdapter extends RecyclerView.Adapter<taskHolder> implements Filterable {

        private List<Task> mTasksList;
        private List<Task> completeList;


        public void setTasksList(List<Task> tasksList) {
            mTasksList = tasksList;
        }


        public taskAdapter(List<Task> taskList) {
            mTasksList = taskList;
            completeList = new ArrayList<>();
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
            if (mTaskList == null)
                return 0;
            else
                return mTasksList.size();
        }

        @Override
        public Filter getFilter() {
            return taskFilter;
        }


        private Filter taskFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Task> filterTasks = new ArrayList<>();

                if (charSequence == null | charSequence.length() == 0) {
                    filterTasks.addAll(completeList);
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();

                    for (Task item :
                            completeList) {
                        if (item.getTitle().toLowerCase().contains(filterPattern)) {
                            filterTasks.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filterTasks;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mTaskList.clear();
                mTaskList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {

        mTaskList =Repository.getInstance(getActivity().getApplicationContext()).getListTasksPerState(userID,stateTask);

        updateUI();
        mTaskAdapter.setTasksList(mTaskList);
        mTaskAdapter.notifyDataSetChanged();

    }

    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != getActivity().RESULT_OK || data == null)
            return;


        if (resultCode == REQUEST_CODE_ADDTASK_FRAGMENT) {
            StateTask stateTask = null;
            switch (getArguments().getInt(ARG_TAB_FRAGMENT_POSITION)) {
                case 0: {
                    stateTask = StateTask.TODO;
                    break;
                }
                case 1: {
                    stateTask = StateTask.DOING;
                    break;
                }
                case 2: {
                    stateTask = StateTask.DONE;
                    break;
                }
            }

            mTaskAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        mTaskAdapter.notifyDataSetChanged();
        // notifyDataSetChanged(mTaskList);
    }

    private void updateUI() {
        switch (position) {
            case 0: {
                if (Repository.getInstance(getActivity().getApplicationContext()).getUserAllTask(userID).size() == 0)
                    mTaskList = null;
                else
                    mTaskList = Repository.getInstance(getActivity().getApplicationContext()).getListTasksPerState(userID, stateTask);
                if ( mTaskList == null   || mTaskList.size()==0) {
                    mImageView.setVisibility(View.VISIBLE);
                    mImageView.setBackgroundResource(R.drawable.image_no_task);
                } else {
                    mFrameLayout_tab_fragment.setBackgroundColor(getResources().getColor(R.color.Light_green));
                    mImageView.setVisibility(View.GONE);
                }
                break;
            }
            case 1: {
                mTaskList = Repository.getInstance(getActivity().getApplicationContext()).getListTasksPerState(userID, stateTask);
                if ( mTaskList == null   || mTaskList.size()==0) {
                    mImageView.setVisibility(View.VISIBLE);
                    mImageView.setBackgroundResource(R.drawable.image_no_task);
                } else {
                    mFrameLayout_tab_fragment.setBackgroundColor(getResources().getColor(R.color.Light_green));
                    mImageView.setVisibility(View.GONE);
                }
                break;
            }

            case 2: {
                mTaskList = Repository.getInstance(getActivity().getApplicationContext()).getListTasksPerState(userID, stateTask);
                if ( mTaskList == null   || mTaskList.size()==0) {
                    mImageView.setVisibility(View.VISIBLE);
                    mImageView.setBackgroundResource(R.drawable.image_no_task);
                } else {
                    mFrameLayout_tab_fragment.setBackgroundColor(getResources().getColor(R.color.Light_green));
                    mImageView.setVisibility(View.GONE);
                }
                break;
            }
        }
    }

    private void findById(View view) {
        mFloatingActionButton = view.findViewById(R.id.floatingActionButton_add_task);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mFrameLayout_tab_fragment = view.findViewById(R.id.tab_layout);
        mImageView = view.findViewById(R.id.imageView_no_task);
    }

    private void setUser() {
        userID = getArguments().getString(TabFragment.ARG_USERNAME_TAB_FRAGMENT);
    }

    private void setStateTask() {
        switch (getArguments().


                getInt(ARG_TAB_FRAGMENT_POSITION)) {
            case 0: {
                stateTask = StateTask.TODO;
                break;
            }
            case 1: {
                stateTask = StateTask.DOING;
                break;
            }
            case 2: {
                stateTask = StateTask.DONE;
                break;
            }
        }
    }

    private void setPositionFragment() {
        position = getArguments().getInt(ARG_TAB_FRAGMENT_POSITION);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_task_manager, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_account: {




                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Are you sure you want to quit?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(MainActivity.newIntent(getActivity()));
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
                return true;
            }
            case R.id.menu_item_delete: {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Are you sure you want to delete all of your tasks?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Repository.getInstance(getActivity()).deleteAllTask(userID);
                                mTaskAdapter.notifyDataSetChanged();
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
                return true;
            }

            case R.id.app_bar_search: {
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
                searchView.setIconified(false);
                searchView.setQueryHint("Search Here");
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String str) {
                        mTaskAdapter.getFilter().filter(str);
                        mTaskAdapter.notifyDataSetChanged();
                        return false;
                    }
                });
                searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        mTaskAdapter.notifyDataSetChanged();
                        return true;
                    }
                });
                item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        notifyDataSetChanged();
                        return true;
                    }
                });
                return true;

            }
        }
        return super.onOptionsItemSelected(item);
    }

    private String getTaskReport(Task task){
        return getString(R.string.task_report,task.getTitle(),task.getSimpleDate(),task.getDescription(),
                task.getStateTask().toString());
    }



}
