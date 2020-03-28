package com.hua.note.data;

import androidx.annotation.IntDef;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Iaovy on 2020/3/27 16:07
 *
 * @email Cymbidium@outlook.com
 */

@Entity
public class NoteEntity {
    @Id
    private String flag;
    @Property(nameInDb = "TEXT")
    private String text;
    @Property(nameInDb = "TIME")
    private String time;
    @Property(nameInDb = "USERID")
    private  String userId;

    public NoteEntity(String flag, String text, String time) {
        this.flag = flag;
        this.text = text;
        this.time = time;
    }

    @Generated(hash = 1072969095)
    public NoteEntity(String flag, String text, String time, String userId) {
        this.flag = flag;
        this.text = text;
        this.time = time;
        this.userId = userId;
    }

    @Generated(hash = 734234824)
    public NoteEntity() {
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
