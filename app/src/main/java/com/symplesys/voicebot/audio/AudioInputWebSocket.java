package com.symplesys.voicebot.audio;

import java.util.ArrayList;
import java.util.List;
import com.symplesys.voicebot.twilio.TwilioWebSocketHandler;

public class AudioInputWebSocket extends AudioSource {
    
    //private static final Logger logger = LogManager.getLogger(AudioInputWebSocket.class);
    List<AudioChunkEventHandler> eventHandlers = new ArrayList<>();

    public AudioInputWebSocket(TwilioWebSocketHandler wsHandler) {
        
        AudioChunkEventHandler eventHandler = new AudioChunkEventHandler() {
            @Override
            public void onAudioChunk(byte[] audioChunk, String streamId) {
                for (AudioChunkEventHandler eventHandler : eventHandlers) {
                    eventHandler.onAudioChunk(audioChunk, streamId);
                }
            }

            @Override
            public void onStart(String streamId) {
                for (AudioChunkEventHandler eventHandler : eventHandlers) {
                    eventHandler.onStart(streamId);
                }
            }

            @Override
            public void onStop(String streamId) {
                for (AudioChunkEventHandler eventHandler : eventHandlers) {
                    eventHandler.onStop(streamId);
                }
            }
        };
        wsHandler.registerEventHandler(eventHandler);
    }    

    @Override
    public void registerEventHandler(AudioChunkEventHandler eventHandler) {
        eventHandlers.add(eventHandler);
    }
}
