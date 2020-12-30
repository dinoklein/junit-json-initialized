package net.recalibrate.junit.json;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class JsonProviderTest {

    @Mock
    private ExtensionContext extensionContext;
    @Mock
    private ExtensionContext.Store store;
    @Mock
    private JsonInitializedProcessor processor;

    @InjectMocks
    private JsonProvider provider;

    @BeforeEach
    void setUp() {
    }

    @Test
    void beforeAll() throws Exception {
        /* arrange */
        Mockito.when(extensionContext.getStore(Mockito.eq(JsonProvider.NAMESPACE))).thenReturn(store);
        Mockito.doNothing().when(store)
                .put(Mockito.eq(JsonProvider.JIP_KEY), Mockito.any(JsonInitializedProcessor.class));
        /* act */
        provider.beforeAll(extensionContext);
        /* assert */
        Mockito.verifyNoMoreInteractions(extensionContext, store, processor);
    }

    @Test
    void beforeEach() throws Exception {
        /* arrange */
        Object fakeTestInstance = new Object();
        Mockito.when(extensionContext.getTestInstance()).thenReturn(Optional.of(fakeTestInstance));
        Mockito.when(extensionContext.getStore(Mockito.eq(JsonProvider.NAMESPACE))).thenReturn(store);
        Mockito.when(store.get(JsonProvider.JIP_KEY, JsonInitializedProcessor.class)).thenReturn(processor);
        Mockito.doNothing().when(processor).accept(Mockito.same(fakeTestInstance));
        /* act */
        provider.beforeEach(extensionContext);
        /* assert */
        Mockito.verifyNoMoreInteractions(extensionContext, store, processor);
    }
}
