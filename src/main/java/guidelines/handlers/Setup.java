package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Permissions;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.interfaces.system.SystemState;
import guidelines.SpeechStrings;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.BasicUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static guidelines.utilities.DeviceAddressClient.getDeviceAddress;

public class Setup {
    private Setup() {
    }

    public static Optional<Response> setupState(HandlerInput input) {

        // Attribute Manger for Database info
        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();
        // If no Name ask Name
        if (persistentAttributes.get("NAME") == null) {
            // store in database
            BasicUtils.setSessionAttributes(attributesManager, GuideStates.STATE.getKey(), GuideStates.INSERT_NAME);

            return BasicUtils.putTogether("Namen", SpeechStrings.WELCOME_NO_CONFIG).build();
        } else {
            // if home adress is available
            if (persistentAttributes.get("HOME") == null) {
                // needed for address permision
                String permission = "read::alexa:device:all:address";
                List<String> permissionList = new ArrayList<>();
                permissionList.add(permission);

                Session session = input.getRequestEnvelope().getSession();
                Permissions permissions = session.getUser().getPermissions();

                SystemState systemState = input.getRequestEnvelope().getContext().getSystem();
                String apiAccessToken = systemState.getApiAccessToken();
                String deviceId = systemState.getDevice().getDeviceId();
                String apiEndpoint = systemState.getApiEndpoint();
                if (permissions == null) {
                    // Return that we want to get the homeAdress
                    BasicUtils.setSessionAttributes(attributesManager, GuideStates.STATE.getKey(), GuideStates.GET_HOME_ADDR);
                    return BasicUtils.putTogether("Home Adresse", SpeechStrings.NO_PERMISSION_DEVICE_GET_HOME).build();
                } else {
                    String deviceAddressJson = getDeviceAddress(apiEndpoint, deviceId, apiAccessToken);
                    if (deviceAddressJson != null && !deviceAddressJson.isEmpty()) {
                        BasicUtils.setPersistentAttributes(attributesManager, "HOME", deviceAddressJson);
                    } else {
                        BasicUtils.setSessionAttributes(attributesManager, GuideStates.STATE.getKey(), GuideStates.GET_HOME_ADDR);
                        return BasicUtils.putTogether("Home Adresse", SpeechStrings.NO_PERMISSION_DEVICE_GET_HOME).build();
                    }
                }

            }

            if (persistentAttributes.get("DEST") == null) {
                BasicUtils.setSessionAttributes(attributesManager, GuideStates.STATE.getKey(), GuideStates.GET_DEST_ADDR);
                return BasicUtils.putTogether("Ziel Adresse", SpeechStrings.START_CONFIG_DEST_ADDRESS).build();
            }

            BasicUtils.setSessionAttributes(attributesManager, GuideStates.STATE.getKey(), GuideStates.TRANSIT);
            String outputMessage = String.format(SpeechStrings.WELCOME_TRANSIT, persistentAttributes.get("NAME"));
            return BasicUtils.putTogether("Route", outputMessage).build();


        }

    }


}
