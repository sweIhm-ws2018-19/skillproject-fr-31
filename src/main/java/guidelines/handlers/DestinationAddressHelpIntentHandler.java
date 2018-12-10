package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import guidelines.SpeechStrings;
import guidelines.stateMachine.GuideStates;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class DestinationAddressHelpIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("DestinationAddressHelpIntent").and(sessionAttribute("State",
                GuideStates.HELP.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        ResponseBuilder respBuilder = handlerInput.getResponseBuilder();

        Request request = handlerInput.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();
        Slot exitOrHomeSlot = slots.get("exitOrHome");

        String speechText;
        String repromptText;

        boolean askResponse = false;

        if (exitOrHomeSlot != null) {
            AttributesManager attributesManager = handlerInput.getAttributesManager();
            attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.HELP));
            speechText = SpeechStrings.HELP_DESTINATION_ADDRESS;
            repromptText = SpeechStrings.THANKS;
        } else {
            repromptText = SpeechStrings.REPROMPT_DESTINATION_ADDRESS;
            speechText = SpeechStrings.SPEECH_ERROR_DESTINATION_ADDRESS;
            askResponse = true;
        }

        respBuilder.withSimpleCard(SpeechStrings.SKILL_NAME, "Hilfe Zieladresse")
                .withSpeech(speechText)
                .withShouldEndSession(false)
                .build();

        if (askResponse) {
            respBuilder.withShouldEndSession(false)
                    .withReprompt(repromptText)
                    .withSpeech(speechText);
        }

        return respBuilder.build();
    }
}
