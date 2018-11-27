package main.java.guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class MyNameIsIntentHandler implements RequestHandler {
    public static final String NAME_KEY = "NAME";
    public static final String NAME_SLOT = "name";

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("MyNameIsIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();
        Slot nameSlot = slots.get(NAME_SLOT);

        String speechText;
        String repromptText;
        boolean askResponse = false;

        if(nameSlot != null) {
            String name = nameSlot.getValue();
            AttributesManager attributesManager = input.getAttributesManager();
            // store in session
            attributesManager.setSessionAttributes(Collections.singletonMap(NAME_KEY, name));
            // store in database
            Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();
            persistentAttributes.put(NAME_KEY, name);
            attributesManager.setPersistentAttributes(persistentAttributes);
            attributesManager.savePersistentAttributes();

            speechText = "Vielen dank <break time=\"1s\"/>"+name;
            repromptText = "Bitte jetzt deine Straße angeben";
        } else {
            speechText = "Leider habe ich das nicht verstanden. Versuche bitte erneut deinen Name zu sagen";
            repromptText =
                "Ich weiß deinen Namen noch nicht. Kannst du ihn mir veraten. Sage zum Beispiel: ich heiße Anita.";
            askResponse = true;
        }

        ResponseBuilder respBuilder = input.getResponseBuilder();
        respBuilder.withSimpleCard("Session", speechText)
                .withSpeech(speechText)
                .withShouldEndSession(false);

        if (askResponse) {
            respBuilder.withShouldEndSession(false)
                    .withReprompt(repromptText);
        }

        return respBuilder.build();
    }
}
