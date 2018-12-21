package guidelines.utilities;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class DeviceAddressClient {
    private DeviceAddressClient(){

    }

    public static String getDeviceAddress(String apiEndpoint, String deviceId, String apiAccessToken){
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = apiEndpoint + "/v1/devices/" + deviceId + "/settings/address";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.add("Authorization", "Bearer " + apiAccessToken);
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, request, String.class);
        return response.getBody();
    }
}
