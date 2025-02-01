package com.symplesys.voicebot.twilio.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StreamConnectedEvent extends StreamEvent {
    @JsonProperty("protocol")
    public String protocol;

    @JsonProperty("version")
    public String version;
}