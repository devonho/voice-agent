package com.symplesys.voicebot;

import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import com.google.api.gax.rpc.ApiException;
import com.symplesys.voicebot.agents.DialogflowCXAgent;

@Disabled("Disabled until credentials passing is fixed")
public class CXTests {

    public static final String GOOGLE_PROJECT_ID = System.getenv("GOOGLE_PROJECT_ID");
    public static final String GOOGLE_AGENT_ID = System.getenv("GOOGLE_AGENT_ID");

    @Test
    void TestDetectIntent() {

        String locationId = "global";
        String sessionId = "my-uuid";
        String languageCode = "en-US";
        String text = "What is the power source for modern BEVs?";

        try {
            DialogflowCXAgent agent = new DialogflowCXAgent(GOOGLE_PROJECT_ID, 
                locationId, 
                GOOGLE_AGENT_ID,        
                sessionId, languageCode);
            agent.send(text);
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
