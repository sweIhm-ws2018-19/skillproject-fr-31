package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.StringUtils;

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

        String speechText;

        if (yesOrNoSlot != null) {
            String yesOrNo = yesOrNoSlot.getValue();
            if (yesOrNo.equals("nein")) {
                String stationsToSelect = StringUtils.prepStringForChoiceIntent(AddressIntentHandler.getStationNames());
                attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.SELECT_NEARBY_STATION));
                speechText = "Alles klar. Ich sage dir jetzt die Stationen die du zur Auswahl hast. Merke dir" +
                        " bitte die zugehörige Nummer der Station die du benutzen möchtest. " +
                        "Station: " + stationsToSelect + " als Zielstation einrichten? " +
                        "Zur Auswahl sage jetzt bitte deine Nummer.";
                FallbackIntentHandler.setFallbackMessage(speechText);
                return responseBuilder
                        .withSpeech(speechText)
                        .withReprompt("Waehle die eins, zwei oder drei")
                        .withShouldEndSession(false)
                        .build();
            } else {
                attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.GET_DEST_ADDR));
                speechText = "Alles klar. Bitte sag mir nochmal die Strasse, Hausnummer und Stadt";
                FallbackIntentHandler.setFallbackMessage(speechText);
                return responseBuilder
                        .withSpeech(speechText)
                        .withReprompt("Bitte sag mir nochmal die Strasse, Hausnummer und Stadt")
                        .withShouldEndSession(false)
                        .build();
            }
        }

        return responseBuilder.withSpeech("DIE BEFUELLUNG HAT NICHT GEKLAPPT").build();
    }
}
