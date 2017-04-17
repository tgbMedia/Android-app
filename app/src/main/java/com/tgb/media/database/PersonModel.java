package com.tgb.media.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PersonModel {

    @Id private Long id;
    private String photo;
    private String name;
    @Generated(hash = 1689681939)
    public PersonModel(Long id, String photo, String name) {
        this.id = id;
        this.photo = photo;
        this.name = name;
    }
    @Generated(hash = 1012623646)
    public PersonModel() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPhoto() {
        return this.photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
