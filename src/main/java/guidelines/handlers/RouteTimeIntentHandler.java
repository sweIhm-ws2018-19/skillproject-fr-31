package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import guidelines.models.Coordinate;
import guidelines.models.Route;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.HereApi;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class RouteTimeIntentHandler implements RequestHandler {

    private static final String MINUTE = " Minuten";

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("RouteTimeIntent").and(sessionAttribute(GuideStates.STATE.getKey(), GuideStates.ROUTE_TIME.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();
        Slot timeSlot = slots.get("time");

        DateTime dt = new DateTime();
        DateTimeFormatter patternFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'");
        String todayDate = dt.toString(patternFormat);

        if (timeSlot != null) {
            AttributesManager attributesManager = input.getAttributesManager();
            final Map<String, Object> home = (Map<String, Object>) attributesManager.getPersistentAttributes().get("HOME");

            final BigDecimal homeLatitudeBD = (BigDecimal) home.get("latitude");
            final double homeLatitude = homeLatitudeBD.doubleValue();
            final BigDecimal homeLongitudeBD = (BigDecimal) home.get("longitude");
            final double homeLongitude = homeLongitudeBD.doubleValue();
            final Map<String, Object> dest = (Map<String, Object>) attributesManager.getPersistentAttributes().get("DEST");
            final Map<String, Object> decision = (Map<String, Object>) dest.get(RouteStartIntentHandler.getDestinationName());
            final BigDecimal destLatitudeBD = (BigDecimal) decision.get("latitude");
            final double destLatitude = destLatitudeBD.doubleValue();
            final BigDecimal destLongitudeBD = (BigDecimal) decision.get("longitude");
            final double destLongitude = destLongitudeBD.doubleValue();

            final Coordinate homeCoordinate = new Coordinate(homeLatitude, homeLongitude);
            final Coordinate destCoordinate = new Coordinate(destLatitude, destLongitude);

            final String time = todayDate + timeSlot.getValue() + ":00+01:00";

            final Route route = HereApi.getRoute(homeCoordinate, destCoordinate, time);


            int minutes = route.getMinutesLeft() % 60;
            int hours = route.getMinutesLeft() / 60;
            String speechText = "Du solltest in %s losgehen um das Verkehrsmittel "
                    + route.getTransport() + " an der Station " + route.getFirstStation() + " zu erreichen." +
                    " Dein Verkehrsmittel fährt um " + route.getTransTime() + " los. Ich wünsche dir eine gute Fahrt";

            if(hours > 0){
                if(hours == 1)
                    speechText = String.format(speechText, ("einer Stunde und " + minutes + MINUTE));
                else
                    speechText = String.format(speechText, (hours + " Stunden und " + minutes + MINUTE));
            }
            else{
                speechText = String.format(speechText, minutes + MINUTE);
            }


            if(route.getMinutesLeft() == 0){
                speechText = "Du solltest jetzt los gehen um das Verkehrsmittel " + route.getTransport() + " an der Station " + route.getFirstStation()
                        + " zu erreichen. Dein Verkehrsmittel fährt um " + route.getTransTime() + " los. Ich wünsche dir eine gute Fahrt";
            }
            if(route.getMinutesLeft() < 0){
                speechText = "Mit den öffentlichen Verkehrsmittel erreichst du dein Ziel nicht mehr rechtzeitig. Auf Wiedersehen";
            }

            attributesManager.setSessionAttributes(Collections.singletonMap(GuideStates.STATE.getKey(), GuideStates.TRANSIT));
            FallbackIntentHandler.setFallbackMessage(speechText);
            return input.getResponseBuilder()
                    .withSpeech(speechText)
                    .withReprompt("Bitte nenne mir nochmals die Uhrzeit")
                    .withShouldEndSession(true)
                    .build();
        }
        return input.getResponseBuilder()
                .withSpeech("Bei der Eingabe der Uhrzeit hat etwas nicht geklappt")
                .build();
    }
}
