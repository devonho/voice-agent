package com.symplesys.voicebot.agents;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.dialogflow.cx.v3beta1.DetectIntentRequest;
import com.google.cloud.dialogflow.cx.v3beta1.DetectIntentResponse;
import com.google.cloud.dialogflow.cx.v3beta1.QueryInput;
import com.google.cloud.dialogflow.cx.v3beta1.QueryResult;
import com.google.cloud.dialogflow.cx.v3beta1.SessionName;
import com.google.cloud.dialogflow.cx.v3beta1.SessionsClient;
import com.google.cloud.dialogflow.cx.v3beta1.SessionsSettings;
import com.google.cloud.dialogflow.cx.v3beta1.TextInput;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DialogflowCXAgent {

  private static final Logger logger = LogManager.getLogger(DialogflowCXAgent.class);
  private String projectId;
  private String locationId;
  private String agentId;
  private String sessionId;
  private String languageCode;

  public DialogflowCXAgent(
      String projectId,
      String locationId,
      String agentId,
      String sessionId,
      String languageCode) {
      this.projectId = projectId;
      this.locationId = locationId;
      this.agentId = agentId;
      this.sessionId = sessionId;
      this.languageCode = languageCode;
  }

  // DialogFlow API Detect Intent sample with text inputs.
  public String detectIntent(String text)
      throws IOException, ApiException {
    SessionsSettings.Builder sessionsSettingsBuilder = SessionsSettings.newBuilder();
    if (locationId.equals("global")) {
      sessionsSettingsBuilder.setEndpoint("dialogflow.googleapis.com:443");
    } else {
      sessionsSettingsBuilder.setEndpoint(locationId + "-dialogflow.googleapis.com:443");
    }
    SessionsSettings sessionsSettings = sessionsSettingsBuilder.build();

    String responseText;
    try (SessionsClient sessionsClient = SessionsClient.create(sessionsSettings)) {
      SessionName session = SessionName.ofProjectLocationAgentSessionName(projectId, locationId, agentId, sessionId);

      TextInput.Builder textInput = TextInput.newBuilder().setText(text);
      QueryInput queryInput = QueryInput.newBuilder().setText(textInput).setLanguageCode(languageCode).build();

      DetectIntentRequest request = DetectIntentRequest.newBuilder()
          .setSession(session.toString())
          .setQueryInput(queryInput)
          .build();

      DetectIntentResponse response = sessionsClient.detectIntent(request);
      QueryResult queryResult = response.getQueryResult();
      responseText = queryResult.getResponseMessages(0).getText().getText(0);
      logger.info("User: {}", text);            
      logger.info("Agent: {}", responseText);            
    }

    return responseText;
  }
}
