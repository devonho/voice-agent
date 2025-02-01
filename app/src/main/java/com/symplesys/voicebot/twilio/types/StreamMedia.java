package com.symplesys.voicebot.twilio.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StreamMedia {
    @JsonProperty("track")
    public String track;
    @JsonProperty("chunk")
    public Integer chunk;
    @JsonProperty("timestamp")
    public Long timestamp;
    @JsonProperty("payload")
    public String payload;
}
