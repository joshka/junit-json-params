package net.joshka.junit.json.params;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.platform.commons.PreconditionViolationException;

import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import java.io.InputStream;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonFileArgumentsProviderTest {

    @Test
    @DisplayName("default constructor does not throw")
    void defaultConstructor() {
        assertThatCode(JsonFileArgumentsProvider::new)
                .doesNotThrowAnyException();
    }

    /**
     * When passed <code>{"key":"value"}</code>, is executed a single time
     * @param object the parsed JsonObject
     */
    @ParameterizedTest
    @JsonFileSource(resources = "/single-object.json")
    @DisplayName("provides a single object")
    void singleObject(JsonObject object) {
        assertThat(object.getString("key")).isEqualTo("value");
    }

    /**
     * When passed <code>[{"key":"value1"},{"key","value2"}]</code>, is
     * executed once per element of the array
     * @param object the parsed JsonObject array element
     */
    @ParameterizedTest
    @JsonFileSource(resources = "/array-of-objects.json")
    @DisplayName("provides an array of objects")
    void arrayOfObjects(JsonObject object) {
        assertThat(object.getString("key")).startsWith("value");
    }

    /**
     * When passed <code>[1, 2]</code>, is executed once per array element
     * @param number the parsed JsonNumber for each array element
     */
    @ParameterizedTest
    @JsonFileSource(resources = "/array-of-numbers.json")
    @DisplayName("provides an array of numbers")
    void arrayOfNumbers(JsonNumber number) {
        assertThat(number.intValue()).isGreaterThan(0);
    }

    /**
     * When passed <code>["value1","value2"]</code>, is executed once per array
     * element
     * @param string the parsed JsonString for each array element
     */
    @ParameterizedTest
    @JsonFileSource(resources = "/array-of-strings.json")
    @DisplayName("provides an array of strings")
    void arrayOfStrings(JsonString string) {
        assertThat(string.getString()).startsWith("value");
    }

    @Test
    @DisplayName("missing resource throws exception")
    void missingResource() {
        BiFunction<Class, String, InputStream> inputStreamProvider = (aClass, resource) -> null;
        ExtensionContext context = mock(ExtensionContext.class);
        JsonFileSource source = mock(JsonFileSource.class);
        when(source.resources()).thenReturn(new String[]{"not-found.json"});
        JsonFileArgumentsProvider provider = new JsonFileArgumentsProvider(inputStreamProvider);
        provider.accept(source);

        assertThatExceptionOfType(PreconditionViolationException.class)
                .isThrownBy(() -> provider.provideArguments(context).forEach(o -> {}))
                .withMessage("Classpath resource does not exist: not-found.json");
    }
}