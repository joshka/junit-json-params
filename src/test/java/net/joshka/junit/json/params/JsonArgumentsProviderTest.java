package net.joshka.junit.json.params;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class JsonArgumentsProviderTest {

    @Test
    @DisplayName("default constructor does not throw")
    void defaultConstructor() {
        assertThatCode(JsonArgumentsProvider::new)
                .doesNotThrowAnyException();
    }

    /**
     * When passed <code>{"key":"value"}</code>, is executed a single time
     * @param object the parsed JsonObject
     */
    @ParameterizedTest
    @JsonSource("{\"key\":\"value\"}")
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
    @JsonSource("[{\"key\":\"value1\"},{\"key\":\"value2\"}]")
    @DisplayName("provides an array of objects")
    void arrayOfObjects(JsonObject object) {
        assertThat(object.getString("key")).startsWith("value");
    }

    /**
     * When passed <code>[1, 2]</code>, is executed once per array element
     * @param number the parsed JsonNumber for each array element
     */
    @ParameterizedTest
    @JsonSource("[1,2]")
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
    @JsonSource("[\"value1\",\"value2\"]")
    @DisplayName("provides an array of strings")
    void arrayOfStrings(JsonString string) {
        assertThat(string.getString()).startsWith("value");
    }

    @ParameterizedTest
    @JsonSource("{'key':'value'}")
    @DisplayName("handles simplified json")
    void simplifiedJson(JsonObject object) {
        assertThat(object.getString("key")).isEqualTo("value");
    }
}