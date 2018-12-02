package main.java.guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;
import java.util.Optional;
import main.java.guidelines.stateMachine.GuideStates;

// Currently only working for USE GPS OR NOT
public class NoIntentHandler implements RequestHandler{

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.NoIntent").and(sessionAttribute("State", GuideStates.USE_GPS_OR_NOT.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return null;
    }
    
}