package net.recalibrate.junit.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Extension to be used with JUnit Jupiter via {@link org.junit.jupiter.api.extension.ExtendWith} on your tests.
 * This will enable the processing of fields annotated with {@link JsonInitialized}, and will initialize said fields
 * with
 */
@Slf4j
public class JsonProvider implements BeforeAllCallback, BeforeEachCallback {

    @VisibleForTesting
    static final String JIP_KEY = "json-initialized-processor";

    @VisibleForTesting
    static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(JsonProvider.class.getPackage().getName());

    /**
     * Initializes our environment by setting up processor to be used on all test instances.
     *
     * @param context JUnit 5 extension context
     * @throws Exception per interface spec
     */
    @Override
    @SuppressWarnings("unusedThrown")
    public void beforeAll(ExtensionContext context) throws Exception {
        val store = context.getStore(NAMESPACE);
        val mapper = new ObjectMapper();
        val parser = new Parser(mapper);
        store.put(JIP_KEY, new JsonInitializedProcessor(parser));
    }

    /** Utilizes the processor setup in {@link #beforeAll(org.junit.jupiter.api.extension.ExtensionContext)}
     * to process a test.
     *
     * @param context JUnit 5 extension context
     * @throws Exception per interface spec
     */
    @Override
    @SuppressWarnings("unusedThrown")
    public void beforeEach(ExtensionContext context) throws Exception {
        val jip = context.getStore(NAMESPACE).get(JIP_KEY, JsonInitializedProcessor.class);
        jip.accept(context.getTestInstance().get());
    }
}
