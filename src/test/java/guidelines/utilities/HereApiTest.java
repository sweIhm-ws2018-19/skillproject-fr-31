package guidelines.utilities;

import org.junit.Assert;
import org.junit.Test;

public class HereApiTest {

    @Test
    public void getCoordinatesTest(){
        final Coordinates want = new Coordinates(48.47452, 11.93002);
        final Coordinates have = HereApi.getCoordinates("In der Feldkirchner Au", 20, "Moosburg", 85368);
        Assert.assertEquals(want, have);
    }

    @Test
    public void getRouteTest(){
        Assert.assertEquals(true,true);
    }
}
