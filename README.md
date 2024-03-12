# junit-json-params

A [Junit 5](http://junit.org/junit5/) library to provide annotations that load
data from JSON Strings or files in parameterized tests.

## Project Info

[![Maven Central Version](https://img.shields.io/maven-central/v/net.joshka/junit-json-params?style=for-the-badge)](https://central.sonatype.com/artifact/net.joshka/junit-json-params)
[![Javadocs](https://javadoc.io/badge/net.joshka/junit-json-params.svg?style=for-the-badge)](https://javadoc.io/doc/net.joshka/junit-json-params)
[![GitHub License](https://img.shields.io/github/license/joshka/junit-json-params?style=for-the-badge)](./LICENSE.txt)  \
[![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/joshka/junit-json-params/gradle.yml?style=for-the-badge)](https://github.com/joshka/junit-json-params/actions/workflows/gradle.yml)
[![Sonar Quality Gate](https://img.shields.io/sonar/quality_gate/joshka_junit-json-params?server=https%3A%2F%2Fsonarcloud.io&style=for-the-badge)](https://sonarcloud.io/project/overview?id=joshka_junit-json-params)
[![Sonar Coverage](https://img.shields.io/sonar/coverage/joshka_junit-json-params?server=https%3A%2F%2Fsonarcloud.io&style=for-the-badge)](https://sonarcloud.io/component_measures?id=joshka_junit-json-params&metric=coverage&view=list)

## Installation

### Apache Maven

```xml
<dependencies>
    <dependency>
        <groupId>net.joshka</groupId>
        <artifactId>junit-json-params</artifactId>
        <version>5.10.2-r0</version>
    </dependency>
    <dependency>
        <groupId>org.eclipse.parsson</groupId>
        <artifactId>parsson</artifactId>
        <version>1.1.1</version>
    </dependency>
</dependencies>
```

### Gradle

```groovy
testImplementation 'net.joshka:junit-json-params:5.10.2-r0'
testImplementation 'org.eclipse.parsson:parsson:1.1.1'
```

## Examples

### `@JsonSource`

`@JsonSource` allows you to specify argument lists as JSON strings.

See [`JsonArgumentsProviderTest`](https://github.com/joshka/junit-json-params/blob/master/src/test/java/net/joshka/junit/json/params/JsonArgumentsProviderTest.java)

```java
import net.joshka.junit.json.params.JsonSource;

class JsonArgumentsProviderTest {
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
        assertThat(number.intValue()).isPositive();
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

    /**
     * When passed <code>{'key':'value'}</code>, is executed a single time.
     * This simplifies writing inline JSON strings
     * @param object the parsed JsonObject
     */
    @ParameterizedTest
    @JsonSource("{'key':'value'}")
    @DisplayName("handles simplified json")
    void simplifiedJson(JsonObject object) {
        assertThat(object.getString("key")).isEqualTo("value");
    }
}
```

### `@JsonFileSource`

`@JsonFileSource` lets you use JSON files from the classpath. It supports
single objects and arrays of objects and JSON primitives (numbers and strings).

See [`JsonFileArgumentsProviderTest`](https://github.com/joshka/junit-json-params/blob/master/src/test/java/net/joshka/junit/json/params/JsonFileArgumentsProviderTest.java)

```java
import net.joshka.junit.json.params.JsonFileSource;

class JsonFileArgumentsProviderTest {
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
        assertThat(number.intValue()).isPositive();
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
}
```

## License

Copyright ©️ 2019-2022 Joshua McKinney

Code is under the [Apache License 2.0](LICENSE.txt)
