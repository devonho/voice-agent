package com.symplesys.voicebot.twilio.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StreamMediaEvent extends StreamEvent{
    @JsonProperty("sequenceNumber")
    public Integer sequenceNumber;
    @JsonProperty("streamSid")
    public String streamSid;
    @JsonProperty("media")
    public StreamMedia media;
}
