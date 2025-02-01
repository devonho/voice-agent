package com.symplesys.voicebot.twilio.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StreamEvent {
    @JsonProperty("event")
    public String eventType;
}
