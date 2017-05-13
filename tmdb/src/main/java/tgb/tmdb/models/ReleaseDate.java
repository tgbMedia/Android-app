package tgb.tmdb.models;

import java.util.Calendar;
import java.util.Date;

public class ReleaseDate {

    private Date releaseDate;

    public ReleaseDate(Date date) {
        this.releaseDate = date;
    }

    public Date getDate(){
        return releaseDate;
    }

    public Calendar getCalendar(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());

        return cal;
    }

    public Integer getYear(){
        return getCalendar().get(Calendar.YEAR);
    }

    public String getTextual(){
        return "";
    }
}
