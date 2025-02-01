package com.symplesys.voicebot;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.symplesys.voicebot.twilio.types.StreamConnectedEvent;
import com.symplesys.voicebot.twilio.types.StreamMediaEvent;
import com.symplesys.voicebot.twilio.types.StreamStartEvent;
import com.symplesys.voicebot.twilio.types.StreamStopEvent;

public class StreamEventTests {

	static byte[] loadJSON(String filename) throws IOException {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream(filename);
		byte[] jsonBytes = is.readAllBytes();
		return jsonBytes;
	}    

    @Test
    void testStreamConnectedEvent() throws IOException {
        byte[] jsonBytes = StreamEventTests.loadJSON("stream-connected.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        StreamConnectedEvent event = mapper.readValue(jsonBytes, StreamConnectedEvent.class);      
        assert(event.eventType.equals("connected"));     
    }
    @Test
    void testStreamStartEvent() throws IOException {
        byte[] jsonBytes = StreamEventTests.loadJSON("stream-start.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        StreamStartEvent event = mapper.readValue(jsonBytes, StreamStartEvent.class);       
        assert(event.eventType.equals("start"));
        assert(event.startMetadata.mediaFormat.encoding.equals("audio/x-mulaw"));
    }

    @Test
    void testStreamMediaEvent() throws IOException {
        byte[] jsonBytes = StreamEventTests.loadJSON("stream-media.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        StreamMediaEvent event = mapper.readValue(jsonBytes, StreamMediaEvent.class);       
        assert(event.eventType.equals("media"));
        assert(event.media.chunk.equals(1));
        assert(event.media.payload.equals("no+JhoaJjpz..."));
    }    

    @Test
    void testStreamStopEvent() throws IOException {
        byte[] jsonBytes = StreamEventTests.loadJSON("stream-stop.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        StreamStopEvent event = mapper.readValue(jsonBytes, StreamStopEvent.class);       
        assert(event.eventType.equals("stop"));
        assert(event.stop.accountSid.equals("ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"));
    }      
}
