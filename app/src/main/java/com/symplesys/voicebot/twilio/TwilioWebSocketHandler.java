package com.symplesys.voicebot.twilio;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.symplesys.voicebot.audio.AudioChunkEventHandler;
import com.symplesys.voicebot.twilio.types.StreamEvent;
import com.symplesys.voicebot.twilio.types.StreamMediaEvent;
import com.symplesys.voicebot.twilio.types.StreamSendMedia;
import com.symplesys.voicebot.twilio.types.StreamStartEvent;
import com.symplesys.voicebot.twilio.types.StreamStopEvent;

@Component
public class TwilioWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LogManager.getLogger(TwilioWebSocketHandler.class);
    private List<AudioChunkEventHandler> eventHandlers = new ArrayList<>();
    private WebSocketSession session = null;

    public static AudioFormat sourceFormat = new AudioFormat(
        AudioFormat.Encoding.ULAW,
        8000,
        8,
        1,
        160,
        50,
        true
    );

    public static AudioFormat destFormat = new AudioFormat(16000, 16, 1, true, false);

    //FileOutputStream fos;

    public void registerEventHandler(AudioChunkEventHandler eventHandler) {
        eventHandlers.add(eventHandler);
    }

    public void send(String streamId, byte[] audioChunk) throws IOException {

        try {
            logger.debug("Audio chunk: {} bytes", audioChunk.length);
            String payload = Base64.getEncoder().encodeToString(audioChunk);
            logger.trace("Audio payload: {}", payload);
            StreamSendMedia msg = new StreamSendMedia(); 
            msg.streamSid = streamId;            
            msg.setPayload(payload);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String jsonString = mapper.writeValueAsString(msg);
            logger.trace("Sending to websocket: {}", jsonString);

            if(this.session != null) {
                TextMessage message = new TextMessage(jsonString);
                this.session.sendMessage(message);
            } else {
                logger.error("Session is null");
            }
    
        } catch (Exception e) {
            logger.error("While sending to websocket: " + e.getMessage());
        }

    }


	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.debug("afterConnectionEstablished");
        this.session = session;
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String jsonString = (String)(message.getPayload());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        StreamEvent event = mapper.readValue(jsonString, StreamEvent.class);
        
        switch (event.eventType) {
            case "connected":
                //StreamConnectedEvent connectedEvent = mapper.readValue(jsonString, StreamConnectedEvent.class);
                logger.debug("stream: connected");
                break;
            case "start":
                StreamStartEvent startEvent = mapper.readValue(jsonString, StreamStartEvent.class);
                logger.debug("stream: start StreamSid: {}", startEvent.streamSid);

                //fos = new FileOutputStream(startEvent.streamSid + ".bin");

                for (AudioChunkEventHandler eventHandler : eventHandlers) {
                    eventHandler.onStart(startEvent.streamSid);
                }                

                break;
            case "media":
                StreamMediaEvent mediaEvent = mapper.readValue(jsonString, StreamMediaEvent.class);
                //logger.debug("stream: media {}", mediaEvent.media.chunk);
                byte[] decoded = Base64.getDecoder().decode(mediaEvent.media.payload);

                // Twilio: audio/x-mulaw, 8kHz, 1 channel
                // STT: audio/linear, 16kHz, 1 channel

                InputStream is = new java.io.BufferedInputStream(new java.io.ByteArrayInputStream(decoded));
                AudioInputStream ais = new AudioInputStream(is, sourceFormat, decoded.length / 160);
                ais = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, ais);
                ais = AudioSystem.getAudioInputStream(destFormat, ais);
                //this.audioInputWebSocket.pushChunk(ais.readAllBytes());
                //logger.debug("chunk: {}", decoded.length);
                //fos.write(decoded);
                for (AudioChunkEventHandler eventHandler : eventHandlers) {
                    eventHandler.onAudioChunk(ais.readAllBytes(), mediaEvent.streamSid);
                }
                
                break;
            case "stop":
                StreamStopEvent stopEvent = mapper.readValue(jsonString, StreamStopEvent.class);
                logger.debug("stream: stop");

                //fos.close();
                for (AudioChunkEventHandler eventHandler : eventHandlers) {
                    eventHandler.onStop(stopEvent.streamSid);
                }                

                break;
            default:    
                break;
        }
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		logger.debug("handleTransportError");
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		logger.debug("afterConnectionClosed");
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

}