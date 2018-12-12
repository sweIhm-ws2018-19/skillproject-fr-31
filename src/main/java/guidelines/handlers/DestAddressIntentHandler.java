package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import guidelines.models.Coordinate;
import guidelines.stateMachine.GuideStates;
import guidelines.utilities.HereApi;

import java.util.*;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class DestAddressIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("DestAddressIntent").and(sessionAttribute("State", GuideStates.DEST_ADDR.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();
        Slot citySlot = slots.get("city");
        Slot streetSlot = slots.get("street");
        Slot streetNumberSlot = slots.get("streetNumber");
        Slot postalCodeSlot = slots.get("postalCode");

        if (citySlot != null && streetSlot != null && streetNumberSlot != null && postalCodeSlot != null) {
            String cityValue = citySlot.getValue();
            String streetValue = streetSlot.getValue();
            String streetNumberValue = streetNumberSlot.getValue();
            String postalCode = postalCodeSlot.getValue();

            AttributesManager attributesManager = input.getAttributesManager();
            attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.SELECT_NEARBY_STATION));
            final Coordinate coordinates = HereApi.getCoordinate(streetValue, Integer.valueOf(streetNumberValue),
                    cityValue, Integer.valueOf(postalCode));
            List<String> stationNames = new ArrayList<>(HereApi.getNearbyStations(coordinates).keySet());



            return input.getResponseBuilder()
                    .withSpeech("Du hast uns folgende Adresse genannt: " + streetValue + " "
                            + streetNumberValue + " " + postalCode + " " + cityValue + ". Als naechstes waehlen wir " +
                            "eine Zielstation aus. Moechtest du " + stationNames.get(0) + ", " + stationNames.get(1) +
                            " oder " + stationNames.get(2) + " als Zielstation einrichten?")
                    .withReprompt("Bitte gebe die Adresse an deines erstes Ziels")
                    .withShouldEndSession(false)
                    .build();
        } else {
            return input.getResponseBuilder()
                    .withSpeech("Leider hat das Befüllen der Slots nicht richtig funktioniert")
                    .withReprompt("Bitte mache die Eingabe der Slots erneut").build();
        }
    }
}
