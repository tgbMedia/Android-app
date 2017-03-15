package com.tgb.media.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        indexes = {
                @Index(value = "name", unique = true)
        }
)
public class GenreModel {

    public @Id long id;
    public String name;

    @Generated(hash = 267808659)
    public GenreModel(long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 973863029)
    public GenreModel() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
