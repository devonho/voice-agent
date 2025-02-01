package com.symplesys.voicebot.audio;

import java.util.EventListener;

public interface AudioChunkEventHandler extends EventListener{

    public void onAudioChunk(byte[] audioChunk, String streamId);
    
    public void onStart(String streamId);

    public void onStop(String streamId);
} 
