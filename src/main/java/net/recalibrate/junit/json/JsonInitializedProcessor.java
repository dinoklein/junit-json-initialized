package net.recalibrate.junit.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.function.Consumer;

/**
 * Given a test instance, this processor will look for fields with {@link JsonInitialized} annotation, and then take
 * the steps to initialize the field as appropriate.
 */
@AllArgsConstructor
@Slf4j
public class JsonInitializedProcessor implements Consumer<Object> {

    @NonNull
    private final Parser parser;

    /**
     * Takes a provided test instance, iterates over all fields, and initializes those that are annotated
     * with {@link JsonInitialized}.
     *
     * @param testInstance test instance to be processed
     */
    @Override
    @SneakyThrows
    public void accept(Object testInstance) {
        val testClass = testInstance.getClass();
        val fields = FieldUtils.getFieldsWithAnnotation(testClass, JsonInitialized.class);
        log.debug("Found {} fields with matching annotation", fields.length);

        for (val field : fields) {
            log.debug("Processing field '{}'", field.getName());
            JsonInitialized annotation = field.getAnnotation(JsonInitialized.class);
            val value = annotation.value();
            val resourceLocation = annotation.resourceLocation();
            log.debug("Attribute value: '{}'", value);
            log.debug("Attribute resourceLocation: '{}'", resourceLocation);

            verifyAttributesOnAnnotation(value, resourceLocation, field, annotation);

            val fieldClass = field.getType();
            Object obj;
            if (StringUtils.isNotBlank(value)) {
                log.debug("Processing from inline value");
                obj = parser.parseFromString(value, fieldClass);
            } else {
                log.debug("Processing with resource location");
                obj = parser.parseFromResource(resourceLocation, fieldClass);
            }

            log.trace("Read object: {}", obj);
            field.setAccessible(true);
            field.set(testInstance, obj);
        }
    }

    /**
     * Verifies the attributes on {@link JsonInitialized the annotation}, making sure only a single attribute
     * is specified, throwing an exception otherwise.
     *
     * @param value
     * @param resourceLocation
     * @param field
     * @param annotation
     */
    private void verifyAttributesOnAnnotation(String value, String resourceLocation, Field field,
                                              JsonInitialized annotation) {
        if (StringUtils.isNoneBlank(value, resourceLocation) || StringUtils.isAllBlank(value, resourceLocation)) {
            val msg = "Annotation on field %s ought to have only one attribute assigned a value: %s";
            throw new IllegalArgumentException(String.format(msg, field.getName(), annotation));
        }
    }
}
