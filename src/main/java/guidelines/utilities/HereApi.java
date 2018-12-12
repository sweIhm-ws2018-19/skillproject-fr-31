package guidelines.utilities;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import guidelines.models.Coordinate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HereApi {
    private static final String appId = "7nxl2JeeP1KRwPVAwQpP";
    private static final String appCode = "lwWNVNoVphoUo3V5XhGZng";

    private static final String geocodeBase = "https://geocoder.api.here.com/6.2/geocode.json?app_id="+ appId +"&app_code="+ appCode;
    private static final String routeBase = "https://transit.api.here.com/v3/route.json?app_id="+appId+"&app_code="+appCode;
    private static final String stationsBase = "https://transit.api.here.com/v3/stations/by_geocoord.json?app_id="+appId+"&app_code="+appCode;

    public static Coordinate getCoordinate(String street, int number, String city, int zip){
        String requestUrl = geocodeBase +"&searchtext="+ street + "&city=" + city + "&housenumber=" + number + "&postalcode=" + zip;
        JsonNode jsNode = sendRequest(requestUrl);
        jsNode = jsNode.findPath("DisplayPosition");

        return new Coordinate(jsNode.findValue("Latitude").asDouble(),jsNode.findValue("Longitude").asDouble());
    }

    public static Coordinate getRoute(Coordinate home, Coordinate dest){
        JsonNode timeReq = sendRequest("http://worldtimeapi.org/api/timezone/Europe/Berlin");
        final String time = timeReq.findValue("datetime").asText();
        String requestUrl = routeBase + "&routing=all&dep=" + home.getLatitude() + "," + home.getLongitude() + "&arr=" + dest.getLatitude() + "," + dest.getLongitude() +
                "&time=" + time + "&routingMode=realtime";
        JsonNode jsNode = sendRequest(requestUrl);
        return new Coordinate(1.0,1.0);
    }

    public static Map<String, Coordinate> getNearbyStations(Coordinate co){
        String stationReq = stationsBase + "&center=" + co.getLatitude() + "," + co.getLongitude();
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

    public static Map<String, Coordinates> getNearbyStations(Coordinates co){
        String stationReq = stationsBase + "&center=" + co.getLatitude() + "," + co.getLongitude();
        JsonNode jsNode = sendRequest(stationReq);
        jsNode = jsNode.findPath("Stn");

        Map<String,Coordinates> stations = new HashMap<>();
        if (jsNode.isArray()) {
            for (final JsonNode objNode : jsNode) {
                stations.put(objNode.get("name").asText(), new Coordinates(objNode.get("y").asDouble(), objNode.get("x").asDouble()));
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
            System.out.println("could not read js node");
        }
        return jsNode;
    }
}