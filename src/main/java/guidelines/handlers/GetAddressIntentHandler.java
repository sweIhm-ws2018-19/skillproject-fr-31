package main.java.guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Permissions;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.interfaces.system.SystemState;
import com.amazon.ask.model.services.deviceAddress.Address;
import com.amazon.ask.response.ResponseBuilder;
import main.java.guidelines.SpeechStrings;
import main.java.guidelines.exceptions.DeviceAddressClientException;
import main.java.guidelines.exceptions.UnauthorizedException;
import main.java.guidelines.utilities.AlexaDeviceAddressClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        try {
            SystemState systemState = input.getRequestEnvelope().getContext().getSystem();
            String apiAccessToken = systemState.getApiAccessToken();
            String deviceId = systemState.getDevice().getDeviceId();
            String apiEndpoint = systemState.getApiEndpoint();

            AlexaDeviceAddressClient alexaDeviceAddressClient = new AlexaDeviceAddressClient(deviceId, apiAccessToken, apiEndpoint);
            Address addressObject = alexaDeviceAddressClient.getFullAddress();

            if (addressObject == null) {
                return responseBuilder.withSimpleCard(SpeechStrings.SKILL_NAME, "Es gab einen Fehler mit dem Skill.")
                        .withSpeech("Es gab einen Fehler mit dem Skill.")
                        .withReprompt("Versuche es noch einmal")
                        .build();
            }

            return responseBuilder.withSimpleCard(SpeechStrings.SKILL_NAME, addressObject.getAddressLine1())
                    .withSpeech(addressObject.getAddressLine1())
                    .build();
        }catch (UnauthorizedException error){
            return responseBuilder.withAskForPermissionsConsentCard(permissionList)
                    .withSpeech(SpeechStrings.NO_PERMISSION_DEVICE_ADDRESS)
                    .build();
        } catch (DeviceAddressClientException error){
            log.error("Der Geräte-Client hat es nicht geschafft die Adresse zurückzugeben");
            return responseBuilder.withSimpleCard(SpeechStrings.SKILL_NAME, "Es gab einen Fehler mit dem Skill.")
                    .withSpeech("Es gab einen Fehler mit dem Skill.")
                    .withReprompt("Versuche es noch einmal")
                    .build();
        }
    }
}
