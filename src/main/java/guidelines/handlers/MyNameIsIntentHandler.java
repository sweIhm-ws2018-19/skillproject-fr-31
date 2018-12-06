package guidelines.handlers;

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

public class MyNameIsIntentHandler implements RequestHandler {

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

        if(nameSlot != null) {
            String name = nameSlot.getValue();
            AttributesManager attributesManager = input.getAttributesManager();
            // store in session
            attributesManager.setSessionAttributes(Collections.singletonMap("NAME", name));
            // store in database
            Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();
            persistentAttributes.put("NAME", name);
            attributesManager.setPersistentAttributes(persistentAttributes);
            attributesManager.savePersistentAttributes();

            speechText = SpeechStrings.THANKS+name;
            repromptText = name + SpeechStrings.PLS+SpeechStrings.STREET;
        } else {
            speechText = SpeechStrings.INAUDIBLE +  " Versuche bitte erneut deinen Name zu sagen";
            repromptText = SpeechStrings.NAMEUNKNOWN;
            askResponse = true;
        }

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
