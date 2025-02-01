package com.symplesys.voicebot.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.symplesys.voicebot.agents.DialogflowCXAgent;
import com.symplesys.voicebot.audio.AudioInputWebSocket;
import com.symplesys.voicebot.audio.AudioMic;
import com.symplesys.voicebot.audio.AudioOutputWebSocket;
import com.symplesys.voicebot.audio.AudioSink;
import com.symplesys.voicebot.audio.AudioSource;
import com.symplesys.voicebot.audio.AudioSpeaker;
import com.symplesys.voicebot.audio.Caller;
import com.symplesys.voicebot.audio.STT;
import com.symplesys.voicebot.audio.TTS;
import com.symplesys.voicebot.twilio.TwilioWebSocketHandler;

@Configuration
public class VoicebotApplicationConfiguration {

    private static final Logger logger = LogManager.getLogger(VoicebotApplicationConfiguration.class);

    public static final String GOOGLE_PROJECT_ID = System.getenv("GOOGLE_PROJECT_ID");
    public static final String GOOGLE_AGENT_ID = System.getenv("GOOGLE_AGENT_ID");
    public static final String USE_TELEPHONY = System.getenv("USE_TELEPHONY");

    private static TwilioWebSocketHandler twilioWebSocketHandler;

    @Bean
    public AudioSink audioSink(TwilioWebSocketHandler twilioWebSocketHandler) {
        logger.debug("Initializing AudioSink bean");
        if(USE_TELEPHONY.equals("true") ) {
            return new AudioOutputWebSocket(twilioWebSocketHandler);
        } else {
            return new AudioSpeaker();
        }
    }

    @Bean
    public AudioSource audioSource(TwilioWebSocketHandler twilioWebSocketHandler) {
        logger.debug("Initializing AudioSource bean");
        if(USE_TELEPHONY.equals("true") ) {
            logger.debug("Using telephony");
            return new AudioInputWebSocket(twilioWebSocketHandler);
        } else {
            return new AudioMic();
        }
    }
    /* 
    @Bean
    public AudioInputWebSocket audioInputWebSocket(TwilioWebSocketHandler twilioWebSocketHandler) {
        logger.debug("Initializing AudioInputWebSocket bean");
        if(audioInputWebSocket != null)
        {
            return audioInputWebSocket;
        }
        else {
            audioInputWebSocket = new AudioInputWebSocket(twilioWebSocketHandler);
            return audioInputWebSocket;
        }
    }
    */
    @Bean
    public TwilioWebSocketHandler twilioWebSocketHandler() {
        logger.debug("Initializing TwilioWebSocketHandler bean");
        if(twilioWebSocketHandler != null)
        {
            return twilioWebSocketHandler;
        }
        else {
            twilioWebSocketHandler = new TwilioWebSocketHandler();
            return twilioWebSocketHandler;
        }        
    }

    @Bean
    public DialogflowCXAgent agent() {
        logger.debug("Initializing Agent bean");
        return new DialogflowCXAgent(GOOGLE_PROJECT_ID, "global", GOOGLE_AGENT_ID, "session", "en-US");
    }

    @Bean
    public Caller caller(DialogflowCXAgent agent, TTS tts, STT stt) {
        logger.debug("Initializing Caller bean");
        return new Caller(agent, tts, stt);
    }
}
