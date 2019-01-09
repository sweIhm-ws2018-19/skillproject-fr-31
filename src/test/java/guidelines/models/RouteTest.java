package guidelines.models;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RouteTest {

    @Test
    public void routeTest() {
        Route want = new Route(20, 0, "Moosburg", "", "");
        Route have = new Route(20, 0 , "Hinterdupfing", "", "");

        Assert.assertNotEquals(want,have);
    }

    @Test
    public void routeEqualsTest(){
        assertTrue(new Route(20, 0, "Moosburg", "", "").equals(new Route(20, 0, "Moosburg", "", "")));
    }
}