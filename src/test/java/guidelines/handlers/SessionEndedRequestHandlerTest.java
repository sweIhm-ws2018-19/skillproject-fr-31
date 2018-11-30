package java.guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

public class SessionEndedRequestHandlerTest {
    private SessionEndedRequestHandler handler;

    @Before
    public void setup(){
        handler = new SessionEndedRequestHandler();
    }

    @Test
    public void canHandleTest(){
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.matches(any())).thenReturn(true);
        assertTrue(handler.canHandle(inputMock));
    }
}