package test.java.guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import main.java.guidelines.handlers.BackToStartIntentHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class BackToStartIntentHandlerTest {
    private BackToStartIntentHandler handler;

    @Before
    public void setup() {
        handler = new BackToStartIntentHandler();
    }

    @Test
    public void canHandleTest() {
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.matches(any())).thenReturn(true);
        assertTrue(handler.canHandle(inputMock));
    }
}
