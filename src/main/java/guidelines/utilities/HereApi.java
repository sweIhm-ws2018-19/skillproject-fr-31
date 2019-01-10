package guidelines.utilities;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import guidelines.exceptions.HereApiRequestFailedException;
import guidelines.models.Coordinate;
import guidelines.models.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class HereApi {
    private static final String SECTIONS_STRING = "Sections";
    private static final String APPID_APPCODE = "?app_id=7nxl2JeeP1KRwPVAwQpP&app_code=lwWNVNoVphoUo3V5XhGZng";

    private static final String GEOCODEBASE = "https://geocoder.api.here.com/6.2/geocode.json"+ APPID_APPCODE;
    private static final String ROUTEBASE = "https://transit.api.here.com/v3/route.json"+ APPID_APPCODE;
    private static final String STATIONSBASE = "https://transit.api.here.com/v3/stations/by_geocoord.json" + APPID_APPCODE;

    private static final Logger log = LoggerFactory.getLogger(HereApi.class);

    private HereApi(){}

    public static Coordinate getCoordinate(String street, int number, String city){
        String requestUrl = GEOCODEBASE +"&searchtext="+ street + "&city=" + city + "&housenumber=" + number;
        JsonNode jsNode = sendRequest(requestUrl);
        jsNode = jsNode.findPath("DisplayPosition");
        if(jsNode.isMissingNode())
            return null;
        return new Coordinate(jsNode.findValue("Latitude").asDouble(),jsNode.findValue("Longitude").asDouble());
    }

    public static Route getRoute(Coordinate home, Coordinate dest, String time){
        final Instant currentTime = Instant.ofEpochMilli(System.currentTimeMillis());
        OffsetDateTime currentTimeMez = currentTime.atOffset(ZoneOffset.ofHours(1));

        String requestUrl = ROUTEBASE + "&routing=all&dep=" + home.getLatitude() + "," + home.getLongitude() + "&arr=" + dest.getLatitude() + "," + dest.getLongitude() +
                "&time=" + time + "&routingMode=realtime&arrival=1&walk=2000,200";
        JsonNode jsNode = sendRequest(requestUrl);

        JsonNode connection = jsNode.findPath("Connection").get(0);
        String depTimeSt = connection.findValue("Dep").findValue("time").asText();
        String firstStation = connection.findValue(SECTIONS_STRING).findValue("Sec").get(0).findValue("Arr").findValue("Stn").findValue("name").asText();
        String transport;
        try{
            transport = connection.findValue(SECTIONS_STRING).findValue("Sec").get(1).findValue("Dep").findValue("Transport").findValue("At").findValue("category").asText();
        }catch(NullPointerException e){
            transport = "";
        }
        String transTime = connection.findValue(SECTIONS_STRING).findValue("Sec").get(1).findValue("Dep").findValue("time").asText();

        final OffsetDateTime transTimeObj = Instant.parse(transTime + ".000Z").atOffset(ZoneOffset.ofHours(1)).minusHours(1);
        transTime = transTimeObj.getHour() + ":" + transTimeObj.getMinute();

        OffsetDateTime depTime = Instant.parse( depTimeSt + ".000Z" ).atOffset(ZoneOffset.ofHours(1)).minusHours(1);
        int minutesLeft =(int)currentTimeMez.until(depTime, ChronoUnit.MINUTES);
        return new Route(minutesLeft, 0, firstStation, transport, transTime);
    }

    public static Map<String, Coordinate> getNearbyStations(Coordinate co){
        String stationReq = STATIONSBASE + "&center=" + co.getLatitude() + "," + co.getLongitude();
        JsonNode jsNode = sendRequest(stationReq);
        jsNode = jsNode.findPath("Stn");

        Map<String, Coordinate> stations = new HashMap<>();
        if (jsNode.isArray()) {
            for (final JsonNode objNode : jsNode) {
                stations.put(objNode.get("name").asText(), new Coordinate(objNode.get("y").asDouble(), objNode.get("x").asDouble()));
            }
        }
        return stations;
    }

    private static JsonNode sendRequest(String url){
        RestTemplate rs = new RestTemplate();
        String result = rs.getForObject(url, String.class);
        ObjectMapper jsonMapper = new ObjectMapper();
        JsonNode jsNode = null;
        try {
            jsNode = jsonMapper.readTree(result);
        } catch (IOException e) {
            log.info("could not read js node");
        }
        if(jsNode == null)
            throw new HereApiRequestFailedException("Could not send HereApi Request: " + url);
        return jsNode;
    }
}