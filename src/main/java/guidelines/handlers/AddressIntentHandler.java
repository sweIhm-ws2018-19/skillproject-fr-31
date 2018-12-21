package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import guidelines.models.Coordinate;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.BasicUtils;
import guidelines.utilities.HereApi;

import java.util.*;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class AddressIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AddressIntent")
                .and(sessionAttribute(GuideStates.getStateString(), GuideStates.GET_DEST_ADDR.toString())
                        .or(sessionAttribute(GuideStates.getStateString(), GuideStates.GET_HOME_ADDR.toString()))
                ));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        AttributesManager attributesManager = input.getAttributesManager();
        GuideStates currentState = GuideStates.valueOf(input.getAttributesManager().getSessionAttributes().get(GuideStates.getStateString()).toString());

        Map<String, Slot> slots = BasicUtils.getSlots(input);
        Slot citySlot = slots.get("city");
        Slot streetSlot = slots.get("street");
        Slot streetNumberSlot = slots.get("streetNumber");

        String speechText;


        if (citySlot != null && streetSlot != null && streetNumberSlot != null) {
            String cityValue = citySlot.getValue();
            String streetValue = streetSlot.getValue();
            String streetNumberValue = streetNumberSlot.getValue();


            final Coordinate coordinates = HereApi.getCoordinate(streetValue, Integer.valueOf(streetNumberValue),
                    cityValue);
            if (coordinates == null) {
                BasicUtils.setSessionAttributes(attributesManager, GuideStates.getStateString(), currentState);
                return input.getResponseBuilder()
                        .withSpeech("Ich habe dich leider nicht verstanden. Bitte geben die Adresse nochmal ein")
                        .withReprompt("Ich habe dich leider nicht verstanden. Bitte geben die Adresse nochmal ein")
                        .withShouldEndSession(false)
                        .build();
            }

            if (currentState == GuideStates.GET_HOME_ADDR) {

                Map<String, Double> myMap = new HashMap<>();
                myMap.put("latitude", coordinates.getLatitude());
                myMap.put("longitude", coordinates.getLongitude());
                BasicUtils.setPersistentAttributes(attributesManager, "HOME", myMap);
                return Setup.setupState(input);
            } else {
                Map<String, Coordinate> nearbyStations = HereApi.getNearbyStations(coordinates);
                BasicUtils.setSessionAttributes(attributesManager, "Stations", nearbyStations);


                BasicUtils.setSessionAttributes(attributesManager, GuideStates.getStateString(), GuideStates.SAY_DEST_ADDR_AGAIN);
                speechText = "Du hast mir folgende Adresse mitgeteilt: " + streetValue + ", " + streetNumberValue + ", " +
                        cityValue + ". Moechtest du deine Eingabe wiederholen?";
                FallbackIntentHandler.setFallbackMessage(speechText);
            }

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
}
