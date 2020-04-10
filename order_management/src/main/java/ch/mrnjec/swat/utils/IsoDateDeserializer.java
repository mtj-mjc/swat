package ch.mrnjec.swat.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Deserializes ISODate
 * Note: It only deserializes ISODate from MongoDb (cause of the $date)
 *
 * @since: 10.05.2019
 * @author: Matej Mrnjec
 */
public class IsoDateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        String dateValue = node.get("$date").asText();

        // Check if Date in this Format
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = null;
        try {
            date = df.parse(dateValue);
        } catch (ParseException e) {
            System.out.println("Ignored ParseException because it may be a Long Value");
        }

        // If not then it must be a Long
        if(date == null){
            date = new Date(Long.parseLong(dateValue));
        }
        return date;
    }
}
