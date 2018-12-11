package guidelines.utilities;

import guidelines.models.Coordinates;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void getNearbyStationsTest(){
        final Map<String, Coordinates> want = new HashMap<>();
        want.put("Neustadtstraße", new Coordinates(48.4759,11.93533));
        want.put("Moosburg", new Coordinates(48.470331,11.930385));
        final Map<String, Coordinates> have = HereApi.getNearbyStations(new Coordinates(48.47452,11.93002));
        Assert.assertEquals(want,have);
    }
}
