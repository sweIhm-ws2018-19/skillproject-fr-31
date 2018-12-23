package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import guidelines.SpeechStrings;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.BasicUtils;

import java.util.Map;
import java.util.Optional;

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
                // Return that we want to get the homeAdress
                BasicUtils.setSessionAttributes(attributesManager, GuideStates.STATE.getKey(), GuideStates.GET_HOME_ADDR);
                return BasicUtils.putTogether("Home Adresse", SpeechStrings.SAY_HOME_ADDRESS).build();
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
