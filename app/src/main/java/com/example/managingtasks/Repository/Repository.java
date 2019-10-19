package com.example.managingtasks.Repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.managingtasks.GreenDao.TaskManagerOpenHelper;
import com.example.managingtasks.Model.DaoMaster;
import com.example.managingtasks.Model.DaoSession;
import com.example.managingtasks.Model.Task;
import com.example.managingtasks.Model.TaskDao;
import com.example.managingtasks.Model.User;

import com.example.managingtasks.Controller.StateTask;
import com.example.managingtasks.Model.UserDao;


import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class Repository {

    private static Repository instance;

    private Context mContext;
    private TaskDao mTaskDao;
    private UserDao mUserDao;
    private DaoSession daoSession;

    private Repository(Context context) {
        mContext = context.getApplicationContext();

        SQLiteDatabase db = new TaskManagerOpenHelper(mContext).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        mTaskDao = daoSession.getTaskDao();
        mUserDao = daoSession.getUserDao();

        User user = new User("admin", "123123");
        mUserDao.insert(user);
    }

    public static Repository getInstance(Context context) {

        if (instance == null) {
            instance = new Repository(context);
        }
        return instance;
    }

    public void insertUser(User user) {
        mUserDao.insert(user);
    }

    public boolean isExistUser(String username) {
        if (mUserDao.queryBuilder().where(UserDao.Properties.Username.eq(username)).unique() != null)
            return true;
        return false;
    }

    public boolean validateUser(String username, String pass) {
        QueryBuilder<User> qb = mUserDao.queryBuilder();
        qb.where(qb.and(UserDao.Properties.Username.eq(username), UserDao.Properties.Password.eq(pass)));
        if (qb.unique() != null)
            return true;
        return false;

    }

    public void insertTask(Task task) {
        mTaskDao.insert(task);
    }

    public List<Task> getUserAllTask(String userId) {
        return mTaskDao.queryBuilder().where(TaskDao.Properties.User_uuid.eq(userId)).list();

    }

    public List<Task> getListTasksPerState(String username, StateTask state) {
        return mTaskDao.queryBuilder()
                .where(TaskDao.Properties.User_uuid.eq(username), TaskDao.Properties.MStateTask.eq(state.getValue(state)))
                .list();


   /*     QueryBuilder<Task> qb = mTaskDao.queryBuilder();
        qb.and(TaskDao.Properties.User_uuid.eq(username), TaskDao.Properties.MStateTask.eq(state));
        return qb.list();*/
        //  return mTaskDao.loadAll();
    }

    public User getUser(String username) {
        return mUserDao.queryBuilder().where(UserDao.Properties.Username.eq(username)).unique();
    }

    public Task getTaskById(String userId, UUID idTask) {
        QueryBuilder<Task> qb = mTaskDao.queryBuilder();
        qb.where(qb.and(TaskDao.Properties.User_uuid.eq(userId), TaskDao.Properties.TaskId.eq(idTask)));
        return qb.unique();
    }

    public void updateTask(Task task) {
        mTaskDao.update(task);
    }

    public void deleteTask(Task task) {

        mTaskDao.delete(task);

    }

    public void deleteAllTask(String userId) {
        final DeleteQuery<Task> tableDeleteQuery = daoSession.queryBuilder(Task.class)
                .where(TaskDao.Properties.User_uuid.eq(userId))
                .buildDelete();
        tableDeleteQuery.executeDeleteWithoutDetachingEntities();
        daoSession.clear();
    }

    public List<User> getAllUsers() {
        return mUserDao.loadAll();
    }

    public void deleteUser(User user) {
        mUserDao.delete(user);
    }

    public File getPhotoFile(Task task) {
        return new File(mContext.getFilesDir(), task.getPhotoName());
    }

}

