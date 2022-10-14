# junit-json-params

A [Junit 5](http://junit.org/junit5/) library to provide annotations that load
data from JSON Strings or files in parameterized tests.

## Project Info
[Project site](http://www.joshka.net/junit-json-params)

[![Build Status](https://app.travis-ci.com/joshka/junit-json-params.svg?branch=master)](https://app.travis-ci.com/joshka/junit-json-params)
[![Maven Central](https://img.shields.io/maven-central/v/net.joshka/junit-json-params.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.joshka%22%20AND%20a:%22junit-json-params%22)
[![Javadocs](https://javadoc.io/badge/net.joshka/junit-json-params.svg)](https://javadoc.io/doc/net.joshka/junit-json-params)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=net.joshka%3Ajunit-json-params&metric=alert_status)](https://sonarcloud.io/dashboard/index/net.joshka:junit-json-params)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=net.joshka%3Ajunit-json-params&metric=coverage)](https://sonarcloud.io/component_measures?id=net.joshka:junit-json-params&metric=coverage)
[![License](https://img.shields.io/github/license/joshka/junit-json-params.svg)](blob/master/LICENSE.txt)
[![Dependabot Status](https://api.dependabot.com/badges/status?host=github&repo=joshka/junit-json-params)](https://dependabot.com)

## Installation

### Apache Maven
```xml
<dependency>
    <groupId>net.joshka</groupId>
    <artifactId>junit-json-params</artifactId>
    <version>5.6.2-r1</version>
</dependency>
```

### Gradle
```groovy
compile 'net.joshka:junit-json-params:5.6.2-r1'
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
}
```

## License
Copyright 2019 Joshua McKinney

Code is under the [Apache License 2.0](LICENSE.txt)
