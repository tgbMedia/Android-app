package com.tgb.media.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;


@Entity(
        indexes = {
                @Index(value = "id, apiLanguage", unique = true)
        }
)
public class GenreModel {

    @NotNull long id;
    @NotNull String name;
    @NotNull String apiLanguage;
@Generated(hash = 535307714)
public GenreModel(long id, @NotNull String name, @NotNull String apiLanguage) {
    this.id = id;
    this.name = name;
    this.apiLanguage = apiLanguage;
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
public String getApiLanguage() {
    return this.apiLanguage;
}
public void setApiLanguage(String apiLanguage) {
    this.apiLanguage = apiLanguage;
}

}
