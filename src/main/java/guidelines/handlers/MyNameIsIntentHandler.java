package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import guidelines.SpeechStrings;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.BasicUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class MyNameIsIntentHandler implements RequestHandler {

    private static String name;

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("MyNameIsIntent").and(sessionAttribute("State", GuideStates.INSERT_NAME.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();
        Slot nameSlot = slots.get("name");

        String speechText;
        String repromptText;
        boolean askResponse = false;

        if (nameSlot != null) {
            setName(nameSlot.getValue());
            AttributesManager attributesManager = input.getAttributesManager();

            BasicUtils.setPersistentAttributes(attributesManager, "NAME", nameSlot.getValue());
            return Setup.SetupState(input);

//            speechText = SpeechStrings.WELCOME_USER + getName() + SpeechStrings.START_CONFIG_DEST_ADDRESS;
//            repromptText = getName() + SpeechStrings.PLS + SpeechStrings.STREET;
        } else {
            speechText = SpeechStrings.INAUDIBLE + " Versuche bitte erneut deinen Name zu sagen";
            repromptText = SpeechStrings.NAMEUNKNOWN;
            askResponse = true;
        }


        FallbackIntentHandler.setFallbackMessage(speechText);
        ResponseBuilder respBuilder = input.getResponseBuilder();
        respBuilder.withSimpleCard(SpeechStrings.SKILL_NAME, "Namenseingabe")
                .withSpeech(speechText)
                .withReprompt("Bitte sage mir nochmal die Straße, Hausnummer und Stadt")
                .withShouldEndSession(false);

        if (askResponse) {
            respBuilder.withShouldEndSession(false)
                    .withReprompt(repromptText);
        }

        return respBuilder.build();
    }

    public static String getName() {
        return name;
    }

    private void setName(String name) {
        MyNameIsIntentHandler.name = name;
    }
}
