package main.java.guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import main.java.guidelines.SpeechStrings;
import main.java.guidelines.stateMachine.GuideStates;
import org.w3c.dom.Attr;

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
        // TODO: Switch between DestHelpInfo and HomeHelpInfo and break
        Request request = handlerInput.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();
        Slot exitOrHomeSlot = slots.get("exitOrHome");

        String speechText;
        String repromptText;

        boolean askResponse = false;

        if (exitOrHomeSlot != null) {
            String choice = exitOrHomeSlot.getValue();
            String stateToSet;
            if(choice.equals("Heimadresse")){
                stateToSet = GuideStates.HELP.toString();
            }else{
                stateToSet = GuideStates.LAUNCH_STATE.toString();
            }
            AttributesManager attributesManager = handlerInput.getAttributesManager();
            attributesManager.setSessionAttributes(Collections.singletonMap("State", stateToSet));
            speechText = SpeechStrings.HELP_DESTINATION_ADDRESS;
            repromptText = "ASHJFDASJHK";
        } else {
            repromptText = "Bitte wiederhole nochmal was du gesagt hast? Möchtest du mit den Infos zur Heimadresse weiterfahren oder die Hilfefunktion beenden?";
            speechText = "Leider hat etwas nicht geklappt, bis sage mir nochmal ob du Infos zur Heimadresse oder die Hilfefunktion beenden willst";
            askResponse = true;
        }

        respBuilder.withSimpleCard("Hilfe Zieladresse", "Hilfe Zieladresse")
                .withSpeech(SpeechStrings.HELP_DESTINATION_ADDRESS)
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
