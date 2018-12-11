package guidelines.utilities;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import guidelines.models.Coordinates;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class HereApi {
    private static final String appId = "7nxl2JeeP1KRwPVAwQpP";
    private static final String appCode = "lwWNVNoVphoUo3V5XhGZng";

    private static final String geocodeBase = "https://geocoder.api.here.com/6.2/geocode.json?app_id="+ appId +"&app_code="+ appCode;
    private static final String routeBase = "https://transit.api.here.com/v3/route.json?app_id="+appId+"&app_code="+appCode;

    public static Coordinates getCoordinates(String street, int number, String city, int zip){
        String requestUrl = geocodeBase +"&searchtext="+ street + "&city=" + city + "&housenumber=" + number + "&postalcode=" + zip;
        JsonNode jsNode = sendRequest(requestUrl);
        jsNode = jsNode.findPath("DisplayPosition");

        return new Coordinates(jsNode.findValue("Latitude").asDouble(),jsNode.findValue("Longitude").asDouble());
    }

    public static Coordinates getRoute(Coordinates home, Coordinates dest){
        JsonNode timeReq = sendRequest("http://worldtimeapi.org/api/timezone/Europe/Berlin");
        final String time = timeReq.findValue("datetime").asText();
        String requestUrl = "&routing=all&dep=" + home.getLatitude() + "," + home.getLongitude() + "&arr=" + dest.getLatitude() + "," + dest.getLongitude() +
                "&time=" + time + "&routingMode=realtime";
        JsonNode jsNode = sendRequest(requestUrl);
        return new Coordinates(1.0,1.0);
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
