package com.example.managingtasks.GreenDao;

import android.content.Context;

import com.example.managingtasks.Model.DaoMaster;
import com.example.managingtasks.Model.DaoMaster;

public class TaskManagerOpenHelper extends DaoMaster.OpenHelper {

    public static final String NAME = "TaskManager.db";

    public TaskManagerOpenHelper(Context context) {
        super(context, NAME);
    }
}
