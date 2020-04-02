package com.hua.note.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.greendao.gen.DaoMaster;
import com.greendao.gen.DaoSession;
import com.greendao.gen.NoteEntityDao;

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
        noteDao = daoSession.getNoteEntityDao();
    }

    private SQLiteDatabase getWritableDatabase() {
        if (helper == null) {
            helper = new DaoMaster.DevOpenHelper(context, "user_db", null);
        }
        return helper.getWritableDatabase();
    }

    /**
     * 新建note
     *
     * @param noteEntity
     * @return
     */
    public long insertNote(NoteEntity noteEntity) {
        return noteDao.insert(noteEntity);
    }

    /**
     * 更新note
     *
     * @param id
     * @param text
     * @param time
     */
    public void updateNote(Long id, String text, Long time, String title) {
        NoteEntity noteEntity = noteDao.queryBuilder().where(NoteEntityDao.Properties.Id.eq(id)).build().unique();
        noteEntity.setText(text);
        noteEntity.setTime(time);
        noteEntity.setTitle(title);
        noteDao.update(noteEntity);
    }

    /**
     * 置顶note
     *
     * @param id
     * @param time
     */
    public void stickyNote(Long id, Long time) {
        NoteEntity noteEntity = noteDao.queryBuilder().where(NoteEntityDao.Properties.Id.eq(id)).build().unique();
        noteEntity.setTime(time);
        noteEntity.setName("sticky");
        noteDao.update(noteEntity);
    }

    /**
     * 取消置顶note
     *
     * @param id
     * @param time
     */
    public void deleteSticky(Long id, Long time) {
        NoteEntity noteEntity = noteDao.queryBuilder().where(NoteEntityDao.Properties.Id.eq(id)).build().unique();
        noteEntity.setTime(time);
        noteEntity.setName("default");
        noteDao.update(noteEntity);
    }

    /**
     * 删除note
     *
     * @param noteEntity
     */
    public void deleteNote(NoteEntity noteEntity) {
        daoSession.delete(noteEntity);
    }

    public List<NoteEntity> findListByName(String name) {
        List<NoteEntity> noteList = noteDao.queryBuilder().where(NoteEntityDao.Properties.Name.eq(name)).build().list();
        Collections.sort(noteList);
        return noteList;
    }

    public NoteEntity findNoteById(Long id) {
        return noteDao.queryBuilder().where(NoteEntityDao.Properties.Id.eq(id)).build().unique();
    }

    /**
     * 模糊查找
     *
     * @param text
     * @return
     */
    public List<NoteEntity> findListLikeText(String text) {
        List<NoteEntity> noteList = noteDao.queryBuilder().where(NoteEntityDao.Properties.Text.like("%" + text + "%")).build().list();
        Collections.sort(noteList);
        return noteList;
    }

    public List<NoteEntity> findListLikeTitle(String text) {
        List<NoteEntity> noteList = noteDao.queryBuilder().where(NoteEntityDao.Properties.Title.like("%" + text + "%")).build().list();
        Collections.sort(noteList);
        return noteList;
    }
}
