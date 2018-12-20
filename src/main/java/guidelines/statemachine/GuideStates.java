package guidelines.statemachine;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import guidelines.SpeechStrings;
import guidelines.handlers.AddressIntentHandler;
import guidelines.handlers.FallbackIntentHandler;
import guidelines.utilities.BasicUtils;
import guidelines.utilities.StringUtils;

import java.util.Collections;
import java.util.Optional;

public enum GuideStates {
    CONFIG, CONFIG_AVAILABLE, INSERT_NAME, LAUNCH_STATE, HOME_ADDR, GET_DEST_NAME,
    GET_HOME_ADDR, Q_NEXT_ADDR,
    GET_DEST_ADDR, HELP, HELP_DEST, HELP_HOME, TRANSIT, USE_GPS_OR_NOT, YES, HOME_ADDR_NAME, NO, SELECT_NEARBY_STATION,
    SAY_DEST_ADDR_AGAIN;

    public static Optional<Response> decisionBuilder(GuideStates state, boolean isYes, AttributesManager attributesManager){
        ResponseBuilder respBuilder = new ResponseBuilder();
        switch (state){
            case SAY_DEST_ADDR_AGAIN:
            {
                if(isYes){
                    attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.GET_DEST_ADDR));
                    String speechText = "Alles klar. Bitte sag mir nochmal die Strasse, Hausnummer und Stadt";
                    FallbackIntentHandler.setFallbackMessage(speechText);
                    respBuilder
                            .withSpeech(speechText)
                            .withReprompt("Bitte sag mir nochmal die Strasse, Hausnummer und Stadt")
                            .withShouldEndSession(false)
                            .build();
                }else{
                    String stationsToSelect = StringUtils.prepStringForChoiceIntent(AddressIntentHandler.getStationNames());
                    attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.SELECT_NEARBY_STATION));
                    String speechText = "Alles klar. Ich sage dir jetzt die Stationen die du zur Auswahl hast. Merke dir" +
                            " bitte die zugehörige Nummer der Station die du benutzen möchtest. " +
                            "Station: " + stationsToSelect +
                            " Zur Auswahl sage jetzt bitte die Nummer der Gewünschten Haltestelle.";
                    FallbackIntentHandler.setFallbackMessage(speechText);
                    respBuilder
                            .withSpeech(speechText)
                            .withReprompt(stationsToSelect)
                            .withShouldEndSession(false)
                            .build();
                }
                break;

            }
            case Q_NEXT_ADDR:{
                if(isYes){
                    attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.GET_DEST_ADDR));
                    respBuilder = BasicUtils.putTogether("Neue Adresse", SpeechStrings.FOLLOWING_ADDRESSES+" "+SpeechStrings.SAY_ADDRESS);
                }else{
                    String speech = " Die Einrichtung waere hiermit vorerst abgeschlossen. " +
                            "Du kannst nun die Hilfefunktion aufrufen oder eine Route erfragen";
                    attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.TRANSIT));
                    respBuilder = BasicUtils.putTogether("Route", speech);
                }
                break;
            }

        }
        return respBuilder.build();
    }
}
