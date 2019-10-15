package com.example.managingtasks.Controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.managingtasks.Model.Task;
import com.example.managingtasks.Repository.Repository;
import com.example.managingtasks.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class TaskDetailDialogFragment extends DialogFragment {

    public static final String ARG_TASK_ITEM = "ARG_TASK_ITEM";
    public static final String TAG_TIME_PICKER_TASK_DETAIL = "tag_timePicker_task_detail";
    public static final String TAG_DATE_PICKER_TASK_DETAIL = "tag_date_picker_task_detail";
    public static final int REQUEST_CODE_DATEPICKER_TASK_DETAIL = 4;
    public static final int REQUEST_CODETIMEPICKER_TASK_DETIL = 5;
    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private Button mButtonDate;
    private Button mButtonTime;
    private CheckBox mCheckBox;
    private RadioButton mRadioButtonDone, mRadioButtonDoing, mRadioButtonToDo;
    private RadioGroup mRadioGroup;


    private StateTask stateTask;
    private String uuid_task;
    private Date tempTime;
    private Date mDate;
    private Task mTask;
    private String user;
    private List<Task> taskList;

    public static TaskDetailDialogFragment newInstance(String username, String uuid_task) {

        Bundle args = new Bundle();
        args.putString(ARG_TASK_ITEM, uuid_task);
        args.putString(TabFragment.ARG_USERNAME_TAB_FRAGMENT, username);

        TaskDetailDialogFragment fragment = new TaskDetailDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TaskDetailDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = getArguments().getString(TabFragment.ARG_USERNAME_TAB_FRAGMENT);
        taskList = new ArrayList<>();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater
                .inflate(R.layout.fragment_task_detail, null, false);

        findTask();

        mDate = mTask.getDate();
        tempTime = mTask.getDate();

        findView(view);
        setStateTask();
        initUI();
        initListeners();


        return new AlertDialog.Builder(getActivity())
                .setTitle("Editing Task")
                .setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Repository.getInstance(getActivity()).deleteTask(mTask);
                        ((TabFragment) getTargetFragment()).notifyDataSetChanged();
                        dismiss();
                    }
                })
                .setPositiveButton("edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mTask.setTitle(mEditTextTitle.getText().toString());
                        mTask.setDescription(mEditTextDescription.getText().toString());
                        mTask.setDon(mCheckBox.isChecked());
                        mTask.setDate(mDate);

                        changeStateTask();
                        Repository.getInstance(getActivity()).updateTask(mTask);
                        ((TabFragment) getTargetFragment()).notifyDataSetChanged();
                    }
                })
                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })

                .setView(view)
                .create();
    }

    private void findTask() {
        uuid_task =  getArguments().getString(ARG_TASK_ITEM);
        mTask = Repository.getInstance(getContext()).getTaskById(user,UUID.fromString(uuid_task));
    }

    private void initUI() {
        mEditTextTitle.setText(mTask.getTitle());
        mEditTextDescription.setText(mTask.getDescription());
        mCheckBox.setChecked(mTask.isDon());
        mButtonDate.setText(mTask.getSimpleDate());
        mButtonTime.setText(mTask.getSimpleTime());
    }


    private void findView(View view) {
        mEditTextDescription = view.findViewById(R.id.edit_text_title_description);
        mEditTextTitle = view.findViewById(R.id.edit_text_title);
        mButtonDate = view.findViewById(R.id.button_date);
        mButtonTime = view.findViewById(R.id.button_time);
        mCheckBox = view.findViewById(R.id.checkBox);
        mRadioButtonDoing = view.findViewById(R.id.radioButton_doing);
        mRadioButtonDone = view.findViewById(R.id.radioButton_done);
        mRadioButtonToDo = view.findViewById(R.id.radioButton_todo);
        mRadioGroup = view.findViewById(R.id.radioGroup_taskState);
    }

    private void setStateTask() {

        switch (mTask.getStateTask()) {
            case TODO: {
                stateTask = StateTask.TODO;
                mRadioButtonToDo.setChecked(true);
                mRadioButtonDone.setChecked(false);
                mRadioButtonDoing.setChecked(false);
                break;
            }
            case DOING: {
                stateTask = StateTask.DOING;
                mRadioButtonDoing.setChecked(true);
                mRadioButtonToDo.setChecked(false);
                mRadioButtonDone.setChecked(false);
                break;
            }
            case DONE: {
                stateTask = StateTask.DONE;
                mRadioButtonDone.setChecked(true);
                mRadioButtonDoing.setChecked(false);
                mRadioButtonToDo.setChecked(false);

                break;
            }
        }
    }

    private void changeStateTask() {

        int checkedId = mRadioGroup.getCheckedRadioButtonId();

        if (mRadioButtonToDo.getId() == checkedId) {
            mTask.setStateTask(StateTask.TODO);

        }
        if (mRadioButtonDoing.getId() == checkedId) {
            mTask.setStateTask(StateTask.DOING);

        }
        if (mRadioButtonDone.getId() == checkedId) {
            mTask.setStateTask(StateTask.DONE);
        }
    }


    private void initListeners() {

        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mTask.getDate());
                datePickerFragment.setTargetFragment(TaskDetailDialogFragment.this, REQUEST_CODE_DATEPICKER_TASK_DETAIL);
                datePickerFragment.show(getFragmentManager(), TAG_DATE_PICKER_TASK_DETAIL);

            }

        });

        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mDate);
                timePickerFragment.setTargetFragment(TaskDetailDialogFragment.this, REQUEST_CODETIMEPICKER_TASK_DETIL);
                timePickerFragment.show(getFragmentManager(), TAG_TIME_PICKER_TASK_DETAIL);
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != getActivity().RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_DATEPICKER_TASK_DETAIL) {

            mDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_TASK_DATE);
            mDate.setHours(tempTime.getHours());
            mDate.setMinutes(tempTime.getMinutes());

            mTask.setDate(mDate);
            DateFormat df = new SimpleDateFormat("dd MMM yyyy");
            mButtonDate.setText(df.format(mTask.getDate()));
        }

        if (requestCode == REQUEST_CODETIMEPICKER_TASK_DETIL) {

            tempTime = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TASK_TIME);
            mDate.setHours(tempTime.getHours());
            mDate.setMinutes(tempTime.getMinutes());

            mTask.setDate(mDate);

            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            mButtonTime.setText(dateFormat.format(mTask.getDate()));

        }
    }


}


