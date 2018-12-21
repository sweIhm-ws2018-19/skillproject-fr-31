package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import guidelines.models.Coordinate;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.BasicUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class DestNameIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("DestNameIntent").and(sessionAttribute("State", GuideStates.GET_DEST_NAME.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();
        Slot destNameSlot = slots.get("destCustomName");

        String speechText = "";
        String repromt = "Bitte sage uns nochmal den Namen des Ziels";

        AttributesManager attributesManager = input.getAttributesManager();
        if (destNameSlot != null) {

            String destName = destNameSlot.getValue();

            if (destName != null) {

                Map<String, Coordinate> stations = (Map<String, Coordinate>) attributesManager.getSessionAttributes().get("Stations");
                ArrayList<String> keys = new ArrayList<>(stations.keySet());
                GuideStates stateToSet = GuideStates.Q_NEXT_ADDR;
                if (attributesManager.getPersistentAttributes().get("DEST1") == null) {

                    // so sollte es funktionieren
                    BasicUtils.setPersistentAttributes(attributesManager,destName,stations.get(keys.get(DestChoiceIntentHandler.getDestChoice() - 1)));


//                    speechText = "Deine gewuenschte Zielstation ist nun unter den Namen: "+stations.get(keys.get(0))+ keys.get(0)+destName
//                            + " gespeichert. Noch eine Adresse eingeben?";
                    speechText = "Deine gewuenschte Zielstation ist nun unter den Namen: "+ destName
                            + " gespeichert. Noch eine Adresse eingeben?";

                    // default state q_next

                } else if (attributesManager.getPersistentAttributes().get("DEST2") == null) {

                    BasicUtils.setPersistentAttributes(attributesManager,"DEST2", "asdf2");
                    speechText = "Deine gewuenschte Zielstation ist nun unter den Namen: " + destName
                            + " gespeichert. Noch eine Adresse eingeben?";
                    // default state q_next

                } else if (attributesManager.getPersistentAttributes().get("DEST3") == null) {
                    BasicUtils.setPersistentAttributes(attributesManager,"DEST3", "asdf3");
                    speechText = "Deine gewuenschte Zielstation ist nun unter den Namen: " + destName + " gespeichert." +
                            " Die Einrichtung waere hiermit vorerst abgeschlossen. " +
                            "Du kannst nun die Hilfefunktion aufrufen oder eine Route erfragen";
                    stateToSet = GuideStates.TRANSIT;
                } else {
                    speechText = "Error";
                    // Todo: all dest Addresses are set.
                    //      - Oeveride?
                    //      - Error?
                    //      - Do nothing?
                }

                BasicUtils.setSessionAttributes(attributesManager,"State", stateToSet);
            } else {
                speechText = "Leider hab ich den Namen nicht verstanden";
                repromt = "Bitte sag den Namen erneut";
                FallbackIntentHandler.setFallbackMessage(speechText);
            }

            FallbackIntentHandler.setFallbackMessage(speechText);
        } else {
            speechText = "Leider hab ich den Namen nicht verstanden";
            repromt = "Bitte sag den Namen erneut";
            FallbackIntentHandler.setFallbackMessage(speechText);
        }

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withReprompt(repromt)
                .build();
    }
}
