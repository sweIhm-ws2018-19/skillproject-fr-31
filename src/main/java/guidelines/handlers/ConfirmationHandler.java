package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.BasicUtils;

import java.util.*;

import static com.amazon.ask.request.Predicates.intentName;

public class ConfirmationHandler implements RequestHandler {

    ArrayList<GuideStates> confirmationStates=new ArrayList<>(Arrays.asList(GuideStates.SAY_DEST_ADDR_AGAIN,GuideStates.Q_NEXT_ADDR, GuideStates.CONFIG));
    private boolean inConfirmationStates(HandlerInput input){
        return confirmationStates.contains(
                GuideStates.valueOf(input.getAttributesManager().getSessionAttributes().get("State").toString()));

    }
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ConfirmationIntent")
                .and(this::inConfirmationStates));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder responseBuilder = input.getResponseBuilder();

        Map<String, Slot> slots = BasicUtils.getSlots(input);
        Slot yesOrNoSlot = slots.get("decision");

        AttributesManager attributesManager = input.getAttributesManager();

        String speechText;

        if (yesOrNoSlot != null) {
            String yesOrNo = yesOrNoSlot.getValue();
            GuideStates currentState = GuideStates.valueOf(attributesManager.getSessionAttributes().get("State").toString());
            if (yesOrNo.equals("nein")) {
                return GuideStates.decisionBuilder(currentState,false, input);
            } else {
                return GuideStates.decisionBuilder(currentState,true, input);
            }
        }

        return responseBuilder.withSpeech("DIE BEFUELLUNG HAT NICHT GEKLAPPT").build();
    }
}
