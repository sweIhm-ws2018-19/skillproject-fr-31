package guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import guidelines.stateMachine.GuideStates;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class ChoiceIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ChoiceIntent").and(sessionAttribute("State", GuideStates.SELECT_NEARBY_STATION.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();
        Slot choiceSlot = slots.get("choice");

        if (choiceSlot != null) {
            String choiceValue = choiceSlot.getValue();

            int choice = Integer.valueOf(choiceValue);

            return input.getResponseBuilder()
                    .withSpeech("Deine Wahl faellt auf " + DestAddressIntentHandler.stationNames.get(choice - 1))
                    .build();
        } else {
            return input.getResponseBuilder()
                    .withSpeech("Leider hat das Befüllen der Slots nicht richtig funktioniert")
                    .withReprompt("Bitte mache die Eingabe der Slots erneut").build();
        }


    }
}
