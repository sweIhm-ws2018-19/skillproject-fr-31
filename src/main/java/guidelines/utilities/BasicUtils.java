package guidelines.utilities;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;

import java.util.Map;

public final class BasicUtils {

    public static Map<String,Slot> getSlots(HandlerInput input){
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        return intent.getSlots();
    }
    public  static ResponseBuilder putTogether(String title, String text){
        return new ResponseBuilder().withSimpleCard(title,text)
                .withSpeech(text)
                .withReprompt(text);
    }

    public static void setSessionAttributes(AttributesManager attributes, String key, Object value){
        Map<String, Object> currentAttributes = attributes.getSessionAttributes();
        currentAttributes.put(key, value);
        attributes.setSessionAttributes(currentAttributes);
    }

    public static void setPersistentAttributes(AttributesManager attributes, String key, Object value){
        Map<String, Object> currentAttribues = attributes.getPersistentAttributes();
        currentAttribues.put(key, value);
        attributes.setPersistentAttributes(currentAttribues);
        attributes.savePersistentAttributes();
    }
}
