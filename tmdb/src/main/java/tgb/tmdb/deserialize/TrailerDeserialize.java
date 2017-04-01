package tgb.tmdb.deserialize;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tgb.tmdb.models.Trailer;
import tgb.tmdb.models.Video;

public class TrailerDeserialize implements JsonDeserializer<Trailer> {

    private String deviceLanguage;

    public TrailerDeserialize(String deviceLanguage){
        this.deviceLanguage = deviceLanguage;
    }

    @Override
    public Trailer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Type collectionType = new TypeToken<Collection<Trailer>>(){}.getType();
        List<Trailer> videos = new Gson().fromJson(json.getAsJsonObject().getAsJsonArray("results"), collectionType);

        Collections.sort(videos, new Comparator<Video>() {
            @Override
            public int compare(Video left, Video right) {
                boolean leftIsSameLang = left.language.equals(deviceLanguage);
                boolean rightIsSameLang = right.language.equals(deviceLanguage);

                if(leftIsSameLang && rightIsSameLang)
                    return left.size.compareTo(right.size);

                return leftIsSameLang ? -1 : 1;
            }
        });

        return videos.size() > 0 ? videos.get(0) : null;
    }
}
