package guidelines.utilities;

import guidelines.models.Coordinate;
import guidelines.models.Route;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
        final Instant oneHourAgo = Instant.ofEpochMilli(System.currentTimeMillis());
        OffsetDateTime time = oneHourAgo.plusSeconds(2*60*60).atOffset(ZoneOffset.ofHours(1));
        final Route have = HereApi.getRoute(new Coordinate(48.14989,11.54617),
                new Coordinate(48.15427,11.55383),time.toString());
        Assert.assertTrue(have.getMinutesLeft() >= 0);
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
