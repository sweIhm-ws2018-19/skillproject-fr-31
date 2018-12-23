package guidelines.utilities;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class BasicUtilsTest {

    private BasicUtils sut;

    @Before
    public void setup() {
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        sut = new BasicUtils(inputMock.getAttributesManager());
    }

    @Test
    public void test_Ctor() {
        assertEquals(sut.getClass(), BasicUtils.class);
    }
}
