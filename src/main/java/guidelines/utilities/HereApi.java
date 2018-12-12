package guidelines.utilities;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import guidelines.models.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HereApi {
    private static final String APPID_APPCODE = "?app_id=7nxl2JeeP1KRwPVAwQpP&app_code=lwWNVNoVphoUo3V5XhGZng";

    private static final String GEOCODEBASE = "https://geocoder.api.here.com/6.2/geocode.json"+ APPID_APPCODE;
    private static final String ROUTEBASE = "https://transit.api.here.com/v3/route.json"+ APPID_APPCODE;
    private static final String STATIONSBASE = "https://transit.api.here.com/v3/stations/by_geocoord.json" + APPID_APPCODE;

    private static final Logger log = LoggerFactory.getLogger(HereApi.class);

    private HereApi(){}

    public static Coordinate getCoordinate(String street, int number, String city){
        String requestUrl = GEOCODEBASE +"&searchtext="+ street + "&city=" + city + "&housenumber=" + number;
        JsonNode jsNode = sendRequest(requestUrl);
        try{
            jsNode = jsNode.findPath("DisplayPosition");
            if(jsNode.asText().equals("missing node"))
                return null;
            return new Coordinate(jsNode.findValue("Latitude").asDouble(),jsNode.findValue("Longitude").asDouble());
        }catch(NullPointerException ex){
            return null;
        }
    }

    public static Coordinate getRoute(Coordinate home, Coordinate dest){
        JsonNode timeReq = sendRequest("http://worldtimeapi.org/api/timezone/Europe/Berlin");
        final String time = timeReq.findValue("datetime").asText();
        String requestUrl = ROUTEBASE + "&routing=all&dep=" + home.getLatitude() + "," + home.getLongitude() + "&arr=" + dest.getLatitude() + "," + dest.getLongitude() +
                "&time=" + time + "&routingMode=realtime";
        JsonNode jsNode = sendRequest(requestUrl);
        return new Coordinate(1.0,1.0);
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
        String result = "";
        result = rs.getForObject(url, String.class);

        ObjectMapper jsonMapper = new ObjectMapper();
        JsonNode jsNode = null;
        try {
            jsNode = jsonMapper.readTree(result);
        } catch (IOException e) {
            log.info("could not read js node");
        }
        return jsNode;
    }
}