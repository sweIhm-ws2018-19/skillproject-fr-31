package guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import guidelines.SpeechStrings;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

public class SetupTest {
    @Test
    public void setupStateNameIsNullTest(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final Map<String, String> slots = new HashMap<>();

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = Setup.setupState(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertNotNull(response.getOutputSpeech());
        assertNotEquals("test", response.getReprompt());
        assertTrue(response.getOutputSpeech().toString().contains(SpeechStrings.WELCOME_NO_CONFIG));
    }

    @Test
    public void setupStateNameIsNotNullTest(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Object> persistentAttributes = new HashMap<>();
        persistentAttributes.put("NAME", "Denis");
        final Map<String, String> slots = new HashMap<>();

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = Setup.setupState(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertNotNull(response.getOutputSpeech());
        assertNotEquals("test", response.getReprompt());
        // MISSING ASSERT TRUE RESPONSE WITH SPEECH
    }

    @Test
    public void setupStateHomeIsNullTest(){
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Object> persistentAttributes = new HashMap<>();
        persistentAttributes.put("NAME", "Denis");
        persistentAttributes.put("HOME", null);
        final Map<String, String> slots = new HashMap<>();

        final HandlerInput inputMock = TestUtil.mockHandlerInput(slots, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = Setup.setupState(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertNotNull(response.getOutputSpeech());
        assertNotEquals("test", response.getReprompt());
        assertTrue(response.getOutputSpeech().toString().contains(SpeechStrings.NO_PERMISSION_DEVICE_GET_HOME));
    }
}
