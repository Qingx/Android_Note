package com.hua.note.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.greendao.gen.DaoMaster;
import com.greendao.gen.DaoSession;
import com.greendao.gen.NoteEntityDao;
import com.greendao.gen.UserEntityDao;

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

    public long insertNote(NoteEntity noteEntity) {
        return noteDao.insert(noteEntity);
    }

    public void updateNote(Long id, String text, Long time) {
        NoteEntity noteEntity = noteDao.queryBuilder().where(NoteEntityDao.Properties.Id.eq(id)).build().unique();
        noteEntity.setText(text);
        noteEntity.setTime(time);
        noteDao.update(noteEntity);
    }

    public void stickyNote(Long id, Long time) {
        NoteEntity noteEntity = noteDao.queryBuilder().where(NoteEntityDao.Properties.Id.eq(id)).build().unique();
        noteEntity.setTime(time);
        noteEntity.setName("sticky");
        noteDao.update(noteEntity);
    }

    public void deletesticky(Long id, Long time) {
        NoteEntity noteEntity = noteDao.queryBuilder().where(NoteEntityDao.Properties.Id.eq(id)).build().unique();
        noteEntity.setTime(time);
        noteEntity.setName("default");
        noteDao.update(noteEntity);
    }

    public void deleteNote(NoteEntity noteEntity) {
        daoSession.delete(noteEntity);
    }


    public List<NoteEntity> findListByName(String name) {
        List<NoteEntity> noteList = noteDao.queryBuilder().where(NoteEntityDao.Properties.Name.eq(name)).build().list();
        Collections.sort(noteList);
        return noteList;
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

    public NoteEntity findNoteById(Long id) {
        return noteDao.queryBuilder().where(NoteEntityDao.Properties.Id.eq(id)).build().unique();
    }
}
