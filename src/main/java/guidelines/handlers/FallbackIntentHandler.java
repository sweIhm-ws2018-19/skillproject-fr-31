package guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import guidelines.SpeechStrings;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class FallbackIntentHandler implements RequestHandler {

    private static String fallbackMessage = "Default Fallback Message";

    @Override
    public boolean canHandle(HandlerInput input) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
                .withSpeech(getFallbackMessage())
                .withSimpleCard(SpeechStrings.SKILL_NAME, getFallbackMessage())
                .withReprompt(getFallbackMessage())
                .build();
    }

    public static String getFallbackMessage() {
        return fallbackMessage;
    }

    public static void setFallbackMessage(String fallbackMessage) {
        FallbackIntentHandler.fallbackMessage = fallbackMessage;
    }
}
