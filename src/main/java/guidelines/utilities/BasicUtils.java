package guidelines.utilities;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;

import java.util.Map;

public final class BasicUtils {
    private BasicUtils(){};
    public static Map<String,Slot> getSlots(HandlerInput input){
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();
        return slots;
    }
}
