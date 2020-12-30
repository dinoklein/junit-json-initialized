package net.recalibrate.junit.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JsonInitializedProcessor_ValidUsageTest {

    private static final String FIXED_FIELD = "This is a test";
    private static final String JSON_RECORD_PATH = "/net/recalibrate/junit/json/record.json";
    private static final String JSON_RECORD_STRING = "{\"name\":\"George Washington\", \"age\":200}";


    /** Test fixture with both usages of the annotation and a field without annotation. */
    private static class TestFixture {
        String nonInitializedField = FIXED_FIELD;

        @JsonInitialized(resourceLocation = JSON_RECORD_PATH)
        DummyRecord fileInitializedRecord;

        @JsonInitialized(value = JSON_RECORD_STRING)
        DummyRecord stringInitializedRecord;
    }


    @Mock
    private Parser parser;

    @InjectMocks
    private JsonInitializedProcessor processor;

    @Test
    void accept() throws IOException {
        /* arrange */
        DummyRecord mockRecord1 = Mockito.mock(DummyRecord.class);
        Mockito.when(parser.parseFromResource(Mockito.eq(JSON_RECORD_PATH), Mockito.same(DummyRecord.class)))
                .thenReturn(mockRecord1);
        DummyRecord mockRecord2 = Mockito.mock(DummyRecord.class);
        Mockito.when(parser.parseFromString(Mockito.eq(JSON_RECORD_STRING), Mockito.same(DummyRecord.class)))
                .thenReturn(mockRecord2);
        TestFixture testFixture = new TestFixture();
        /* act */
        processor.accept(testFixture);
        /* assert */
        assertSame(FIXED_FIELD, testFixture.nonInitializedField);
        assertSame(mockRecord1, testFixture.fileInitializedRecord);
        assertSame(mockRecord2, testFixture.stringInitializedRecord);
    }
}
