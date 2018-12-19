package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import guidelines.statemachine.GuideStates;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;
import static guidelines.handlers.LaunchRequestHandler.getDeviceAddressJson;

public class DestNameIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("DestNameIntent").and(sessionAttribute("State", GuideStates.DEST_NAME.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();
        Slot destNameSlot = slots.get("destCustomName");

        if (destNameSlot != null) {
            String destName = destNameSlot.getValue();

            AttributesManager attributesManager = input.getAttributesManager();
            attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.TRANSIT));
            Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();

            double latitude = DestAddressIntentHandler
                    .getNearbyStations()
                    .get(DestAddressIntentHandler.getStationNames().get(DestChoiceIntentHandler.getDestChoice() - 1))
                    .getLatitude();
            double longitude = DestAddressIntentHandler
                    .getNearbyStations()
                    .get(DestAddressIntentHandler.getStationNames().get(DestChoiceIntentHandler.getDestChoice()  - 1))
                    .getLongitude();

            persistentAttributes.put("Zuhause", getDeviceAddressJson());
            persistentAttributes.put("NAME", MyNameIsIntentHandler.getName());
            persistentAttributes.put(destName + "x", latitude);
            persistentAttributes.put(destName + "y", longitude);
            attributesManager.setPersistentAttributes(persistentAttributes);
            attributesManager.savePersistentAttributes();

            return input.getResponseBuilder()
                    .withSpeech("Deine gewuenschte Zielstation ist nun unter den Namen: " + destName + " gespeichert." +
                            " Die Einrichtung waere hiermit vorerst abgeschlossen. " +
                            "Du kannst nun die Hilfefunktion aufrufen oder eine Route erfragen")
                    .withReprompt("Bitte sage uns nochmal den Namen des Ziels")
                    .build();
        }

        return input.getResponseBuilder()
                .withSpeech("Leider hat das Befüllen der Slots nicht richtig funktioniert")
                .withReprompt("Bitte mache die Eingabe der Slots erneut")
                .build();
    }
}
