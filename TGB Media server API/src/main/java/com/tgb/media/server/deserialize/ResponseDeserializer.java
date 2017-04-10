package com.tgb.media.server.deserialize;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.tgb.media.server.models.Response;

import java.lang.reflect.Type;

public class ResponseDeserializer<T> implements JsonDeserializer<Response<T>> {

    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String RESULTS = "results";

    @Override
    public Response<T> deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException
    {
        Response<T> response = new Response<>();

        response.status = je.getAsJsonObject().get(STATUS).getAsString();
        response.error = je.getAsJsonObject().get(ERROR).getAsString();


        JsonElement results = je.getAsJsonObject().get(RESULTS);
        response.results = new Gson().fromJson(results, type);

        return response;

    }
}
