package com.symplesys.voicebot;

import java.io.FileOutputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.junit.jupiter.api.Test;

import com.symplesys.voicebot.twilio.TwilioWebSocketHandler;

public class CodecTests {

    @Test
    void testDecode() {
        try {
            AudioFormat micFormat = new AudioFormat( 
                16000, 
                16, 
                1, 
                true, false);

            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("twilio.bin");
            int numFrames = is.readAllBytes().length / 160;

            //Encoding[] encodings = AudioSystem.getTargetEncodings(StreamHandler.sourceFormat);
            //AudioFormat[] formats = AudioSystem.getTargetFormats(AudioFormat.Encoding.PCM_SIGNED, StreamHandler.sourceFormat);
            AudioInputStream ais = new AudioInputStream(is, TwilioWebSocketHandler.sourceFormat, numFrames);
            ais = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, ais);
            ais = AudioSystem.getAudioInputStream(micFormat, ais);

            FileOutputStream fos = new FileOutputStream("twilio.wav");
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, fos);
            fos.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
