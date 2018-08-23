package net.joshka.junit.json.params;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import java.io.Reader;
import java.io.StringReader;
import java.util.stream.Stream;

public class JsonArgumentsProvider implements AnnotationConsumer<JsonSource>, ArgumentsProvider {
    private String value;

    @Override
    public void accept(JsonSource jsonFileSource) {
        value = jsonFileSource.value();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        try (Reader reader = new StringReader(value)) {
            return values(reader).map(Arguments::of);
        }
    }

    private static Stream<JsonValue> values(Reader reader) {
        try (JsonReader jsonReader = Json.createReader(reader)) {
            JsonStructure structure = jsonReader.read();
            return structure.getValueType() == JsonValue.ValueType.ARRAY
                    ? structure.asJsonArray().stream()
                    : Stream.of(structure);
        }
    }
}
