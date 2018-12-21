package guidelines.handlers;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import guidelines.SpeechStrings;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.BasicUtils;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class ConfigurationHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ConfigurationIntent")
                .and(sessionAttribute(GuideStates.getStateString(), GuideStates.TRANSIT.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder respBuilder = new ResponseBuilder();

        respBuilder.withSpeech(SpeechStrings.NEW_STREET)
            .withReprompt(SpeechStrings.NEW_STREET)
            .withShouldEndSession(false);

        BasicUtils.setSessionAttributes(input.getAttributesManager(), GuideStates.getStateString(), GuideStates.CONFIG);

        return respBuilder.build();
    }
}
