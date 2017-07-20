package com.tgb.media.database;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PersonModel implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.photo);
        dest.writeString(this.name);
    }

    protected PersonModel(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.photo = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<PersonModel> CREATOR = new Parcelable.Creator<PersonModel>() {
        @Override
        public PersonModel createFromParcel(Parcel source) {
            return new PersonModel(source);
        }

        @Override
        public PersonModel[] newArray(int size) {
            return new PersonModel[size];
        }
    };
}
