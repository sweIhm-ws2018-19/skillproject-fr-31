package main.java.guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;

import main.java.guidelines.SpeechStrings;
import main.java.guidelines.stateMachine.GuideStates;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class HomeAddressHelpIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("HomeAddressHelpIntent").and(sessionAttribute("State",
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
            attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.HELP));
            speechText = SpeechStrings.HELP_HOME_ADDRESS;
            repromptText = "Vielen dank";
        } else {
            repromptText = "Bitte wiederhole nochmal was du gesagt hast? Möchtest du mit den Infos zur Zieladresse weiterfahren oder die Hilfefunktion beenden?";
            speechText = "Leider hat etwas nicht geklappt, bis sage mir nochmal ob du Infos zur Heimadresse oder die Hilfefunktion beenden willst";
            askResponse = true;
        }

        respBuilder.withSimpleCard("Hilfe Heimadresse", "Hilfe Heimadresse")
                .withSpeech(speechText)
                .withReprompt(repromptText)
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
