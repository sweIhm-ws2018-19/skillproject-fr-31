package guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import guidelines.SpeechStrings;
import guidelines.models.Coordinate;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ConfirmationHandlerTest {
    private ConfirmationHandler handler;

    @Before
    public void setup() {
        handler = new ConfirmationHandler();
    }

    @Test
    public void test_Ctor() {
        assertEquals(handler.getClass(), ConfirmationHandler.class);
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
    public void handleTestSlotYes(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(GuideStates.STATE.getKey(), GuideStates.SAY_DEST_ADDR_AGAIN);
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("decision", "ja");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();


        assertFalse(response.getShouldEndSession());
        assertNotNull(response.getOutputSpeech());
        assertNotEquals("test", response.getReprompt());
        assertTrue(response.getOutputSpeech().toString().contains("Alles klar. Bitte sag mir nochmal die Strasse, Hausnummer und Stadt"));
    }

    @Test
    public void handleTestSlotNo(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Coordinate> stations = new HashMap<>();
        stations.put("bahnhof", new Coordinate(41.222, 11.111));
        stations.put("arbeit", new Coordinate(41.212, 11.171));
        sessionAttributes.put(GuideStates.STATE.getKey(), GuideStates.SAY_DEST_ADDR_AGAIN);
        sessionAttributes.put("Stations", stations);
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("decision", "nein");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();


        assertFalse(response.getShouldEndSession());
        assertNotNull(response.getOutputSpeech());
        assertNotEquals("test", response.getReprompt());
        String stationsToSelect = StringUtils.prepStringForChoiceIntent(new ArrayList<>(stations.keySet()));
        assertTrue(response.getOutputSpeech().toString().contains(stationsToSelect));
    }

    @Test
    public void handleTestSlotYesQNextAddressState(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(GuideStates.STATE.getKey(), GuideStates.Q_NEXT_ADDR);
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("decision", "ja");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();


        assertFalse(response.getShouldEndSession());
        assertNotNull(response.getOutputSpeech());
        assertNotEquals("test", response.getReprompt());
        assertTrue(response.getOutputSpeech().toString().contains(SpeechStrings.FOLLOWING_ADDRESSES));
    }

    @Test
    public void handleTestSlotNoQNextAddressState(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Coordinate> stations = new HashMap<>();
        stations.put("bahnhof", new Coordinate(41.222, 11.111));
        stations.put("arbeit", new Coordinate(41.212, 11.171));
        sessionAttributes.put(GuideStates.STATE.getKey(), GuideStates.Q_NEXT_ADDR);
        sessionAttributes.put("Stations", stations);
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("decision", "nein");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();


        assertFalse(response.getShouldEndSession());
        assertNotNull(response.getOutputSpeech());
        assertNotEquals("test", response.getReprompt());
        assertTrue(response.getOutputSpeech().toString().contains("Du kannst nun die Hilfefunktion aufrufen, eine Route erfragen oder die Konfiguration starten"));
    }

    @Test
    public void handleTestSlotNoConfigState(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Coordinate> stations = new HashMap<>();
        stations.put("bahnhof", new Coordinate(41.222, 11.111));
        stations.put("arbeit", new Coordinate(41.212, 11.171));
        sessionAttributes.put(GuideStates.STATE.getKey(), GuideStates.CONFIG);
        sessionAttributes.put("Stations", stations);
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("decision", "nein");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();


        assertFalse(response.getShouldEndSession());
        assertNotNull(response.getOutputSpeech());
        assertNotEquals("test", response.getReprompt());
        assertTrue(response.getOutputSpeech().toString().contains(SpeechStrings.NEW_CONFIG));
    }

    @Test
    public void handleTestSlotYesConfigState(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(GuideStates.STATE.getKey(), GuideStates.CONFIG);
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("decision", "ja");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();


        assertFalse(response.getShouldEndSession());
        assertNotNull(response.getOutputSpeech());
        assertNotEquals("test", response.getReprompt());
        assertTrue(response.getOutputSpeech().toString().contains(SpeechStrings.FOLLOWING_ADDRESSES));
    }

    @Test
    public void handleTestSlotNoNewConfigState(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Coordinate> stations = new HashMap<>();
        stations.put("bahnhof", new Coordinate(41.222, 11.111));
        stations.put("arbeit", new Coordinate(41.212, 11.171));
        sessionAttributes.put(GuideStates.STATE.getKey(), GuideStates.NEW_CONFIG);
        sessionAttributes.put("Stations", stations);
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("decision", "nein");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();


        assertFalse(response.getShouldEndSession());
        assertNotNull(response.getOutputSpeech());
        assertNotEquals("test", response.getReprompt());
    }

    @Test
    public void handleTestSlotYesNewConfigState(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(GuideStates.STATE.getKey(), GuideStates.NEW_CONFIG);
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("decision", "ja");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();


        assertFalse(response.getShouldEndSession());
        assertNotNull(response.getOutputSpeech());
        assertNotEquals("test", response.getReprompt());
    }

    @Test
    public void slotNullTest(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("decision", null);

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertNotNull(response.getOutputSpeech());
        assertTrue(response.getOutputSpeech().toString().contains("DIE BEFUELLUNG HAT NICHT GEKLAPPT"));
    }
}
