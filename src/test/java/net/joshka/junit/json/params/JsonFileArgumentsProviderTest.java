package net.joshka.junit.json.params;

import java.util.List;
import net.joshka.junit.json.params.data.TestObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.platform.commons.PreconditionViolationException;

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
     * @param object the parsed object
     */
    @ParameterizedTest
    @JsonFileSource(resources = "/single-object.json", target = TestObject.class)
    @DisplayName("provides a single object")
    void singleObject(TestObject object) {
        assertThat(object.getKey()).isEqualTo("value");
    }

    /**
     * When passed <code>[{"key":"value1"},{"key","value2"}]</code>, is
     * executed once per element of the array
     * @param object the parsed object for each element in array
     */
    @ParameterizedTest
    @JsonFileSource(resources = "/array-of-objects.json", target = TestObject[].class)
    @DisplayName("provides an array of objects")
    void arrayOfObjects(TestObject[] object) {
        System.out.println(object.length);
        //assertThat(object.getKey()).startsWith("value");
    }

    /**
     * When passed <code>[1, 2]</code>, is executed once per array element
     * @param number the parsed integer for each element in array
     */
    @ParameterizedTest
    @JsonFileSource(resources = "/array-of-numbers.json", target = Integer.class)
    @DisplayName("provides an array of numbers")
    void arrayOfNumbers(Integer number) {
        assertThat(number.intValue()).isPositive();
    }

    /**
     * When passed <code>["value1","value2"]</code>, is executed once per array
     * element
     * @param string the parsed string for each element in array
     */
    @ParameterizedTest
    @JsonFileSource(resources = "/array-of-strings.json", target = String.class)
    @DisplayName("provides an array of strings")
    void arrayOfStrings(String string) {
        assertThat(string).startsWith("value");
    }

    /**
     * When passed <code>[{"key":"value1"},{"key","value2"}]</code>
     * and argument is a JsonArray, test is executed only once.
     * @param objects the parsed array of objects
     */
    @ParameterizedTest
    @JsonFileSource(resources = "/array-of-objects.json", target = TestObject[].class)
    void arrayOfTestObject(TestObject[] objects) {
        assertThat(objects).hasSize(2);
    }

    /**
     * When passed <code>[{"key":"value1"},{"key","value2"}]</code>
     * and argument is a List, test is executed only once.
     * @param object the parsed List object
     */
    @ParameterizedTest
    @JsonFileSource(resources = "/array-of-objects.json", target = List.class)
    void listTestObject(List<TestObject> object) {
        assertThat(object).hasSize(2);
    }

    /**
     * When passed <code>[{"key":"value1"},{"key","value2"}]</code>
     * and argument is a List, test is executed only once.
     * @param object the parsed List object
     */
    @ParameterizedTest
    @JsonFileSource(resources = "/array-of-objects.json", target = List.class)
    void listString(List<String> object) {
        assertThat(object).hasSize(2);
    }

    @Test
    @DisplayName("missing resource throws exception")
    void missingResource() throws Exception {
        BiFunction<Class<?>, String, InputStream> inputStreamProvider = (aClass, resource) -> null;
        ExtensionContext context = mock(ExtensionContext.class);
        JsonFileSource source = mock(JsonFileSource.class);
        when(source.resources()).thenReturn(new String[]{"not-found.json"});
        when(context.getRequiredTestMethod()).thenReturn(this.getClass().getDeclaredMethod("missingResource"));
        JsonFileArgumentsProvider provider = new JsonFileArgumentsProvider(inputStreamProvider);
        provider.accept(source);

        assertThatExceptionOfType(PreconditionViolationException.class)
                .isThrownBy(() -> provider.provideArguments(context).forEach(o -> {}))
                .withMessage("Classpath resource does not exist: not-found.json");
    }
}