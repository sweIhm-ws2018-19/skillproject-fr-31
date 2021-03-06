/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import guidelines.SpeechStrings;
import guidelines.statemachine.GuideStates;
import guidelines.utilities.BasicUtils;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class HelpIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent").and(sessionAttribute(GuideStates.STATE.getKey(), GuideStates.TRANSIT.toString())));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder respBuilder = input.getResponseBuilder();

        AttributesManager attributesManager = input.getAttributesManager();
        BasicUtils.setSessionAttributes(attributesManager, GuideStates.STATE.getKey(), GuideStates.HELP.toString());

        String speechText = SpeechStrings.HELP;
        FallbackIntentHandler.setFallbackMessage(speechText);

        respBuilder.withSimpleCard(SpeechStrings.SKILL_NAME, "Hilfe")
                .withSpeech(speechText)
                .withReprompt(SpeechStrings.REPROMPT)
                .withShouldEndSession(false)
                .build();

        return respBuilder.build();
    }
}
