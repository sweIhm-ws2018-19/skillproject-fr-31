package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import guidelines.stateMachine.GuideStates;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class SayDestAddressAgainIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("SayDestAddressAgain").and(sessionAttribute("State", GuideStates.SAY_DEST_ADDR_AGAIN.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder responseBuilder = input.getResponseBuilder();
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();
        Slot yesOrNoSlot = slots.get("decision");

        AttributesManager attributesManager = input.getAttributesManager();


        if (yesOrNoSlot != null) {
            String yesOrNo = yesOrNoSlot.getValue();
            if (yesOrNo.equals("ja")) {
                attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.DEST_ADDR));
                return responseBuilder
                        .withSpeech("Alles klar. Bitte sag mir nochmal die Strasse, Hausnummer und Stadt")
                        .withReprompt("Bitte sag mir nochmal die Strasse, Hausnummer und Stadt")
                        .withShouldEndSession(false)
                        .build();

            } else {
                attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.SELECT_NEARBY_STATION));
                return responseBuilder
                        .withSpeech("Alles klar. Moechtest du " + DestAddressIntentHandler.stationNames.get(0) + ", " + DestAddressIntentHandler.stationNames.get(1) +
                                " oder " + DestAddressIntentHandler.stationNames.get(2) + " als Zielstation einrichten? Zur Auswahl sage: eins, zwei" +
                                " oder drei.")
                        .withReprompt("Waehle die eins, zwei oder drei")
                        .withShouldEndSession(false)
                        .build();
            }
        }

        return responseBuilder.withSpeech("DIE BEFUELLUNG HAT NICHT GEKLAPPT").build();
    }
}
