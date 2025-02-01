package com.symplesys.voicebot.audio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;


class MicInputReader implements Runnable {

    private AudioInputStream audio;
    private static final Logger logger = LogManager.getLogger(MicInputReader.class);
    List<AudioChunkEventHandler> eventHandlers;
    
    public MicInputReader(List<AudioChunkEventHandler> eventHandlers) throws LineUnavailableException {
        this.eventHandlers = eventHandlers;

        AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info targetInfo = new DataLine.Info(
            TargetDataLine.class,
            audioFormat); // Set the system information to read from the microphone audio stream

        if (!AudioSystem.isLineSupported(targetInfo)) {
            audio = null;
            logger.error("Microphone not supported");
        } else {
            // Target data line captures the audio stream the microphone produces.
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            audio = new AudioInputStream(targetDataLine);
        }
    }

    private byte[] getAudioChunk() throws IOException {
        if (audio == null) {
            return new byte[0];
        } else {
            byte[] data = new byte[8000];
            audio.read(data);
            return data;    
        }
    }

    public void run() {

        boolean doneOnce = false;

        while (true) {

            if(doneOnce== false && eventHandlers.size() > 0) {
                logger.debug("Starting mic");
                for (AudioChunkEventHandler eventHandler : eventHandlers) {
                    eventHandler.onStart("<MIC>");
                }                
                doneOnce = true;
            }

            if(doneOnce == true) {
                try {
                    byte[] audioChunk = getAudioChunk();
                    for (AudioChunkEventHandler eventHandler : eventHandlers) {
                        eventHandler.onAudioChunk(audioChunk, "<MIC>");
                    }
                } catch (IOException e) {
                    logger.error("Error reading audio chunk", e);
                }
            } else {
                try {
                    Thread.sleep(10);
                } catch (Exception e) {}
            }
        }
        /* 
        for (AudioChunkEventHandler eventHandler : eventHandlers) {
            eventHandler.onStop();
        }
        */
    }
}

@Component
public class AudioMic extends AudioSource{

    private static final Logger logger = LogManager.getLogger(AudioMic.class);
    private List<AudioChunkEventHandler> eventHandlers = new ArrayList<>();
    private Thread thread;

    public AudioMic()  {
        try {
            thread = new Thread(new MicInputReader(eventHandlers), "AudioMic");
            thread.start();
        } catch (LineUnavailableException e) {
            logger.error("Error initializing audio source", e);
        }
    }

    public void registerEventHandler(AudioChunkEventHandler eventHandler) {
        eventHandlers.add(eventHandler);
    }

    public void stop() {
        thread.interrupt();
    }

}
