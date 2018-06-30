package com.yoga.core.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateYMDHMJsonDeserializer extends JsonDeserializer<Date> {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        try {
            return formatter.parse(p.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
