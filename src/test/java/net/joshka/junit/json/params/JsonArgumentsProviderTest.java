package net.joshka.junit.json.params;

import net.joshka.junit.json.params.data.TestObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import jakarta.json.stream.JsonParsingException;
import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.*;

class JsonArgumentsProviderTest {

    @Test
    @DisplayName("default constructor does not throw")
    void defaultConstructor() {
        assertThatCode(JsonArgumentsProvider::new)
                .doesNotThrowAnyException();
    }

    /**
     * When passed <code>{"key":"value"}</code>, is executed a single time
     * @param object the parsed object
     */
    @ParameterizedTest
    @JsonSource(value = "{\"key\":\"value\"}", target = TestObject.class)
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
    @JsonSource(value = "[{\"key\":\"value1\"},{\"key\":\"value2\"}]", target = TestObject.class)
    @DisplayName("provides an array of objects")
    void arrayOfObjects(TestObject object) {
        assertThat(object.getKey()).startsWith("value");
    }

    /**
     * When passed <code>[1, 2]</code>, is executed once per array element
     * @param number the parsed Integer for each element in array
     */
    @ParameterizedTest
    @JsonSource(value = "[1,2]", target = Integer.class)
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
    @JsonSource(value = "[\"value1\",\"value2\"]", target = String.class)
    @DisplayName("provides an array of strings")
    void arrayOfStrings(String string) {
        assertThat(string).startsWith("value");
    }

    @ParameterizedTest
    @JsonSource(value = "{'key':'value'}", target = TestObject.class)
    @DisplayName("handles simplified json")
    void simplifiedJson(TestObject object) {
        assertThat(object.getKey()).isEqualTo("value");
    }

    @DisplayName("handles invalid json")
    @Test
    void invalidJson() {
        JsonSource invalidJsonSource = new JsonSource() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonSource.class;
            }

            @Override
            public String value() {
                return "notJson";
            }

            @Override
            public Class<?> target() {
                return Object.class;
            }

        };
        JsonArgumentsProvider args = new JsonArgumentsProvider();
        args.accept(invalidJsonSource);

        assertThatExceptionOfType(JsonParsingException.class)
                .isThrownBy(() -> args.provideArguments(null));
    }
}