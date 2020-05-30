package net.joshka.junit.json.params;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.Preconditions;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import java.io.InputStream;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class JsonFileArgumentsProvider implements AnnotationConsumer<JsonFileSource>, ArgumentsProvider {

    private final BiFunction<Class, String, InputStream> inputStreamProvider;

    private String[] resources;

    JsonFileArgumentsProvider() {
        this(Class::getResourceAsStream);
    }

    JsonFileArgumentsProvider(BiFunction<Class, String, InputStream> inputStreamProvider) {
        this.inputStreamProvider = inputStreamProvider;
    }

    private static JsonValue values(InputStream inputStream) {
        try (JsonReader reader = Json.createReader(inputStream)) {
            return reader.read();
        }
    }

    @Override
    public void accept(JsonFileSource jsonFileSource) {
        resources = jsonFileSource.resources();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        boolean isList = stream(context.getRequiredTestMethod().getParameterTypes())
            .anyMatch(List.class::isAssignableFrom);
        return stream(resources)
                .map(resource -> openInputStream(context, resource))
                .map(JsonFileArgumentsProvider::values)
                .flatMap(json -> {
                    if(json.getValueType() == ValueType.ARRAY && !isList){
                        return json.asJsonArray().stream();
                    }
                    return Stream.of(json);
                })
            .map(Arguments::arguments);
    }

    private InputStream openInputStream(ExtensionContext context, String resource) {
        Class<?> testClass = context.getRequiredTestClass();
        InputStream inputStream = inputStreamProvider.apply(testClass, resource);
        return Preconditions.notNull(inputStream,
                () -> "Classpath resource does not exist: " + resource);
    }
}
