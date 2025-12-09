package com.tierraburritoservidor.domain.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String date = p.getText();
        if (date.contains(".")) {
            int dotIndex = date.indexOf(".");
            String secondsFraction = date.substring(dotIndex + 1);
            if (secondsFraction.length() > 3) {
                date = date.substring(0, dotIndex + 4); // Keep only 3 digits
            }
        }
        return LocalDateTime.parse(date, formatter);
    }
}
