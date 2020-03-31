package com.hua.note.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class StickyEntity implements Comparable<StickyEntity> {
    @Id
    private Long id;
    @Property(nameInDb = "stickyTime")
    private Long time;
    @Property(nameInDb = "stickyText")
    private String text;
    @Property(nameInDb = "stickyType")
    private String type;
    @Property(nameInDb = "stickyName")
    private String name;

    @Generated(hash = 1812869811)
    public StickyEntity(Long id, Long time, String text, String type, String name) {
        this.id = id;
        this.time = time;
        this.text = text;
        this.type = type;
        this.name = name;
    }

    @Generated(hash = 1258311365)
    public StickyEntity() {
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

    @Override
    public int compareTo(StickyEntity o) {
        return o.getTime().compareTo(this.getTime());
    }
}
