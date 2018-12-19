package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import guidelines.SpeechStrings;
import guidelines.statemachine.GuideStates;

import java.util.Collections;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class BackToStartIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("BackToStartIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        String speechText = "Du kannst nun wieder die Hilfefunktion aufrufen oder eine Route erfragen";

        AttributesManager attributesManager = handlerInput.getAttributesManager();
        attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.TRANSIT.toString()));


        FallbackIntentHandler.setFallbackMessage(speechText);
        return handlerInput.getResponseBuilder()
                .withSimpleCard(SpeechStrings.SKILL_NAME, "Back to the beginning")
                .withSpeech(speechText)
                .withReprompt(speechText)
                .withShouldEndSession(false)
                .build();
    }
}
