package test.java.guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import main.java.guidelines.handlers.DestinationAddressHelpIntentHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class DestinationAddressHelpIntentHandlerTest {
    private DestinationAddressHelpIntentHandler handler;

    @Before
    public void setup(){
        handler = new DestinationAddressHelpIntentHandler();
    }

    @Test
    public void canHandleTest(){
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.matches(any())).thenReturn(true);
        assertTrue(handler.canHandle(inputMock));
    }
}
