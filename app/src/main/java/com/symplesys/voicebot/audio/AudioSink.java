package com.symplesys.voicebot.audio;


public abstract class AudioSink {

    public abstract void play(String streamId, byte [] audioContents);

}
