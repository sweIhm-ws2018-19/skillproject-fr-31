package main.java.guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import main.java.guidelines.SpeechStrings;
import main.java.guidelines.stateMachine.GuideStates;

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
        return handlerInput.getResponseBuilder()
                .withSimpleCard("Hilfe Zieladresse", "Hilfe Zieladresse")
                .withSpeech(SpeechStrings.HELP_DESTINATION_ADDRESS)
                .withReprompt(SpeechStrings.REPROMPT)
                .withShouldEndSession(false)
                .build();
    }
}
