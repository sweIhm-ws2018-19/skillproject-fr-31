package main.java.guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import main.java.guidelines.stateMachine.GuideStates;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class UserConfigIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("UserConfigIntent").and(sessionAttribute("State", GuideStates.CONFIG)));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        // ToDo:
        //   AskforName
        //   switch to insert_name state

        return Optional.empty();
    }
}
