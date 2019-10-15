package com.example.managingtasks.Model;

import com.example.managingtasks.GreenDao.UuidConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class User  {

    @Id(autoincrement = true)
    private Long id;

    @Unique
    @Property(nameInDb = "user_uuid")
    private String user_uuid;

    @Property(nameInDb = "uuid")
    @Index(unique = true)
    @Convert(converter = UuidConverter.class, columnType = String.class)
    private UUID mUUID;

    @Property(nameInDb = "username")
    private String username;

    @Property(nameInDb = "password")
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getMUUID() {
        return this.mUUID;
    }

    public void setMUUID(UUID mUUID) {
        this.mUUID = mUUID;
    }

    public String getUser_uuid() {
        return this.user_uuid;
    }

    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }

    public User(String username, String password) {
        this();
        this.user_uuid = mUUID.toString();
        this.username = username;
        this.password = password;
    }
    public User() {
        mUUID = UUID.randomUUID();
    }

    public User(UUID uuid) {
        mUUID = uuid;
    }

    @Generated(hash = 664099648)
    public User(Long id, String user_uuid, UUID mUUID, String username,
                String password) {
        this.id = id;
        this.user_uuid = user_uuid;
        this.mUUID = mUUID;
        this.username = username;
        this.password = password;
    }

}
