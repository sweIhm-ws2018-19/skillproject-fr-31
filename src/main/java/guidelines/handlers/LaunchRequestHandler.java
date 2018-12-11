/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Permissions;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.interfaces.system.SystemState;
import com.amazon.ask.model.services.deviceAddress.Address;
import com.amazon.ask.response.ResponseBuilder;

import guidelines.SpeechStrings;
import guidelines.models.DeviceAddress;
import guidelines.stateMachine.GuideStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.amazon.ask.request.Predicates.requestType;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class LaunchRequestHandler implements RequestHandler {


    private static final Logger log = LoggerFactory.getLogger(LaunchRequestHandler.class);

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class).or(sessionAttribute("State", GuideStates.LAUNCH_STATE)));
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

        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();

        String outputMessage;
        if (persistentAttributes.get("NAME") == null) {
            RestTemplate restTemplate = new RestTemplate();
            String requestUrl = apiEndpoint + "/v1/devices/" + deviceId + "/settings/address";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            httpHeaders.add("Authorization", "Bearer " + apiAccessToken);
            HttpEntity<String> request = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, request, String.class);

            // store in session
            attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.USE_GPS_OR_NOT));
            // store in database
            persistentAttributes.put("Heimadresse", response.getBody());

            attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.INSERT_NAME));

            attributesManager.setPersistentAttributes(persistentAttributes);
            attributesManager.savePersistentAttributes();

            outputMessage = SpeechStrings.WELCOME_NO_CONFIG;
        } else {
            attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.TRANSIT));
            outputMessage = String.format(SpeechStrings.WELCOME_TRANSIT, persistentAttributes.get("NAME"));
        }

        return responseBuilder.withSimpleCard(SpeechStrings.SKILL_NAME, "Start")
                .withSpeech(outputMessage)
                .withReprompt(outputMessage)
                .build();
    }
}
