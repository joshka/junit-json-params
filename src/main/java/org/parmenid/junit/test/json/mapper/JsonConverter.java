package org.parmenid.junit.test.json.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.converter.ArgumentConversionException;

public class JsonConverter {

    public static Object convert(String source, Class<?> target) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(source, target);
        } catch (JsonProcessingException e){
            throw new ArgumentConversionException("Can't convert " + source +
                    " to type: " + target.getName() + ". Check json format");
        }
    }

}
