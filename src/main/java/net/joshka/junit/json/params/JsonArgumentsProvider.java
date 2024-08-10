package net.joshka.junit.json.params;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonParsingException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.stream.Stream;

public class JsonArgumentsProvider implements AnnotationConsumer<JsonSource>, ArgumentsProvider {
    private String value;
    private Class<?> targetClass;
    private boolean isArrayClass;

    @Override
    public void accept(JsonSource jsonSource) {
        value = jsonSource.value();
        targetClass = jsonSource.target();
        isArrayClass = targetClass.isArray();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        try {
            return getArguments(value);
        } catch (JsonParsingException e) {
            // attempt to parse simplified json e.g. "{'key':value'}"
            if (e.getMessage().contains("Unexpected char 39")) {
                return getArguments(value.replace("'", "\""));
            }
            throw e;
        }
    }

    private Stream<? extends Arguments> getArguments(String value) throws IOException {
        try (Reader reader = new StringReader(value)) {
            if (this.targetClass.equals(Object.class)) {
                return values(reader, this.isArrayClass)
                        .map(Arguments::of);
            }
            return values(reader, this.isArrayClass)
                    .map(json -> JsonConverter.convert(json, this.targetClass))
                    .map(Arguments::of);
        }
    }

    private static Stream<JsonValue> values(Reader reader, boolean arrayClass) {
        try (JsonReader jsonReader = Json.createReader(reader)) {
            JsonStructure structure = jsonReader.read();
            Stream<JsonValue> values = Stream.of(structure);
            if (structure.getValueType() == JsonValue.ValueType.ARRAY) {
                values = arrayClass ? values : structure.asJsonArray().stream();
            }
            return values;
        }
    }
}
