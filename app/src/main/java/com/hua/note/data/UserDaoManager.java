package com.hua.note.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.greendao.gen.DaoMaster;
import com.greendao.gen.DaoSession;
import com.greendao.gen.NoteEntityDao;
import com.greendao.gen.UserEntityDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Iaovy on 2020/3/28 9:49
 *
 * @email Cymbidium@outlook.com
 */
public class UserDaoManager {
    private DaoMaster.DevOpenHelper helper;
    private Context context;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private UserEntityDao userDao;
    private NoteEntityDao noteDao;

    @SuppressLint("StaticFieldLeak")
    private static UserDaoManager userDaoManager;

    public static UserDaoManager getInstance(Context context) {
        if (userDaoManager == null) {
            synchronized (UserDaoManager.class) {
                if (userDaoManager == null) {
                    userDaoManager = new UserDaoManager(context);
                }
            }
        }
        return userDaoManager;
    }

    private UserDaoManager(Context context) {
        this.context = context;
        helper = new DaoMaster.DevOpenHelper(context, "user_db", null);
        daoMaster = new DaoMaster(getWritableDatabase());
        daoSession = daoMaster.newSession();
        userDao = daoSession.getUserEntityDao();
        noteDao = daoSession.getNoteEntityDao();
    }

    private SQLiteDatabase getWritableDatabase() {
        if (helper == null) {
            helper = new DaoMaster.DevOpenHelper(context, "user_db", null);
        }
        return helper.getWritableDatabase();
    }

    public long insert(UserEntity userEntity) {
        return userDao.insert(userEntity);
    }

    public long insertNote(NoteEntity userEntity) {
        return noteDao.insert(userEntity);
    }

    public void updateNote(String flag, String text, String time) {
        NoteEntity noteEntity = noteDao.queryBuilder().where(NoteEntityDao.Properties.Flag.eq(flag)).build().unique();
        noteEntity.setText(text);
        noteEntity.setTime(time);
        noteDao.update(noteEntity);
    }

    public void deleteNote(NoteEntity noteEntity) {
        daoSession.delete(noteEntity);
    }

    public List<NoteEntity> findListByName(String userName) {
        List<NoteEntity> oldList = noteDao.queryBuilder().where(NoteEntityDao.Properties.UserId.eq(userName)).build().list();
        Collections.reverse(oldList);
        return oldList;
    }

    public int findUserByName(String userName) {
        if (userDao.queryBuilder().where(UserEntityDao.Properties.UserName.eq(userName)).build().unique() != null) {
            return 1;
        } else return 0;
    }

    public int isPwdRightByName(String userName, String userPwd) {
        UserEntity user = userDao.queryBuilder().where(UserEntityDao.Properties.UserName.eq(userName)).build().unique();
        if (user.getUserPwd().equals(userPwd)) {
            return 1;
        } else return 0;
    }

    public NoteEntity findNoteByFlag(String flag) {
        return noteDao.queryBuilder().where(NoteEntityDao.Properties.Flag.eq(flag)).build().unique();
    }
}
