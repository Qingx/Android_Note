package com.hua.note.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class NoteEntity implements Comparable<NoteEntity> {
    @Id
    private Long id;
    @Property(nameInDb = "time")
    private Long time;
    @Property(nameInDb = "text")
    private String text;
    @Property(nameInDb = "type")
    private String type;
    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "title")
    private String title;

    @Generated(hash = 1867373649)
    public NoteEntity(Long id, Long time, String text, String type, String name,
                      String title) {
        this.id = id;
        this.time = time;
        this.text = text;
        this.type = type;
        this.name = name;
        this.title = title;
    }

    @Generated(hash = 734234824)
    public NoteEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(NoteEntity o) {
        return o.getTime().compareTo(this.getTime());
    }


}
