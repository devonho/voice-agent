package com.symplesys.voicebot.twilio.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StreamStop {
    @JsonProperty("accountSid")
    public String accountSid;
    @JsonProperty("callSid")
    public String callSid;
}
