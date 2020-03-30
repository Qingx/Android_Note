package com.hua.note.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Iaovy on 2020/3/28 9:35
 *
 * @email Cymbidium@outlook.com
 */
@Entity
public class UserEntity {
    @Id
    private String userName;
    @Property(nameInDb = "USERPWD")
    private String userPwd;

    @Generated(hash = 760400838)
    public UserEntity(String userName, String userPwd) {
        this.userName = userName;
        this.userPwd = userPwd;
    }
    @Generated(hash = 1433178141)
    public UserEntity() {
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPwd() {
        return this.userPwd;
    }
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
    
}
