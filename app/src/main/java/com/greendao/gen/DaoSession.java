package com.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.hua.note.data.NoteEntity;

import com.greendao.gen.NoteEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig noteEntityDaoConfig;

    private final NoteEntityDao noteEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        noteEntityDaoConfig = daoConfigMap.get(NoteEntityDao.class).clone();
        noteEntityDaoConfig.initIdentityScope(type);

        noteEntityDao = new NoteEntityDao(noteEntityDaoConfig, this);

        registerDao(NoteEntity.class, noteEntityDao);
    }
    
    public void clear() {
        noteEntityDaoConfig.clearIdentityScope();
    }

    public NoteEntityDao getNoteEntityDao() {
        return noteEntityDao;
    }

}
