package guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import guidelines.models.Coordinate;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class DestChoiceIntentHandlerTest {
    private DestChoiceIntentHandler handler;

    @Before
    public void setup() {
        handler = new DestChoiceIntentHandler();
    }

    @Test
    public void test_Ctor() {
        assertEquals(handler.getClass(), DestChoiceIntentHandler.class);
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
    public void slotNotNullHandleTest(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Coordinate> stations = new HashMap<>();
        stations.put("bahnhof", new Coordinate(41.222, 11.111));
        stations.put("arbeit", new Coordinate(41.212, 11.171));
        ArrayList<String> stationNames = new ArrayList<>(stations.keySet());
        sessionAttributes.put("Stations", stations);
        sessionAttributes.put(GuideStates.STATE.getKey(), GuideStates.SELECT_NEARBY_STATION);
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("choice", "1");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertFalse(response.getShouldEndSession());
        assertNotNull(response.getOutputSpeech());
        assertNotEquals("test", response.getOutputSpeech());
        assertTrue(response.getOutputSpeech().toString().contains(stationNames.get(0)));
    }
}
