package net.recalibrate.junit.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used on fields to indicate that the field is to be initialized from a JSON record, whether
 * specified inline, or via an external file; note that only one option may be chosen.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonInitialized {
    /** Attribute for specifying JSON text, which will be used in building the annotated field. */
    String value() default "";

    /** Attribute for specifying the resource where the JSON resides, to be used in building the annotated field. */
    String resourceLocation() default "";
}
