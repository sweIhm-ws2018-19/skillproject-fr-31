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
    CONFIG("Config"), NEW_CONFIG("New_Config"), INSERT_NAME("insert_name"), LAUNCH_STATE("launch_state"),
    GET_DEST_NAME("get_dest_name"), GET_HOME_ADDR("get_home_addr"), Q_NEXT_ADDR("q_next_addr"),
    GET_DEST_ADDR("get_dest_addr"), HELP("help"), TRANSIT("transit"), SELECT_NEARBY_STATION("select_nearby_station"),
    SAY_DEST_ADDR_AGAIN("say_dest_addr_again"), ROUTE_TIME("route_time"), STATE("State");

    private final String key;

    GuideStates(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static Optional<Response> decisionBuilder(GuideStates state, boolean isYes, HandlerInput input) {
        AttributesManager attributesManager = input.getAttributesManager();
        ResponseBuilder respBuilder = new ResponseBuilder();
        switch (state) {
            case SAY_DEST_ADDR_AGAIN:
                if (isYes) {
                    BasicUtils.setSessionAttributes(attributesManager, STATE.key, GuideStates.GET_DEST_ADDR);
                    String speechText = "Alles klar. Bitte sag mir nochmal die Strasse, Hausnummer und Stadt";
                    FallbackIntentHandler.setFallbackMessage(speechText);
                    respBuilder
                            .withSpeech(speechText)
                            .withReprompt("Bitte sag mir nochmal die Strasse, Hausnummer und Stadt")
                            .withShouldEndSession(false)
                            .build();
                } else {
                    Map<String, Coordinate> stations = (Map<String, Coordinate>) attributesManager.getSessionAttributes().get("Stations");
                    String stationsToSelect = StringUtils.prepStringForChoiceIntent(new ArrayList<>(stations.keySet()));
                    BasicUtils.setSessionAttributes(attributesManager, STATE.key, GuideStates.SELECT_NEARBY_STATION);
                    String speechText = "Alles klar. Ich sage dir jetzt die Stationen die du zur Auswahl hast. Merke dir" +
                            " bitte die zugehörige Nummer der Station die du benutzen möchtest. " +
                            "Station: " + stationsToSelect +
                            " Zur Auswahl sage jetzt bitte: Ich nehme die, plus die zahl";
                    FallbackIntentHandler.setFallbackMessage(speechText);
                    respBuilder
                            .withSpeech(speechText)
                            .withReprompt(stationsToSelect)
                            .withShouldEndSession(false)
                            .build();
                }
                break;


            case Q_NEXT_ADDR:
                if (isYes) {
                    BasicUtils.setSessionAttributes(attributesManager, STATE.key, GuideStates.GET_DEST_ADDR);
                    respBuilder = BasicUtils.putTogether("Neue Adresse", SpeechStrings.FOLLOWING_ADDRESSES + " " + SpeechStrings.SAY_ADDRESS);
                } else {
                    String speech = " Die Einrichtung waere hiermit vorerst abgeschlossen. " +
                            "Du kannst nun die Hilfefunktion aufrufen, eine Route erfragen oder die Konfiguration starten";
                    BasicUtils.setSessionAttributes(attributesManager, STATE.key, GuideStates.TRANSIT);
                    respBuilder = BasicUtils.putTogether("Route", speech);
                }
                break;

            case CONFIG:
                if (isYes) {
                    respBuilder = BasicUtils.putTogether("Neue Adresse", SpeechStrings.FOLLOWING_ADDRESSES + " " + SpeechStrings.SAY_ADDRESS);
                    BasicUtils.setSessionAttributes(attributesManager, STATE.key, GuideStates.GET_DEST_ADDR);
                } else {
                    respBuilder = BasicUtils.putTogether("Neue Konfiguration", SpeechStrings.NEW_CONFIG);
                    BasicUtils.setSessionAttributes(attributesManager, STATE.key, GuideStates.NEW_CONFIG);
                }
                break;

            case NEW_CONFIG:
                if (isYes) {
                    attributesManager.getPersistentAttributes().clear();
                    attributesManager.savePersistentAttributes();
                    BasicUtils.setSessionAttributes(attributesManager, STATE.key, GuideStates.GET_DEST_ADDR);
                    return Setup.setupState(input);
                } else {
                    respBuilder = BasicUtils.putTogether("RoutenOption", String.format(SpeechStrings.WELCOME_TRANSIT, attributesManager.getPersistentAttributes().get("NAME")));
                    BasicUtils.setSessionAttributes(attributesManager, STATE.key, GuideStates.TRANSIT);
                }
                break;
            default:
                respBuilder = BasicUtils.putTogether("GuideStates", "You are currently in state " + state.getKey());

        }
        return respBuilder.build();
    }
}
