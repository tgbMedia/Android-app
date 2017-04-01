package tgb.tmdb.deserialize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Calendar;

import tgb.tmdb.models.ReleaseDate;

public class ReleaseDateDeserializer implements JsonDeserializer<ReleaseDate> {

    private DateFormat tmdbReleaseDateFormat;

    public ReleaseDateDeserializer(DateFormat tmdbReleaseDateFormat){
        this.tmdbReleaseDateFormat = tmdbReleaseDateFormat;
    }

    @Override
    public ReleaseDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        ReleaseDate releaseDate = null;

        try{
            releaseDate = new ReleaseDate(tmdbReleaseDateFormat.parse(json.getAsString()));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return releaseDate;
    }
}
