package guidelines.utilities;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasicUtilsTest {

    private BasicUtils sut;

    @Before
    public void setup() {
        sut = new BasicUtils();
    }

    @Test
    public void test_Ctor() {
        assertEquals(sut.getClass(), BasicUtils.class);
    }
}
