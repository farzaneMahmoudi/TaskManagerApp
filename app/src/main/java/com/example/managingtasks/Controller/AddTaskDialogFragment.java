package com.example.managingtasks.Controller;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.managingtasks.Model.Task;
import com.example.managingtasks.R;
import com.example.managingtasks.Repository.Repository;
import com.example.managingtasks.Utils.PictureUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskDialogFragment extends DialogFragment {


    private static final int REQUEST_CODE_DATE_PICKER = 2;
    private static final int REQUEST_CODE_TIME_PICKER = 3;
    private static final String TAG_DATE_PICKER = "DatePicker";
    private static final String TAG_TIME_PICKER = "TimePicker";
    public static final String AUTHORITY_FILE_PROVIDER = "com.example.managingtasks.fileProvider";
    public static final int REQUEST_CODE_CAPTURE_IMAGE = 6;

    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private Button mButtonDate;
    private Button mButtonTime;
    private CheckBox mCheckBox;
    private ImageButton mImageButtonCamera;
    private ImageView mImageViewPicture;


    private Date tempTime;
    private Date mDate;
    private StateTask stateTask;
    private String title;
    private String description;
    private boolean isDon;
    private char firstAlphabetTitle;
    private Task mTask;
    private String user;
    private Uri mPhotoUri;
    private File mPhotoFile;


    public static AddTaskDialogFragment newInstance(int position, String username) {

        Bundle args = new Bundle();
        args.putInt(TabFragment.ARG_TAB_FRAGMENT_POSITION, position);
        args.putString(TabFragment.ARG_USERNAME_TAB_FRAGMENT, username);

        AddTaskDialogFragment fragment = new AddTaskDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AddTaskDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStateTask();
        setUser();

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater
                .inflate(R.layout.fragment_add_task, null, false);


        findView(view);
        mTask = new Task(user);
        mPhotoFile = Repository.getInstance(getContext()).getPhotoFile(mTask);
        mTask.setDate(new Date());
        mDate = mTask.getDate();
        tempTime = mTask.getDate();
        initUI();
        initListeners();


        return new AlertDialog.Builder(getActivity())
                .setTitle("Adding Task")
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (mEditTextTitle.getText().toString().isEmpty())
                            Toast.makeText(getActivity(), R.string.TOAST_TITLE_MUST_BE_COMPLETE, Toast.LENGTH_SHORT).show();
                        else {

                            title = mEditTextTitle.getText().toString();
                            description = mEditTextDescription.getText().toString();
                            isDon = mCheckBox.isChecked();
                            firstAlphabetTitle = getFirstAlphabetTitle(title);

                            mTask.setDetailTask(stateTask, title, description, isDon, firstAlphabetTitle);
                            Repository.getInstance(getActivity()).insertTask(mTask);

                            ((TabFragment) getTargetFragment()).notifyDataSetChanged();

                        }
                    }
                })
                .setView(view)
                .create();
    }

    private void initUI() {

        mButtonDate.setText(mTask.getSimpleDate());
        mButtonTime.setText(mTask.getSimpleTime());

    }

    public char getFirstAlphabetTitle(String string) {
        return string.charAt(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != getActivity().RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_DATE_PICKER) {
            mDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_TASK_DATE);
            mDate.setHours(tempTime.getHours());
            mDate.setMinutes(tempTime.getMinutes());

            mTask.setDate(mDate);
            DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            mButtonDate.setText(dateFormat.format(mTask.getDate()));

        }

        if (requestCode == REQUEST_CODE_TIME_PICKER) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TASK_TIME);

            tempTime = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TASK_TIME);
            mDate.setHours(tempTime.getHours());
            mDate.setMinutes(tempTime.getMinutes());

            mTask.setDate(mDate);

            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            mButtonTime.setText(dateFormat.format(mTask.getDate()));
        } else if (requestCode == REQUEST_CODE_CAPTURE_IMAGE) {
            updatePhotoView();

            getActivity().revokeUriPermission(
                    mPhotoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

    }

    private void findView(View view) {
        mEditTextDescription = view.findViewById(R.id.edit_text_title_description);
        mEditTextTitle = view.findViewById(R.id.edit_text_title);
        mButtonDate = view.findViewById(R.id.button_date);
        mButtonTime = view.findViewById(R.id.button_time);
        mCheckBox = view.findViewById(R.id.checkBox);
        mImageButtonCamera = view.findViewById(R.id.image_button_camera);
        mImageViewPicture = view.findViewById(R.id.image_view_camera);
    }

    private void setUser() {
        user = getArguments().getString(TabFragment.ARG_USERNAME_TAB_FRAGMENT);
    }

    private void setStateTask() {
        switch (getArguments().getInt(TabFragment.ARG_TAB_FRAGMENT_POSITION)) {
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

    private void initListeners() {

        mButtonDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mTask.getDate());
                datePickerFragment.setTargetFragment(AddTaskDialogFragment.this, REQUEST_CODE_DATE_PICKER);
                datePickerFragment.show(getFragmentManager(), TAG_DATE_PICKER);

            }

        });

        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mDate);
                timePickerFragment.setTargetFragment(AddTaskDialogFragment.this, REQUEST_CODE_TIME_PICKER);
                timePickerFragment.show(getFragmentManager(), TAG_TIME_PICKER);
            }

        });

        mImageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhotoFile == null)
                    return;

                mPhotoUri = FileProvider.getUriForFile(getContext(),
                        AUTHORITY_FILE_PROVIDER,
                        mPhotoFile);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);

                List<ResolveInfo> cameraActivities = getActivity().getPackageManager()
                        .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : cameraActivities) {
                    getActivity().grantUriPermission(resolveInfo.activityInfo.packageName,
                            mPhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                try {
                    startActivityForResult(intent, REQUEST_CODE_CAPTURE_IMAGE);
                } catch (ActivityNotFoundException activityNotFound) {
                    Toast.makeText(getActivity(), R.string.No_camera_found, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mImageViewPicture.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getAbsolutePath(), getActivity());
            mImageViewPicture.setImageBitmap(bitmap);
        }
    }


}
