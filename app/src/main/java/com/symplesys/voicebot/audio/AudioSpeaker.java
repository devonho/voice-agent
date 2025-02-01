package com.symplesys.voicebot.audio;

import java.io.ByteArrayInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AudioSpeaker extends AudioSink {

    private static final Logger logger = LogManager.getLogger(AudioSpeaker.class);

    @Override
    public void play(String streamId, byte[] audioContents) {

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audioContents));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            Thread.sleep(clip.getMicrosecondLength() / 1000);
        }
        catch (Exception e) {
            logger.error("Error playing clip: {}", e.getMessage());
        }
    }
}
