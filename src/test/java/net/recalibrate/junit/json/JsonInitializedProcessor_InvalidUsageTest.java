package net.recalibrate.junit.json;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JsonInitializedProcessor_InvalidUsageTest {

    /** Test fixture with no attributes on annotation. */
    static class TestFixture1 {
        @JsonInitialized
        DummyRecord record;
    }

    /** Test fixture with both attributes on annotation. */
    static class TestFixture2 {
        @JsonInitialized(value = "some-json", resourceLocation = "some-path")
        DummyRecord record;
    }

    @Mock
    private Parser parser;

    @InjectMocks
    private JsonInitializedProcessor processor;

    @ParameterizedTest
    @ValueSource(classes = {TestFixture1.class, TestFixture2.class})
    void accept(Class<?> clazz) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object testFixture = clazz.getDeclaredConstructor().newInstance();
        assertThrows(IllegalArgumentException.class, () -> processor.accept(testFixture));
    }
}
