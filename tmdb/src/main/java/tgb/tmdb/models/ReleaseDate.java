package tgb.tmdb.models;

import java.util.Date;

public class ReleaseDate {

    private Date releaseDate;

    public ReleaseDate(Date date) {
        this.releaseDate = date;
    }

    public Date getDate(){
        return releaseDate;
    }

    public String getTextual(){
        return "";
    }
}
