package com.symplesys.voicebot;

import java.util.Base64;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.symplesys.voicebot.audio.AudioMic;
import com.symplesys.voicebot.audio.AudioOutputWebSocket;
import com.symplesys.voicebot.audio.AudioSpeaker;
import com.symplesys.voicebot.audio.STT;
import com.symplesys.voicebot.audio.STTHandler;
import com.symplesys.voicebot.audio.TTS;
import com.symplesys.voicebot.twilio.TwilioWebSocketHandler;

@Disabled("Until mocking of audio devices is implemented")
public class STTSTests {
    @Test
    void testTTS() {
        try {
            com.symplesys.voicebot.audio.TTS tts = new TTS(new AudioSpeaker());
            tts.push("foo","Hello world! How are you?");
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void testBase64() {
        byte[] audioChunk = new byte[] { 1, 2, 3, 4, 5 }; // audioChunk 

        String payload = Base64.getEncoder().encodeToString(audioChunk);
        System.out.println("Audio payload: " + payload);
    }

    @Test
    void testTTSTwilio() {
        try {
            com.symplesys.voicebot.audio.TTS tts = new TTS(new AudioOutputWebSocket(new TwilioWebSocketHandler()));
            tts.push("foo","Hello world! How are you?");
            Thread.sleep(20000000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void testSTT() throws InterruptedException {
        STT stt = new STT(new AudioMic());
        stt.setup(new STTHandler() {
            
            @Override
            public void onStart() {}

            @Override
            public void onResponse(String streamId, String transcript) {
                System.out.print(transcript);
            }

            @Override
            public void onComplete() {}

            @Override
            public void onError(String s) {
                System.out.print("STT Recognize Error " + s);
            }
        });
        
        Thread.sleep(50000);

    }
    

}
