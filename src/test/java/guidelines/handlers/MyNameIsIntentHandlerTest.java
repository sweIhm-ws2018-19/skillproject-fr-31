package guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import guidelines.statemachine.GuideStates;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MyNameIsIntentHandlerTest {
    private MyNameIsIntentHandler handler;

    @Before
    public void setup(){
        handler = new MyNameIsIntentHandler();
    }

    @Test
    public void test_Ctor() {
        assertEquals(handler.getClass(), MyNameIsIntentHandler.class);
    }

    @Test
    public void canHandleTest(){
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.matches(any())).thenReturn(true);
        assertTrue(handler.canHandle(inputMock));
    }

    @Test
    public void nullHandleTest(){
        assertThrows(NullPointerException.class, () -> handler.handle(null));
    }

    @Test
    public void handleTestSlotNotNull(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("name", "denis");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        assertEquals("denis", MyNameIsIntentHandler.getName());
        final Response response = res.get();

        assertNotEquals("TEST", response.getReprompt());
        assertNotNull(response.getOutputSpeech());
    }

    @Test
    public void handleTestSlotIsNull(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("name", null);

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertFalse(response.getShouldEndSession());
        assertNotEquals("TEST", response.getReprompt());
        assertNotNull(response.getOutputSpeech());
    }
}