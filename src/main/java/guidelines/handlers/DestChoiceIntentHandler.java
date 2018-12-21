package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import guidelines.models.Coordinate;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.BasicUtils;
import guidelines.utilities.StringUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class DestChoiceIntentHandler implements RequestHandler {

    private static int destChoice;

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("DestChoiceIntent").and(sessionAttribute(GuideStates.STATE, GuideStates.SELECT_NEARBY_STATION.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();
        Slot choiceSlot = slots.get("choice");

        AttributesManager attributesManager = input.getAttributesManager();

        String speechText;

        if (choiceSlot != null) {
            String choiceValue = choiceSlot.getValue();
            int choice = Integer.parseInt(choiceValue);
            setDestChoice(choice -1);
            BasicUtils.setSessionAttributes(attributesManager,GuideStates.STATE, GuideStates.GET_DEST_NAME);
            Map<String, Coordinate> stations = (Map<String, Coordinate>)attributesManager.getSessionAttributes().get("Stations");
            ArrayList<String> stationNames = new ArrayList<>(stations.keySet());
            if (destChoice > stations.size()){
                String stationsToSelect = StringUtils.prepStringForChoiceIntent(stationNames);
                speechText = "Du hast ein Ziel ausgewählt das nicht exsistiert. Bitte Wähle erneut. Hier sind nocheinmal die möglichkeiten. "+stationsToSelect;
            }
            else{
                speechText = "Deine Wahl faellt auf " + stationNames.get(destChoice) +
                        ". Welchen benutzerdefinierten Namen moechtest du der Station geben? Sage hierzu: Mein Ziel " +
                        "heisst: plus den Namen";
            }
            FallbackIntentHandler.setFallbackMessage(speechText);

            return input.getResponseBuilder()
                    .withSpeech(speechText)
                    .withShouldEndSession(false)
                    .withReprompt("Bitte sage mir den Namen fuer deine gewuenschte Zielstation. Sage hierzu: Mein Ziel heisst: " +
                            "plus den Namen")
                    .build();
        } else {
            speechText = "Leider hat das Befüllen der Slots nicht richtig funktioniert";
            FallbackIntentHandler.setFallbackMessage(speechText);
            return input.getResponseBuilder()
                    .withSpeech("Leider hat das Befüllen der Slots nicht richtig funktioniert")
                    .withReprompt("Bitte mache die Eingabe der Slots erneut").build();
        }
    }

    static int getDestChoice() {
        return destChoice;
    }

    private static void setDestChoice(int destChoice) {
        DestChoiceIntentHandler.destChoice = destChoice;
    }
}
