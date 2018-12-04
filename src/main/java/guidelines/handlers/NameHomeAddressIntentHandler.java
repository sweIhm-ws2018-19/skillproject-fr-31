package main.java.guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import main.java.guidelines.SpeechStrings;
import main.java.guidelines.stateMachine.GuideStates;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class NameHomeAddressIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("NameHomeAddressIntent").and(sessionAttribute("State", GuideStates.HOME_ADDR_NAME.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();
        Slot homeAddressName = slots.get("homeAddressName");

        String speechText = homeAddressName.getName();
        String repromptText = "Mal schauen";
        boolean askResponse = false;

        ResponseBuilder respBuilder = input.getResponseBuilder();
        respBuilder.withSimpleCard("Session", SpeechStrings.SKILL_NAME)
                .withSpeech(speechText)
                .withShouldEndSession(false);

        if (askResponse) {
            respBuilder.withShouldEndSession(false)
                    .withReprompt(repromptText);
        }

        return respBuilder.build();
    }
}
