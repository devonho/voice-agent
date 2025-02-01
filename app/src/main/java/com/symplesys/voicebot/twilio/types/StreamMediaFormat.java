package com.symplesys.voicebot.twilio.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StreamMediaFormat {
    @JsonProperty("encoding")
    public String encoding;
    @JsonProperty("sampleRate")
    public String sampleRate;
    @JsonProperty("channels")
    public Integer channels;
}