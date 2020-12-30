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
class ParserTest {

    private static final String JSON_RECORD_PATH = "/net/recalibrate/junit/json/record.json";
    private static final String JSON_RECORD_STRING = "{\"name\":\"George Washington\", \"age\":200}";

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private Parser parser;

    @Test
    void parseFromString() throws IOException {
        /* arrange */
        DummyRecord mockRecord = Mockito.mock(DummyRecord.class);
        Mockito.when(mapper.readValue(Mockito.eq(JSON_RECORD_STRING), Mockito.eq(DummyRecord.class)))
                .thenReturn(mockRecord);
        /* act */
        Object record = parser.parseFromString(JSON_RECORD_STRING, DummyRecord.class);
        /* assert */
        assertSame(mockRecord, record);
    }

    @Test
    void parseFromResource() throws IOException {
        /* arrange */
        DummyRecord mockRecord = Mockito.mock(DummyRecord.class);
        Mockito.when(mapper.readValue(Mockito.any(InputStream.class), Mockito.eq(DummyRecord.class)))
                .thenReturn(mockRecord);
        /* act */
        Object record = parser.parseFromResource(JSON_RECORD_PATH, DummyRecord.class);
        /* assert */
        assertSame(mockRecord, record);
    }
}