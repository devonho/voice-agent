package com.symplesys.voicebot.twilio.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StartMetadata {
    @JsonProperty("streamSid")
    public String streamSid;
    @JsonProperty("accountSid")
    public String accountSid;
    @JsonProperty("callSid")
    public String callSid;
    @JsonProperty("tracks")
    public List<String> tracks;
    @JsonProperty("mediaFormat")
    public StreamMediaFormat mediaFormat;
}