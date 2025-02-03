package com.symplesys.voicebot.audio;

import com.symplesys.voicebot.agents.DialogflowCXAgent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Caller {

    private static final Logger logger = LogManager.getLogger(Caller.class);

    public Caller(DialogflowCXAgent agent, TTS tts, STT stt) {

        STTHandler sttHandler = new STTHandler() {

            @Override
            public void onStart() {}

            @Override
            public void onResponse(String streamId, String transcript) {
                try {
                    String agentResponse = agent.send(transcript);
                    tts.push(streamId, agentResponse);
                } catch (Exception e) {                        
                    logger.error(e.getMessage());
                }
            }

            @Override
            public void onComplete() {}

            @Override
            public void onError(String s) {
                logger.error("STT Recognize Error {}", s);
            }
        };
        
        stt.setup(sttHandler);
    }
}
