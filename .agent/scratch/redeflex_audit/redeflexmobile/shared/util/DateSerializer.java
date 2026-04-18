package com.axys.redeflexmobile.shared.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class DateSerializer implements JsonDeserializer<Date> {

    private static final String[] DATE_FORMATS = new String[]{
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss"
    };

    @Override
    public Date deserialize(JsonElement jsonElement, Type typeOF,
                            JsonDeserializationContext context) throws JsonParseException {

        for (String format : DATE_FORMATS) {
            try {
                return new SimpleDateFormat(format, Locale.getDefault()).parse(jsonElement.getAsString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
    }
}
