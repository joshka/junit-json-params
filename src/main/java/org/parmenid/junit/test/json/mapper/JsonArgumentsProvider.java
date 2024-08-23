package org.parmenid.junit.test.json.mapper;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.Preconditions;

import java.io.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class JsonArgumentsProvider implements AnnotationConsumer<TestJsonMapper>, ArgumentsProvider {
    private final BiFunction<Class<?>, String, InputStream> inputStreamProvider;
    private String[] resources;

    @Override
    public void accept(TestJsonMapper testJsonMapper) {
        this.resources = testJsonMapper.resources();
    }

    JsonArgumentsProvider() {
        this(Class::getResourceAsStream);
    }

    JsonArgumentsProvider(BiFunction<Class<?>, String, InputStream> inputStreamProvider) {
        this.inputStreamProvider = inputStreamProvider;
    }

    private static String values(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(inputStream);
        int value = 0;
        while (true) {
            try {
                if ((value = reader.read()) == - 1) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            output.append((char) value);
        }
    return output.toString();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        Class<?>[] parameterTypes = context.getRequiredTestMethod().getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new IllegalArgumentException("Arguments count: " + parameterTypes.length + ". JsonMapper supports only one as test's arguments");
        }
        Stream<?> jsonStream = JsonMapperUtil.resourceIsFile(this.resources)
                ? fromFile(context) : fromString();
            return jsonStream
                .flatMap(item -> {
                    String json = (String) item;
                    if (JsonMapperUtil.isArray(json)) {
                        if (JsonMapperUtil.isArrayOrCollection(parameterTypes[0])) {
                            return Stream.of(JsonConverter.convert(json, parameterTypes[0]));
                        } else {
                            String[] items =  JsonMapperUtil.isObject(json)
                                    ? JsonMapperUtil.extractJsonObjects(json)
                                    : JsonMapperUtil.extractJsonPrimitives(json);
                            return stream(items)
                                    .filter(obj -> !obj.isBlank())
                                    .map(obj -> {
                                        if (obj.contains("{") && !obj.contains("}")) {
                                            obj += "}";
                                        }
                                        return JsonConverter.convert(obj.trim(), parameterTypes[0]);
                                    });
                        }
                    }
                    return Stream.of(json)
                            .map(obj -> JsonConverter.convert(obj, parameterTypes[0]));
                })
            .map(Arguments::arguments);
    }


    private InputStream openInputStream(ExtensionContext context, String resource) {
        Class<?> testClass = context.getRequiredTestClass();
        InputStream inputStream = inputStreamProvider.apply(testClass, resource);
        return Preconditions.notNull(inputStream,
            () -> "Classpath resource does not exist: " + resource);
    }

    private Stream<?> fromFile(ExtensionContext context) {
        return stream(this.resources)
                .map(resource -> openInputStream(context, resource))
                .map(JsonArgumentsProvider::values);
    }

    private Stream<?> fromString() {
        return stream(this.resources);
    }
}
