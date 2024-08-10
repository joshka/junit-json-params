package net.joshka.junit.json.params;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.JsonObject;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonConverter implements ArgumentConverter {
    private static final Logger logger = Logger.getLogger(JsonConverter.class.getSimpleName());

    /**
     * Convert the supplied {@code source} object according to the supplied
     * {@code context}.
     *
     * @param source the source object to convert; may be {@code null}
     * @param context the parameter context where the converted object will be
     *                used; never {@code null}
     * @return the converted object; may be {@code null} but only if the target
     * type is a reference type
     * @throws ArgumentConversionException if an error occurs during the
     *                                     conversion
     */
    @Override
    public Object convert(Object source, ParameterContext context) {
        if (!(source instanceof JsonObject)) {
            throw new ArgumentConversionException("Not a JsonObject");
        }
        JsonObject json = (JsonObject) source;
        String name = context.getParameter().getName();
        Class<?> type = context.getParameter().getType();
        if (type == String.class) {
            return json.getString(name);
        } else if (type == int.class) {
            return json.getInt(name);
        } else if (type == boolean.class) {
            return json.getBoolean(name);
        }
        throw new ArgumentConversionException("Can't convert to type: '" + type.getName() + "'");
    }

    /**
     * Convert the supplied {@code source} object according to the supplied
     * {@code context}.
     *
     * @param source the source object to convert; may be {@code null}
     * @param targetClass is class what source must be converted to
     * @return the converted object
     * @throws ArgumentConversionException if an error occurs during the conversion
     */
    static Object convert(Object source, Class<?> targetClass) throws ArgumentConversionException {
        if(!source.getClass().getName().startsWith("org.eclipse.parsson.Json")) {
            throw new ArgumentConversionException("Not a JsonStructure type");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = source.toString();
        try {
            return objectMapper.readValue(jsonString, targetClass);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "an exception was thrown");
        }
        return new ArgumentConversionException("Can't convert " + jsonString + " to " + targetClass.getName());
    }
}
