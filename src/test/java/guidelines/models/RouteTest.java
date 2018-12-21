package guidelines.models;

import org.junit.Assert;
import org.junit.Test;

public class RouteTest {

    @Test
    public void RouteTest() {
        Route want = new Route(20, 0, "Moosburg");
        Route have = new Route(20, 0 , "Hinterdupfing");
        Assert.assertNotEquals(want,have);
    }
}