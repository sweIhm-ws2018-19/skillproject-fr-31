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

public class HomeAddressHelpIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("HomeAddressHelpIntent").and(sessionAttribute(GuideStates.STATE.getKey(),
                GuideStates.HELP.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        ResponseBuilder respBuilder = handlerInput.getResponseBuilder();

        Request request = handlerInput.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();
        Slot exitOrDestSlot = slots.get("exitOrDest");

        String speechText;
        String repromptText;

        boolean askResponse = false;

        if (exitOrDestSlot != null) {
            AttributesManager attributesManager = handlerInput.getAttributesManager();
            BasicUtils.setSessionAttributes(attributesManager,GuideStates.STATE.getKey(), GuideStates.HELP);
            speechText = SpeechStrings.HELP_HOME_ADDRESS;
            repromptText = "Vielen dank";
        } else {
            repromptText = "Bitte wiederhole nochmal was du gesagt hast? Möchtest du mit den Infos zur Zieladresse weiterfahren oder die Hilfefunktion beenden?";
            speechText = "Leider hat etwas nicht geklappt, bis sage mir nochmal ob du Infos zur Heimadresse oder die Hilfefunktion beenden willst";
            askResponse = true;
        }


        FallbackIntentHandler.setFallbackMessage(speechText);
        respBuilder.withSimpleCard(SpeechStrings.SKILL_NAME, "Hilfe Heimadresse")
                .withSpeech(speechText)
                .withReprompt("Moechtest du Infos zur Zieladresse oder zurueck zum Start?")
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
