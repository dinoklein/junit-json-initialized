# junit-json-initialized
A [JUnit 5](https://junit.org/junit5/) extension that initializes fields with JSON content using the
[Jackson library](https://github.com/FasterXML/jackson). Fields are initialized from either an inline
JSON string, or from a file, but only when said fields are annotated with @JsonInitialized.

The impetus behind this extension was to be able to make use of
Project Lombok's [@Jacksonized annotation](https://projectlombok.org/features/experimental/Jacksonized)
so one may have a clean design of a class with final fields, resulting in immutable objects, while being
able to easily initialize test fixtures.

## Installation
### Apache Maven
```xml
<dependency>
    <groupId>net.recalibrate</groupId>
    <artifactId>junit-json-initialized</artifactId>
    <version>1.0.0</version>
</dependency>
```
### Gradle
```groovy
compile 'net.recalibrate:junit-json-initialized:1.0.0'
```

## Examples
### Inline JSON String
Annotate your test class with `@ExtendWith(JsonProvider.class)` to enable processing, and then your fields
with `@JsonInitialized` and set the attribute _value_ to whatever JSON string you desire. See the example
below showing usage; an inner class is used below for sake of having a self-contained example.

```java
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import net.recalibrate.junit.json.JsonInitialized;
import net.recalibrate.junit.json.JsonProvider;
import org.junit.jupiter.api.Test;

@ExtendWith(JsonProvider.class)
@Slf4j
public class ExampleTest {
    @Jacksonized
    @Data
    private static class Record {
        private final String name;
        private final int age;
    }

    @JsonInitialized(value = "{ \"name\": \"John Smith\", \"age\": 50 }")
    private Record record;

    @Test
    public void test() {
        log.info("Record: {}", record);
    }
}
```

### External JSON Record
Starting with the example above, in order to utilize JSON from a file, simply place the file somewhere in your
_resources_ directory, and then alter the annotation above to something like this: (repetitive details removed)
```java
public class ExampleTest {
    @JsonInitialized(resourceLocation = "/path/record.json")
    private Record record;
}
```

## Acknowledgement
I wish to acknowledge Josh McKinney's [junit-json-params](https://github.com/joshka/junit-json-params) library: I've
stumbled upon it while looking around for something to use, but, as it is geared towards parameterized tests, as well
as the fact that I wanted to make use of Jackson, I decided to write this extension.

## License
Copyright 2021 Dino Klein
Code is under the Apache License 2.0