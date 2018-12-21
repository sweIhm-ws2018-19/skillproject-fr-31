package guidelines.models;

import org.junit.Assert;
import org.junit.Test;

public class CoordinateTest {

    @Test
    public void setLatitudeTest(){
        final Coordinate have = new Coordinate(1.0, 2.0);
        have.setLatitude(95.0);
        final Coordinate want = new Coordinate(95.0 , 2.0);
        Assert.assertEquals(want, have);
    }

    @Test
    public void setLongitudeTest(){
        final Coordinate have = new Coordinate(1.0, 2.0);
        have.setLongitude(95.0);
        final Coordinate want = new Coordinate(1.0 , 95.0);
        Assert.assertEquals(want, have);
    }

    @Test
    public void getJsonString(){
        final Coordinate have = new Coordinate(48.154721, 11.561071);
        String want = "{\n" +
                "\t\"NAME\":test,\n" +
                "\t\"lat\":48.154721,\n" +
                "\t\"long\":11.561071\n" +
                "}";
        Assert.assertEquals(want,have.toJsonString("test"));
    }
}
