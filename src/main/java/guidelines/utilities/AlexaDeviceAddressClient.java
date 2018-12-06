package main.java.guidelines.utilities;

import com.amazon.ask.model.services.deviceAddress.Address;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.guidelines.exceptions.DeviceAddressClientException;
import main.java.guidelines.exceptions.UnauthorizedException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AlexaDeviceAddressClient {
    private static final Logger log = LoggerFactory.getLogger(AlexaDeviceAddressClient.class);

    private String deviceId;
    private String apiAccessToken;
    private String apiEndpoint;

    // Endpoint: /v1/devices/*deviceId*/settings/address
    private static final String BASE_API_PATH = "/v1/devices/";
    private static final String SETTINGS_PATH = "/settings/";
    private static final String FULL_ADDRESS_PATH = "address";

    public AlexaDeviceAddressClient(String deviceId, String apiAccessToken, String apiEndpoint) {
        this.deviceId = deviceId;
        this.apiAccessToken = apiAccessToken;
        this.apiEndpoint = apiEndpoint;
    }

    public Address getFullAddress() throws DeviceAddressClientException {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        String requestUrl = apiEndpoint + BASE_API_PATH + deviceId + SETTINGS_PATH + FULL_ADDRESS_PATH;
        log.info("Request will be made to the following URL: {}", requestUrl);
        HttpGet httpGet = new HttpGet(requestUrl);

        httpGet.addHeader("Authorization", "Bearer " + apiAccessToken);

        log.info("Sending request to Device Address API");
        Address address;
        try{
            HttpResponse addressResponse = closeableHttpClient.execute(httpGet);
            int statusCode = addressResponse.getStatusLine().getStatusCode();

            log.info("The Device Address API responded with a status code of {}", statusCode);

            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity httpEntity = addressResponse.getEntity();
                String responseBody = EntityUtils.toString(httpEntity);

                ObjectMapper objectMapper = new ObjectMapper();
                address = objectMapper.readValue(responseBody, Address.class);
            } else if (statusCode == HttpStatus.SC_FORBIDDEN) {
                log.info("Failed to authorize with a status code of {}", statusCode);
                throw new UnauthorizedException("Failed to authorize. ");
            } else {
                String errorMessage = "Device Address API query failed with status code of " + statusCode;
                log.info(errorMessage);
                throw new DeviceAddressClientException(errorMessage);
            }
        }catch (IOException error){
            throw new DeviceAddressClientException(error);
        }finally {
            log.info("Request to Address Device API completed.");
        }

        return address;
    }
}
