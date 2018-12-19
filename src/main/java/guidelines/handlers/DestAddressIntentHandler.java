package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import guidelines.models.Coordinate;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.HereApi;

import java.util.*;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class DestAddressIntentHandler implements RequestHandler {

    private static List<String> stationNames;
    private static Map<String, Coordinate> nearbyStations;

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

        String speechText;

        if (citySlot != null && streetSlot != null && streetNumberSlot != null) {
            String cityValue = citySlot.getValue();
            String streetValue = streetSlot.getValue();
            String streetNumberValue = streetNumberSlot.getValue();

            AttributesManager attributesManager = input.getAttributesManager();

            final Coordinate coordinates = HereApi.getCoordinate(streetValue, Integer.valueOf(streetNumberValue),
                    cityValue);
            if (coordinates == null) {
                attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.DEST_ADDR));
                return input.getResponseBuilder()
                        .withSpeech("Ich habe dich leider nicht verstanden. Bitte geben die Adresse nochmal ein")
                        .withReprompt("Ich habe dich leider nicht verstanden. Bitte geben die Adresse nochmal ein")
                        .withShouldEndSession(false)
                        .build();
            }
            nearbyStations = HereApi.getNearbyStations(coordinates);
            stationNames = new ArrayList<>(HereApi.getNearbyStations(coordinates).keySet());

            attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.SAY_DEST_ADDR_AGAIN));
            speechText = "Du hast mir folgende Adresse mitgeteilt: " + streetValue + ", " + streetNumberValue + ", " +
                    cityValue + ". Moechtest du deine Eingabe wiederholen?";
            FallbackIntentHandler.setFallbackMessage(speechText);

            return input.getResponseBuilder()
                    .withSpeech(speechText)
                    .withReprompt("Moechtest du deine Eingabe wiederholen?")
                    .withShouldEndSession(false)
                    .build();
        } else {
            speechText = "Leider hat das Befüllen der Slots nicht richtig funktioniert";
            FallbackIntentHandler.setFallbackMessage(speechText);
            return input.getResponseBuilder()
                    .withSpeech(speechText)
                    .withReprompt("Bitte mache die Eingabe der Slots erneut").build();
        }
    }

    static List<String> getStationNames() {
        return stationNames;
    }

    static Map<String, Coordinate> getNearbyStations() {
        return nearbyStations;
    }
}
