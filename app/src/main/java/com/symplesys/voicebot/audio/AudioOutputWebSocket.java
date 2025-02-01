package com.symplesys.voicebot.audio;

import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.symplesys.voicebot.twilio.TwilioWebSocketHandler;

public class AudioOutputWebSocket extends AudioSink {

    static final Logger logger = LogManager.getLogger(AudioOutputWebSocket.class);

    TwilioWebSocketHandler handler;

    public static AudioFormat sourceFormat = new AudioFormat(16000, 16, 1, true, false); // PCM_SIGNED 16000.0 Hz, 16 bit, mono, 2 bytes/frame, little-endian

    public static AudioFormat targetFormat = new AudioFormat(
        AudioFormat.Encoding.PCM_SIGNED,
        8000,
        8,
        1,
        160,
        50,
        true
    );    

    public AudioOutputWebSocket(TwilioWebSocketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void play(String streamId,byte[] audioContents) {
        try {
            logger.debug("Playing audio: StreamSid: {}", streamId);

            InputStream is = new java.io.BufferedInputStream(new java.io.ByteArrayInputStream(audioContents));
            AudioInputStream ais = new AudioInputStream(is, sourceFormat, audioContents.length / 2); // "PCM_SIGNED 16000.0 Hz, 16 bit, mono, 2 bytes/frame, little-endian"

            //Encoding[] encodings = AudioSystem.getTargetEncodings(sourceFormat);

            ais = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 1, 2, 8000, true), ais);
            ais = AudioSystem.getAudioInputStream(AudioFormat.Encoding.ULAW, ais);

            try {
                byte[] data =  ais.readAllBytes();
                logger.debug("Before sending to websocket. {} bytes", data.length);
                this.handler.send(streamId, data);
            } catch (Exception e) {
                logger.error("While sending to websocket: ", e);
            }

            
        } catch (Exception e) {
            logger.error("While transcoding audio: ", e);
        }
    }
}
