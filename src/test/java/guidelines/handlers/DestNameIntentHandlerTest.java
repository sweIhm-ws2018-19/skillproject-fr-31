package guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import guidelines.models.Coordinate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class DestNameIntentHandlerTest {
    private DestNameIntentHandler handler;

    @Before
    public void setup() {
        handler = new DestNameIntentHandler();
    }

    @Test
    public void test_Ctor() {
        assertEquals(handler.getClass(), DestNameIntentHandler.class);
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
    public void destNameSlotIsNullTest(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("destCustomName", null);

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertNotEquals("TEST", response.getReprompt());
        assertNotNull(response.getOutputSpeech());
        assertTrue(response.getOutputSpeech().toString().contains("Leider hab ich den Namen nicht verstanden"));
    }

    @Test
    public void destNameSlotAndDestNameNotNullTest(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, Coordinate> stations = new HashMap<>();
        stations.put("bahnhof", new Coordinate(41.222, 11.111));
        stations.put("arbeit", new Coordinate(41.212, 11.171));
        stations.put("Sport", new Coordinate(45.222, 11.411));
        stations.put("Theater", new Coordinate(43.212, 11.876));
        sessionAttributes.put("Stations", stations);
        final Map<String, String> slots = new HashMap<>();
        slots.put("destCustomName", "arbeit");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertNotEquals("TEST", response.getReprompt());
        assertNotNull(response.getOutputSpeech());
    }

    @Test
    public void destAlreadyExistInDbTest(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Object> persistentAttributes = new HashMap<>();
        persistentAttributes.put("DEST", new HashMap<>());
        final Map<String, Coordinate> stations = new HashMap<>();
        stations.put("bahnhof", new Coordinate(41.222, 11.111));
        stations.put("arbeit", new Coordinate(41.212, 11.171));
        stations.put("Sport", new Coordinate(45.222, 11.411));
        stations.put("Theater", new Coordinate(43.212, 11.876));
        sessionAttributes.put("Stations", stations);
        final Map<String, String> slots = new HashMap<>();
        slots.put("destCustomName", "arbeit");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertNotEquals("TEST", response.getReprompt());
        assertNotNull(response.getOutputSpeech());
    }
}
