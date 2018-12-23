package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import guidelines.SpeechStrings;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.BasicUtils;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class MyNameIsIntentHandler implements RequestHandler {

    private static String name;

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("MyNameIsIntent").and(sessionAttribute(GuideStates.STATE.getKey(), GuideStates.INSERT_NAME.toString())));
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

        if (nameSlot != null) {
            setName(nameSlot.getValue());
            AttributesManager attributesManager = input.getAttributesManager();

            BasicUtils.setPersistentAttributes(attributesManager, "NAME", nameSlot.getValue());
            return Setup.setupState(input);
        } else {
            speechText = SpeechStrings.INAUDIBLE + " Versuche bitte erneut deinen Name zu sagen";
            repromptText = SpeechStrings.NAMEUNKNOWN;
            FallbackIntentHandler.setFallbackMessage(speechText);
            ResponseBuilder respBuilder = input.getResponseBuilder();
            return  respBuilder.withSimpleCard(SpeechStrings.SKILL_NAME, "Namenseingabe")
                    .withSpeech(speechText)
                    .withReprompt(repromptText)
                    .withShouldEndSession(false)
                    .build();
        }
    }

    public static String getName() {
        return name;
    }

    private static void setName(String name) {
        MyNameIsIntentHandler.name = name;
    }
}
