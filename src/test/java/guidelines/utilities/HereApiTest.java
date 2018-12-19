package guidelines.utilities;

import guidelines.models.Coordinate;
import guidelines.models.Route;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HereApiTest {

    @Test
    public void getCoordinatesTest(){
        final Coordinate want = new Coordinate(48.47452, 11.93002);
        final Coordinate have = HereApi.getCoordinate("In der Feldkirchner Au", 20, "Moosburg");
        Assert.assertEquals(want, have);
    }

    @Test
    public void getCoordinatesTestWithWrongAddr(){
        final Coordinate want = null;
        final Coordinate have = HereApi.getCoordinate("Lothstraße", 64, "Moosburg");
        Assert.assertEquals(want, have);
    }

    @Test
    public void getRouteTest(){
        final Route have = HereApi.getRoute(new Coordinate(48.474536,11.9278286), new Coordinate(48.1549484,11.5537992),"2018-12-19T20:00:29.099426+01:00");
        Assert.assertTrue(have.getMinutesLeft() >= 0);
    }

    @Test
    public void getNearbyStationsTest(){
        final Map<String, Coordinate> want = new HashMap<>();
        want.put("Neustadtstraße", new Coordinate(48.4759,11.93533));
        want.put("Moosburg", new Coordinate(48.470331,11.930385));
        final Map<String, Coordinate> have = HereApi.getNearbyStations(new Coordinate(48.47452,11.93002));
        Assert.assertEquals(want,have);
    }
}
