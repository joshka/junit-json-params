package net.joshka.junit.json.params;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ArgumentsSource(JsonArgumentsProvider.class)
public @interface JsonSource {
    String value();
    Class<?> target() default Object.class;
}
