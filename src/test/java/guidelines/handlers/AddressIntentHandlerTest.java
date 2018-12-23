package guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import guidelines.SpeechStrings;
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

public class AddressIntentHandlerTest {
    private AddressIntentHandler handler;

    @Before
    public void setup() {
        handler = new AddressIntentHandler();
    }

    @Test
    public void test_Ctor() {
        assertEquals(handler.getClass(), AddressIntentHandler.class);
    }


    @Test
    public void canHandleTest() {
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.matches(any())).thenReturn(true);
        assertTrue(handler.canHandle(inputMock));
    }

    @Test
    public void nullHandleTest(){
        assertThrows(NullPointerException.class, () -> handler.handle(null));
    }

    @Test
    public void handleTest(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(GuideStates.STATE.getKey(), GuideStates.GET_DEST_ADDR);
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("street", "lothstraße");
        slots.put("streetNumber", "148");
        slots.put("city", "München");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertFalse(response.getShouldEndSession());
        assertNotEquals("TEST", response.getReprompt());
        assertNotNull(response.getOutputSpeech());
    }

    @Test
    public void coordinatesNullTest(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(GuideStates.STATE.getKey(), GuideStates.GET_DEST_ADDR);
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("street", "lothstraße");
        slots.put("streetNumber", "3000");
        slots.put("city", "HierStehtEtwasSchwachsinniges");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertFalse(response.getShouldEndSession());
        assertNotEquals("TEST", response.getReprompt());
        assertNotNull(response.getOutputSpeech());
    }

    @Test
    public void stateIsGetHomeAddrTest(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(GuideStates.STATE.getKey(), GuideStates.GET_HOME_ADDR);
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("street", "lothstraße");
        slots.put("streetNumber", "148");
        slots.put("city", "München");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertFalse(response.getShouldEndSession());
        assertNotEquals("TEST", response.getReprompt());
        assertNotNull(response.getOutputSpeech());
    }

    @Test
    public void slotNullTest(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(GuideStates.STATE.getKey(), GuideStates.GET_HOME_ADDR);
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("street", null);
        slots.put("streetNumber", null);
        slots.put("city", null);

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertNotEquals("TEST", response.getReprompt());
        assertNotNull(response.getOutputSpeech());
    }
}
