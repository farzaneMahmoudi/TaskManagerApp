package com.example.managingtasks.Model;

import com.example.managingtasks.Controller.StateTask;
import com.example.managingtasks.GreenDao.StateConverter;
import com.example.managingtasks.GreenDao.UuidConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "task")
public class Task  {

    @Id(autoincrement = true)
    private Long id;

  /* @Property(nameInDb = "task_username")
    private String task_user;*/

    @Property(nameInDb = "user_uuid")
    private String user_uuid;

    @Property(nameInDb = "task_uuid")
    @Index(unique = true)
    @Convert(converter = UuidConverter.class,columnType = String.class)
    private UUID taskId;

    @Property(nameInDb = "state")
    @Convert(converter = StateConverter.class,columnType = Integer.class)
    private StateTask mStateTask;

    @Property(nameInDb = "title")
    private String mTitle;

    @Property(nameInDb = "description")
    private String mDescription;


    @Property(nameInDb = "isdone")
    private boolean mIsDon;

    @Transient
    private SimpleDateFormat mSimpleDateFormat;

    @Property(nameInDb = "date")
    private Date mDate;

    @Transient
    private char firstAlphabetTitle;

    public char getFirstAlphabetTitle() {
        return firstAlphabetTitle;
    }

    public void setDetailTask(StateTask stateTask, String title, String description, boolean isDon, char firstAlphabetTitle) {
        mStateTask = stateTask;
        mTitle = title;
        mDescription = description;
        mIsDon = isDon;
        this.firstAlphabetTitle = firstAlphabetTitle;
        //   this.task_user = task_user;
    }

    public void setFirstAlphabetTitle(char firstAlphabetTitle) {
        this.firstAlphabetTitle = firstAlphabetTitle;
    }

    public Task() {
        taskId = UUID.randomUUID();
        mDate = new Date();
    }

    public Task(String user_uuid) {
        //this.user_uuid= user_uuid;
        this.user_uuid = user_uuid;
        taskId = UUID.randomUUID();
        mDate = new Date();
    }

    public Task(UUID UUID) {
        taskId = UUID;
    }

    @Generated(hash = 1856564405)
    public Task(Long id, String user_uuid, UUID taskId, StateTask mStateTask, String mTitle, String mDescription, boolean mIsDon,
                Date mDate) {
        this.id = id;
        this.user_uuid = user_uuid;
        this.taskId = taskId;
        this.mStateTask = mStateTask;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mIsDon = mIsDon;
        this.mDate = mDate;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isDon() {
        return mIsDon;
    }

    public void setDon(boolean don) {
        mIsDon = don;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }


    public StateTask getStateTask() {
        return mStateTask;
    }

    public void setStateTask(StateTask stateTask) {
        mStateTask = stateTask;
    }

    public String getSimpleDate() {
        mSimpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        return mSimpleDateFormat.format(mDate);
    }


    public String getSimpleTime() {
        mSimpleDateFormat = new SimpleDateFormat("hh:mm a");
        return mSimpleDateFormat.format(mDate);

    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public StateTask getMStateTask() {
        return this.mStateTask;
    }

    public void setMStateTask(StateTask mStateTask) {
        this.mStateTask = mStateTask;
    }

    public String getMTitle() {
        return this.mTitle;
    }

    public void setMTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getMDescription() {
        return this.mDescription;
    }

    public void setMDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public boolean getMIsDon() {
        return this.mIsDon;
    }

    public void setMIsDon(boolean mIsDon) {
        this.mIsDon = mIsDon;
    }

    public Date getMDate() {
        return this.mDate;
    }

    public void setMDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getUser_uuid() {
        return this.user_uuid;
    }

    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }
}
