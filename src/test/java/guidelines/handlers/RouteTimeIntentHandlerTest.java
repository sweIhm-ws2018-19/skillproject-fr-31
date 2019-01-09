package guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import guidelines.models.Coordinate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class RouteTimeIntentHandlerTest {
    private RouteTimeIntentHandler handler;

    @Before
    public void setup(){
        handler = new RouteTimeIntentHandler();
    }

    @Test
    public void test_Ctor() {
        assertEquals(handler.getClass(),  RouteTimeIntentHandler.class);
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
    public void timeSlotNullTest(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();
        slots.put("time", null);

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertNotNull(response.getOutputSpeech());
        assertTrue(response.getOutputSpeech().toString().contains("Bei der Eingabe der Uhrzeit hat etwas nicht geklappt"));
    }

    @Test
    @Ignore
    public void timeSlotNotNullTest(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, BigDecimal> homeAddress = new HashMap<>();
        homeAddress.put("latitude", BigDecimal.valueOf(42.121312));
        homeAddress.put("longitude", BigDecimal.valueOf(12.222222));
        final Map<String, Object> destAddresses = new HashMap<>();
        final Map<String, BigDecimal> uniMap = new HashMap<>();
        uniMap.put("latitude", BigDecimal.valueOf(42.23232));
        uniMap.put("longitude", BigDecimal.valueOf(12.93123));
        final Map<String, BigDecimal> arbeitMap = new HashMap<>();
        uniMap.put("latitude", BigDecimal.valueOf(42.23232));
        uniMap.put("longitude", BigDecimal.valueOf(12.93123));
        destAddresses.put("arbeit", arbeitMap);
        destAddresses.put("uni", uniMap);
        persistentAttributes.put("HOME", homeAddress);
        persistentAttributes.put("DEST", destAddresses);

        final Map<String, String> slots = new HashMap<>();
        slots.put("time", "13:45");

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();
    }
}
