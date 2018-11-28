package main.java.guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import main.java.guidelines.SpeechStrings;
import main.java.guidelines.stateMachine.GuideStates;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class HomeAddressHelpIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("HomeAddressHelpIntent").and(sessionAttribute("State", GuideStates.HELP_HOME.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        return handlerInput.getResponseBuilder()
                .withSimpleCard("Hilfe Heimadresse", "Hilfe Heimadresse")
                .withSpeech(SpeechStrings.HELP_HOME_ADDRESS)
                .withReprompt(SpeechStrings.REPROMPT)
                .withShouldEndSession(false)
                .build();
    }
}
