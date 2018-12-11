package guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import guidelines.stateMachine.GuideStates;

import java.util.Map;
import java.util.Optional;

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

        if(citySlot != null && streetSlot != null && streetNumberSlot != null && postalCodeSlot != null){
            String cityValue = citySlot.getValue();
            String streetValue = streetSlot.getValue();
            String streetNumberValue = streetNumberSlot.getValue();
            String postalCode = postalCodeSlot.getValue();
            return input.getResponseBuilder().withSpeech(cityValue + " - " +streetValue + " - " + streetNumberValue + " - " + postalCode).build();
        }else{
            return input.getResponseBuilder().withSpeech("DU VOLLIDIOT DU AHST EINEN FEHLER GEMACHT").withReprompt("SPACKEN").build();
        }
    }
}
