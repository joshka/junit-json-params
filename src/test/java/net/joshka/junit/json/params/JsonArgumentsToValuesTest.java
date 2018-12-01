package net.joshka.junit.json.params;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonArgumentsToValuesTest {
    @ParameterizedTest
    @JsonSource("{'key':'value'}")
    @DisplayName("Converts to Strings")
    void strings(@ConvertWith(JsonConverter.class) String key) {
        assertEquals("value", key);
    }

    @ParameterizedTest
    @JsonSource("{'key': true}")
    @DisplayName("Converts to booleans")
    void ints(@ConvertWith(JsonConverter.class) boolean key) {
        assertTrue(key);
    }


    @ParameterizedTest
    @JsonSource("{'key': 1 }")
    @DisplayName("Converts to ints")
    void booleans(@ConvertWith(JsonConverter.class) int key) {
        assertEquals(1, key);
    }

    @ParameterizedTest
    @Disabled("Doesn't yet work as only the first parameter gets a value")
    @JsonSource("{'stringKey':'value', 'boolKey': true, 'intKey': 1 }")
    @DisplayName("provides multiple objects")
    void multipleObjects(
            @ConvertWith(JsonConverter.class) String stringKey,
            @ConvertWith(JsonConverter.class) int intKey,
            @ConvertWith(JsonConverter.class) boolean boolKey) {
        assertEquals("value", stringKey);
        assertTrue(boolKey);
        assertEquals(1, intKey);
    }
}
