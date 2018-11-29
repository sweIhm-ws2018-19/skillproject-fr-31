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
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import main.java.guidelines.SpeechStrings;
import main.java.guidelines.stateMachine.GuideStates;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class LaunchRequestHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class).or(sessionAttribute("State", GuideStates.LAUNCH_STATE)));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        // Config Avail
        // TODO: check all connfig
        //  Name,
        //  home address,
        //  at least one dest address

        // Config not Avail
        // TODO:
        //   enter config state


        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();


        String outputMessage = SpeechStrings.DEFAULT;
//        if( persistentAttributes.get("NAME") == null || persistentAttributes.get("HOMEADDRESS") == null
//                || persistentAttributes.get("DESTADDRESS") == null) {
        if (persistentAttributes.get("NAME") == null) {
            attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.INSERT_NAME));
            outputMessage = SpeechStrings.WELCOME_NO_CONFIG;
        } else {
            attributesManager.setSessionAttributes(Collections.singletonMap("State", GuideStates.TRANSIT));
            outputMessage = String.format(SpeechStrings.WELCOME_TRANSIT, persistentAttributes.get("NAME"));
        }


        ResponseBuilder builder = input.getResponseBuilder();
        builder.withSimpleCard("Session", SpeechStrings.SKILL_NAME)
                .withSpeech(outputMessage)
                .withReprompt(outputMessage);


//        ResponseBuilder builder = input.getResponseBuilder();
//        builder.withSimpleCard("Session", SpeechStrings.SKILL_NAME);
//        if (name != null){
//             builder.withSpeech(SpeechStrings.WELCOME_USER + name)
//                     .withReprompt(SpeechStrings.REPROMPT);
//        }
//        else
//        {
//            builder.withSpeech(SpeechStrings.WELCOME)
//                    .withReprompt(SpeechStrings.REPROMPT);
//        }

        return builder.build();
    }
}
