package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import guidelines.models.Coordinate;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.BasicUtils;

import java.util.*;

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

                int choice = DestChoiceIntentHandler.getDestChoice();
                if (attributesManager.getPersistentAttributes().get("DEST") == null) {

                    // so sollte es funktionieren
                    Map<String, Object> newMap = new HashMap<>();
                    newMap.put(destName, stations.get(keys.get(choice)));
                    BasicUtils.setPersistentAttributes(attributesManager, "DEST", newMap);

//                    speechText = "Deine gewuenschte Zielstation ist nun unter den Namen: "+stations.get(keys.get(0))+ keys.get(0)+destName
//                            + " gespeichert. Noch eine Adresse eingeben?";

                    // default state q_next

                } else {
                    Map<String,Object> attributeMap = (Map<String,Object>)attributesManager.getPersistentAttributes().get("DEST");
                    attributeMap.put(destName,stations.get(keys.get(choice)));
                    BasicUtils.setPersistentAttributes(attributesManager,"DEST", attributeMap);
                }


                speechText = "Deine gewuenschte Zielstation ist nun unter den Namen: " + destName
                        + " gespeichert. Noch eine Adresse eingeben?";

                BasicUtils.setSessionAttributes(attributesManager, "State", GuideStates.Q_NEXT_ADDR);

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
