package org.parmenid.junit.test.json.mapper;

import java.io.InputStream;
import java.util.List;
import java.util.function.BiFunction;

import org.parmenid.junit.test.json.mapper.data.TestObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.PreconditionViolationException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonArgumentsProviderTest {

    @Test
    @DisplayName("default constructor does not throw")
    void defaultConstructor() {
        assertThatCode(JsonArgumentsProvider::new)
                .doesNotThrowAnyException();
    }

    /**
     * When passed <code>{"key":"value"}</code>, is executed a single time
     * @param object the mapped Object from file
     */
    @TestJsonMapper(resources = "/single-object.json")
    @DisplayName("provides a single object")
    void singleObject(TestObject object) {
        assertThat(object.getKey()).isEqualTo("value");
    }

    /**
     * When passed <code>[{"key":"value1"},{"key","value2"}]</code>, is
     * executed once per element of the array
     * @param object the mapped to Object array element from file
     */
    @TestJsonMapper(resources = "/array-of-objects.json")
    @DisplayName("provides an array of objects")
    void arrayOfObjects(TestObject object) {
        assertThat(object.getKey()).startsWith("value");
    }

    /**
     * When passed <code>[1, 2]</code>, is executed once per array element
     * @param number the mapped Number for each array element from file
     */
    @ParameterizedTest
    @TestJsonMapper(resources = "/array-of-numbers.json")
    @DisplayName("provides an array of numbers")
    void arrayOfNumbers(Integer number) {
        assertThat(number.intValue()).isPositive();
    }

    /**
     * When passed <code>[{"key":"value1"},{"key","value2"}]</code>
     * and argument is a JsonArray, test is executed only once.
     * @param objects the parsed array of objects
     */
    @TestJsonMapper(resources = "/array-of-objects.json")
    void jsonArray(TestObject[] objects) {
        assertThat(objects).hasSize(2);
    }

    /**
     * When passed <code>[{"key":"value1"},{"key","value2"}]</code>
     * and argument is a List, test is executed only once.
     * @param objects the parsed List object
     */
    @TestJsonMapper(resources = "/array-of-objects.json")
    void listJsonObject(List<TestObject> objects) {
        assertThat(objects).hasSize(2);
    }

    /**
     * When passed <code>[{"key":"value1"},{"key","value2"}]</code>
     * and argument is a List, test is executed only once.
     * @param object the parsed List object
     */
    @TestJsonMapper(resources = "/array-of-objects.json")
    void listString(List<String> object) {
        assertThat(object).hasSize(2);
    }

    @ValueSource(strings = "test param")
    @DisplayName("missing resource throws exception")
    @ParameterizedTest
    void missingResource(String param) throws Exception {
        BiFunction<Class<?>, String, InputStream> inputStreamProvider = (aClass, resource) -> null;
        ExtensionContext context = mock(ExtensionContext.class);
        TestJsonMapper source = mock(TestJsonMapper.class);
        when(source.resources()).thenReturn(new String[]{"not-found.json"});
        when(context.getRequiredTestMethod()).thenReturn(this.getClass().getDeclaredMethod("missingResource", String.class));
        JsonArgumentsProvider provider = new JsonArgumentsProvider(inputStreamProvider);
        provider.accept(source);

        assertThatExceptionOfType(PreconditionViolationException.class)
                .isThrownBy(() -> provider.provideArguments(context).forEach(o -> {}))
                .withMessage("Classpath resource does not exist: not-found.json");
    }

    @Test
    void wrongArgumentsCount() throws Exception {
        BiFunction<Class<?>, String, InputStream> inputStreamProvider = (aClass, resource) -> null;
        ExtensionContext context = mock(ExtensionContext.class);
        TestJsonMapper source = mock(TestJsonMapper.class);
        when(source.resources()).thenReturn(new String[]{"/test.json"});
        when(context.getRequiredTestMethod()).thenReturn(this.getClass().getDeclaredMethod("wrongArgumentsCount"));
        JsonArgumentsProvider provider = new JsonArgumentsProvider(inputStreamProvider);
        provider.accept(source);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> provider.provideArguments(context).forEach(o -> {}))
                .withMessage("Arguments count: 0. JsonMapper supports only one as test's arguments");
    }

    /**
     * When passed <code>{"key":"value"}</code>, is executed a single time
     * @param object the mapped Object from string
     */
    @TestJsonMapper(resources = "{\"key\":\"value\"}")
    public void singleObjectFroString(TestObject object) {
        assertThat(object.getKey()).isEqualTo("value");
    }

    /**
     * When passed <code>[{"key":"value1"},{"key","value2"}]</code>, is
     * executed once per element of the array
     * @param object the mapped to Object array element from file
     */
    @TestJsonMapper(resources = "[{\"key\":\"value1\"}, {\"key\":\"value2\"}]")
    @DisplayName("provides an array of objects")
    void arrayOfObjectsFromString(TestObject object) {
        assertThat(object.getKey()).startsWith("value");
    }

    /**
     * When passed <code>[1, 2]</code>, is executed once per array element
     * @param number the mapped Number for each array element from file
     */
    @TestJsonMapper(resources = "[1,2]")
    @DisplayName("provides an array of numbers")
    void arrayOfNumbersFromString(Integer number) {
        assertThat(number.intValue()).isPositive();
    }

    @TestJsonMapper(resources = "[{\"key\":\"value1\"}, {\"key\":\"value2\"}]")
    void jsonArrayFromString(TestObject[] objects) {
        assertThat(objects).hasSize(2);
    }
}