package guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import guidelines.statemachine.GuideStates;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class RouteStartIntentHandler implements RequestHandler {

    private static String destinationName;

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("RouteStartIntent").and(sessionAttribute(GuideStates.STATE.getKey(), GuideStates.TRANSIT.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        Request request = handlerInput.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();
        Slot destinationSlot = slots.get("destination");

        if(destinationSlot != null){
            String speechText = "Um wie viel Uhr moechtest du am Ziel: " + destinationSlot.getValue() + ", sein?";
            handlerInput.getAttributesManager().setSessionAttributes(Collections.singletonMap(GuideStates.STATE.getKey(), GuideStates.ROUTE_TIME));
            setDestinationName(destinationSlot.getValue());
            FallbackIntentHandler.setFallbackMessage(speechText);
            return handlerInput.getResponseBuilder()
                    .withSpeech(speechText)
                    .withShouldEndSession(false)
                    .build();
        }

        return Optional.empty();
    }

    public static String getDestinationName() {
        return destinationName;
    }

    private static void setDestinationName(String destinationName) {
        RouteStartIntentHandler.destinationName = destinationName;
    }
}
