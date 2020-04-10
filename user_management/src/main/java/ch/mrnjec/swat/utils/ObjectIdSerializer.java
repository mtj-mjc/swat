package ch.mrnjec.swat.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bson.types.ObjectId;

import java.io.IOException;

/**
 * Serializes ObjectId
 *
 * @since: 10.05.2019
 * @author: Matej Mrnjec
 */
public class ObjectIdSerializer extends JsonSerializer<ObjectId> {
    @Override
    public void serialize(ObjectId o, JsonGenerator j, SerializerProvider s) throws IOException, JsonProcessingException {
        if(o == null) {
            j.writeNull();
        } else {
            j.writeStartObject();
            j.writeObjectField("$oid", o.toString());
            j.writeEndObject();
        }
    }
}
