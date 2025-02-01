package com.symplesys.voicebot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.symplesys.voicebot.audio.AudioChunkEventHandler;
import com.symplesys.voicebot.audio.AudioMic;
import com.symplesys.voicebot.audio.AudioSpeaker;
import com.symplesys.voicebot.audio.TTS;

@Disabled
@SpringBootTest
public class AudioTests {

    @Test
    void testPlayAudioToSpeaker() {
        try {

            TTS tts = new TTS(new AudioSpeaker());
            tts.push("foo","Hello world!"); 
            Thread.sleep(10000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testAudioMic() throws InterruptedException {
        AudioMic audioMic = new AudioMic();

        AudioChunkEventHandler eventHandler = new AudioChunkEventHandler() {
            @Override
            public void onStart(String streamId) {

            }        

            @Override
            public void onStop(String streamId) {

            }

            @Override
            public void onAudioChunk(byte[] audioChunk, String streamId) {
                assertEquals(audioChunk.length, 8000);
            }
        };
        audioMic.registerEventHandler(eventHandler);
        Thread.sleep(1000);
    }

}


