package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Permissions;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.interfaces.system.SystemState;
import com.amazon.ask.response.ResponseBuilder;
import guidelines.SpeechStrings;
import guidelines.models.Address;
import guidelines.stateMachine.GuideStates;
import guidelines.utilities.HereApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.amazon.ask.request.Predicates.intentName;

public class GetAddressIntentHandler implements RequestHandler {

    private static final Logger log = LoggerFactory.getLogger(GetAddressIntentHandler.class);

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("GetAddressIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder responseBuilder = input.getResponseBuilder();
        String permission = "read::alexa:device:all:address";
        List<String> permissionList = new ArrayList<>();
        permissionList.add(permission);
        Session session = input.getRequestEnvelope().getSession();
        Permissions permissions = session.getUser().getPermissions();
        if (permissions == null) {
            log.info("Der Nutzer hat keine Freigabe für den Skill gegeben.");
            return responseBuilder.withAskForPermissionsConsentCard(permissionList)
                    .withSpeech(SpeechStrings.NO_PERMISSION_DEVICE_ADDRESS)
                    .build();
        }

        SystemState systemState = input.getRequestEnvelope().getContext().getSystem();
        String apiAccessToken = systemState.getApiAccessToken();
        String deviceId = systemState.getDevice().getDeviceId();
        String apiEndpoint = systemState.getApiEndpoint();

        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = apiEndpoint + "/v1/devices/" + deviceId + "/settings/address";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.add("Authorization", "Bearer " + apiAccessToken);
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<Address> response = restTemplate.exchange(requestUrl, HttpMethod.GET, request, Address.class);
        Address deviceAddress = response.getBody();

        AttributesManager attributesManager = input.getAttributesManager();
        // store in session
        attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.USE_GPS_OR_NOT));
        // store in database
        Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();
        persistentAttributes.put("Street", deviceAddress.getAddressLine1());
        persistentAttributes.put("PostalCode", deviceAddress.getCity());
        persistentAttributes.put("City", deviceAddress.getPostalCode());

        attributesManager.setPersistentAttributes(persistentAttributes);
        attributesManager.savePersistentAttributes();

        return responseBuilder.withSpeech(deviceAddress.getAddressLine1() + " " + deviceAddress.getPostalCode() + " " +deviceAddress.getCity()).build();
    }
}
