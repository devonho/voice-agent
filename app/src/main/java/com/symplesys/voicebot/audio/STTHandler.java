package com.symplesys.voicebot.audio;

public interface STTHandler {
    public void onStart();
    public void onResponse(String streamId, String transcript);
    public void onComplete();
    public void onError(String erroMessage);
}
