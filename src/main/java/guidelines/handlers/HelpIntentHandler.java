/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package main.java.guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import main.java.guidelines.SpeechStrings;
import main.java.guidelines.stateMachine.GuideStates;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class HelpIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("HelpIntent").and(sessionAttribute("State", GuideStates.TRANSIT.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        // ToDo:
        //  willst du zu destinationhandler
        //  willst du zu homehandler
        ResponseBuilder respBuilder = input.getResponseBuilder();

        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();

        Slot homeOrDestSlot = slots.get("homeordest");

        String speechText;
        String repromptText;
        boolean askResponse = false;

        if (homeOrDestSlot != null) {
            String choice = homeOrDestSlot.getValue();
            AttributesManager attributesManager = input.getAttributesManager();
            attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.HELP.toString()));
            speechText = SpeechStrings.HELP;
            repromptText = "Bitte funktioniere";
        } else {
            repromptText = "Bitte wiederhole nochmal was du gesagt hast? Möchtest du mit den Infos zur Heimadresse oder zur Zieladresse weiterfahren?";
            speechText = "Leider hat etwas nicht geklappt, bis sage mir nochmal ob du Infos zur Heimadresse oder Zieladresse haben möchtest";
            askResponse = true;
        }

        respBuilder.withSimpleCard("Hilfe Zieladresse", "Hilfe Zieladresse")
                .withSpeech(SpeechStrings.HELP)
                .withReprompt(SpeechStrings.REPROMPT)
                .withShouldEndSession(false)
                .build();

        if (askResponse) {
            respBuilder.withShouldEndSession(false)
                    .withReprompt(repromptText)
                    .withSpeech(speechText);
        }


        return respBuilder.build();
    }
}
