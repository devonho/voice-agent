package com.symplesys.voicebot.twilio.types;



import com.fasterxml.jackson.annotation.JsonProperty;


public class StreamStartEvent extends StreamEvent{
    @JsonProperty("streamSid")
    public String streamSid;
    @JsonProperty("sequenceNumber")
    public String sequenceNumber;
    @JsonProperty("start")
    public StartMetadata startMetadata;
}
