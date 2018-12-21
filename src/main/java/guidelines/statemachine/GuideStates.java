package guidelines.statemachine;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import guidelines.SpeechStrings;
import guidelines.handlers.FallbackIntentHandler;
import guidelines.handlers.Setup;
import guidelines.models.Coordinate;
import guidelines.utilities.BasicUtils;
import guidelines.utilities.StringUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public enum GuideStates {
    CONFIG, NEW_CONFIG, INSERT_NAME, LAUNCH_STATE, GET_DEST_NAME,
    GET_HOME_ADDR, Q_NEXT_ADDR,
    GET_DEST_ADDR, HELP, TRANSIT, SELECT_NEARBY_STATION,
    SAY_DEST_ADDR_AGAIN, ROUTE_TIME;

    public static Optional<Response> decisionBuilder(GuideStates state, boolean isYes, HandlerInput input){
        AttributesManager attributesManager = input.getAttributesManager();
        ResponseBuilder respBuilder = new ResponseBuilder();
        switch (state){
            case SAY_DEST_ADDR_AGAIN:
            {
                if(isYes){
                    BasicUtils.setSessionAttributes(attributesManager,getStateString(), GuideStates.GET_DEST_ADDR);
                    String speechText = "Alles klar. Bitte sag mir nochmal die Strasse, Hausnummer und Stadt";
                    FallbackIntentHandler.setFallbackMessage(speechText);
                    respBuilder
                            .withSpeech(speechText)
                            .withReprompt("Bitte sag mir nochmal die Strasse, Hausnummer und Stadt")
                            .withShouldEndSession(false)
                            .build();
                }else{
                    Map<String, Coordinate> stations = (Map<String, Coordinate>) attributesManager.getSessionAttributes().get("Stations");
                    String stationsToSelect = StringUtils.prepStringForChoiceIntent(new ArrayList<>(stations.keySet()));
                    BasicUtils.setSessionAttributes(attributesManager,getStateString(), GuideStates.SELECT_NEARBY_STATION);
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
                    BasicUtils.setSessionAttributes(attributesManager,getStateString(), GuideStates.GET_DEST_ADDR);
                    respBuilder = BasicUtils.putTogether("Neue Adresse", SpeechStrings.FOLLOWING_ADDRESSES+" "+SpeechStrings.SAY_ADDRESS);
                }else{
                    String speech = " Die Einrichtung waere hiermit vorerst abgeschlossen. " +
                            "Du kannst nun die Hilfefunktion aufrufen, eine Route erfragen oder die Konfiguration starten";
                    BasicUtils.setSessionAttributes(attributesManager,getStateString(), GuideStates.TRANSIT);
                    respBuilder = BasicUtils.putTogether("Route", speech);
                }
                break;
            }
            case CONFIG:{
                if(isYes){
                    respBuilder = BasicUtils.putTogether("Neue Adresse", SpeechStrings.FOLLOWING_ADDRESSES+" "+SpeechStrings.SAY_ADDRESS);
                    BasicUtils.setSessionAttributes(attributesManager,getStateString(), GuideStates.GET_DEST_ADDR);
                }else{
                    respBuilder = BasicUtils.putTogether("Neue Konfiguration" ,SpeechStrings.NEW_CONFIG);
                    BasicUtils.setSessionAttributes(attributesManager,getStateString(), GuideStates.NEW_CONFIG);
                }
                break;
            }
            case NEW_CONFIG:{
                if(isYes){
                    attributesManager.getPersistentAttributes().clear();
                    attributesManager.savePersistentAttributes();
                    BasicUtils.setSessionAttributes(attributesManager,getStateString(), GuideStates.GET_DEST_ADDR);
                    return Setup.setupState(input);
                }else{
                    respBuilder = BasicUtils.putTogether("RoutenOption", String.format(SpeechStrings.WELCOME_TRANSIT, attributesManager.getPersistentAttributes().get("NAME")));
                    BasicUtils.setSessionAttributes(attributesManager,getStateString(), GuideStates.TRANSIT);
                }
                break;

            }

        }
        return respBuilder.build();
    }

    public static String getStateString(){
        return "State";
    }
}
