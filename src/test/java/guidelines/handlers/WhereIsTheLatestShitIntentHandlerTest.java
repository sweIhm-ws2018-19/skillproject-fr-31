package test.java.guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import main.java.guidelines.handlers.WhereIsTheLatestShitIntentHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class WhereIsTheLatestShitIntentHandlerTest {
    private WhereIsTheLatestShitIntentHandler handler;

    @Before
    public void setup(){
        handler = new WhereIsTheLatestShitIntentHandler();
    }

    @Test
    public void canHandleTest(){
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.matches(any())).thenReturn(true);
        assertTrue(handler.canHandle(inputMock));
    }
}
