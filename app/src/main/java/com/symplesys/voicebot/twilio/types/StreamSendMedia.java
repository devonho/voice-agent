package com.symplesys.voicebot.twilio.types;

import com.fasterxml.jackson.annotation.JsonProperty;

class StreamSendMediaMediaPayload {
    @JsonProperty("payload")
    public String b64payload;
}


public class StreamSendMedia {

    @JsonProperty("event")
    public final String eventType = "media";
    @JsonProperty("streamSid")
    public String streamSid;
    @JsonProperty("media")
    public StreamSendMediaMediaPayload media = new StreamSendMediaMediaPayload();

    public void setPayload(String b64payload) {
        this.media.b64payload = b64payload;
    }

    public String toString() {
        return "StreamSendMedia{" +
                "eventType='" + eventType + '\'' +
                ", streamSid='" + streamSid + '\'' +
                ", media=" + media.b64payload.substring(0, 10) +
                '}';
    }
}
