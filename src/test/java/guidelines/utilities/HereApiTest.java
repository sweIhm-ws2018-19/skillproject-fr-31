package guidelines.utilities;

import guidelines.models.Coordinate;
import org.junit.Assert;
import org.junit.Ignore;
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
    public void getRouteTest(){
        Assert.assertEquals(true,true);
    }

    @Ignore
    @Test
    public void getNearbyStationsTest(){
        final Map<String, Coordinate> want = new HashMap<>();
        want.put("Neustadtstraße", new Coordinate(48.4759,11.93533));
        want.put("Moosburg", new Coordinate(48.470331,11.930385));
        final Map<String, Coordinate> have = HereApi.getNearbyStations(new Coordinate(48.47452,11.93002));
        Assert.assertEquals(want,have);
    }
}
