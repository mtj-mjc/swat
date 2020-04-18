package ch.mrnjec.swat.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.util.Date;

/**
 * Serializes ISODate for MongoDB
 *
 * @since: 10.05.2019
 * @author: Matej Mrnjec
 */
public class IsoDateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        try {
            if (value == null) {
                jgen.writeNull();
            } else {
                jgen.writeStartObject();
                jgen.writeFieldName("$date");
                String isoDate = ISODateTimeFormat.dateTime().print(new DateTime(value));
                jgen.writeString(isoDate);
                jgen.writeEndObject();
            }
        } catch (Exception ex) {
            jgen.writeNull();
        }
    }
}
