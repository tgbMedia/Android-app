package com.tgb.media.database;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;

@Entity
public class CastRelationModel implements Parcelable {

    private Long movieId;
    private Long personId;
    private String character;
    private Long order;
    @ToOne(joinProperty = "personId") private PersonModel person;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2113903847)
    private transient CastRelationModelDao myDao;
    @Generated(hash = 201309700)
    public CastRelationModel(Long movieId, Long personId, String character,
            Long order) {
        this.movieId = movieId;
        this.personId = personId;
        this.character = character;
        this.order = order;
    }
    @Generated(hash = 1864097916)
    public CastRelationModel() {
    }
    public Long getMovieId() {
        return this.movieId;
    }
    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
    public Long getPersonId() {
        return this.personId;
    }
    public void setPersonId(Long personId) {
        this.personId = personId;
    }
    public String getCharacter() {
        return this.character;
    }
    public void setCharacter(String character) {
        this.character = character;
    }
    public Long getOrder() {
        return this.order;
    }
    public void setOrder(Long order) {
        this.order = order;
    }
    @Generated(hash = 1154009267)
    private transient Long person__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 362483469)
    public PersonModel getPerson() {
        Long __key = this.personId;
        if (person__resolvedKey == null || !person__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PersonModelDao targetDao = daoSession.getPersonModelDao();
            PersonModel personNew = targetDao.load(__key);
            synchronized (this) {
                person = personNew;
                person__resolvedKey = __key;
            }
        }
        return person;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1003754008)
    public void setPerson(PersonModel person) {
        synchronized (this) {
            this.person = person;
            personId = person == null ? null : person.getId();
            person__resolvedKey = personId;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.movieId);
        dest.writeValue(this.personId);
        dest.writeString(this.character);
        dest.writeValue(this.order);
        dest.writeParcelable(this.person, flags);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 477181027)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCastRelationModelDao() : null;
    }
    protected CastRelationModel(Parcel in) {
        this.movieId = (Long) in.readValue(Long.class.getClassLoader());
        this.personId = (Long) in.readValue(Long.class.getClassLoader());
        this.character = in.readString();
        this.order = (Long) in.readValue(Long.class.getClassLoader());
        this.person = in.readParcelable(PersonModel.class.getClassLoader());
    }

    public static final Creator<CastRelationModel> CREATOR = new Creator<CastRelationModel>() {
        @Override
        public CastRelationModel createFromParcel(Parcel source) {
            return new CastRelationModel(source);
        }

        @Override
        public CastRelationModel[] newArray(int size) {
            return new CastRelationModel[size];
        }
    };
}
