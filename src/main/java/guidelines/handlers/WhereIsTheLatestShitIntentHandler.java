package main.java.guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class WhereIsTheLatestShitIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("WhereIsTheLatestShitIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        ResponseBuilder builder = handlerInput.getResponseBuilder();
        builder.withSpeech("<audio src=\"https://landesbank.bayern/latestshit_good.mp3\" /> ");
        return builder.build();
    }
}
