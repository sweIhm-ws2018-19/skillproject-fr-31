package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.when;

public class TestUtil {

    public static HandlerInput mockHandlerInput(Map<String, String> slots, Map<String, Object> sessionAttributes, Map<String, Object> persistentAttributes, Map<String, Object> requestAttributes) {
        final AttributesManager attributesManagerMock = Mockito.mock(AttributesManager.class);
        when(attributesManagerMock.getSessionAttributes()).thenReturn(sessionAttributes);
        when(attributesManagerMock.getPersistentAttributes()).thenReturn(persistentAttributes);
        when(attributesManagerMock.getRequestAttributes()).thenReturn(requestAttributes);

        final Intent.Builder intentBuilder = Intent.builder();
        slots.forEach((key, value) ->
                intentBuilder.putSlotsItem(key, Slot.builder().withName(key).withValue(value).build())
        );

        // Mock Slots
        final RequestEnvelope requestEnvelopeMock = RequestEnvelope.builder()
                .withRequest(IntentRequest.builder()
                        .withIntent(intentBuilder.build())
                        .build())
                .build();


        // Mock Handler input attributes
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        when(input.getAttributesManager()).thenReturn(attributesManagerMock);
        when(input.getResponseBuilder()).thenReturn(new ResponseBuilder());
        when(input.getRequestEnvelope()).thenReturn(requestEnvelopeMock);

        return input;
    }

    public static HandlerInput mockHandlerInput(String playerName, Map<String, Object> sessionAttributes, Map<String, Object> persistentAttributes, Map<String, Object> requestAttributes) {
        return mockHandlerInput(Collections.singletonMap("PlayerName", playerName), sessionAttributes, persistentAttributes, requestAttributes);
    }
}