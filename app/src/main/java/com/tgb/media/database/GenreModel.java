package com.tgb.media.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class GenreModel {

    @Id private Long id;
    private String name;
    @Generated(hash = 618769937)
    public GenreModel(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 973863029)
    public GenreModel() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
