package com.symplesys.voicebot.twilio.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StreamStopEvent extends StreamEvent {
    @JsonProperty("sequenceNumber")
    public Integer sequenceNumber;
    @JsonProperty("streamSid")
    public String streamSid;
    @JsonProperty("stop")
    public StreamStop stop;
}
