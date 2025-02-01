package com.symplesys.voicebot.audio;

import java.io.IOException;
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TTS   {

    private static final Logger logger = LogManager.getLogger(TTS.class);

    @Autowired
    private AudioSink audioSink;

    TextToSpeechClient textToSpeechClient;
    VoiceSelectionParams voice;
    AudioConfig audioConfig;

    public TTS(AudioSink audioSink) throws IOException{
        this.audioSink = audioSink;
        this.textToSpeechClient = TextToSpeechClient.create();
        this.voice =
        VoiceSelectionParams.newBuilder()
            .setLanguageCode("en-US")
            .setSsmlGender(SsmlVoiceGender.FEMALE)
            .build();
        this.audioConfig = AudioConfig.newBuilder()
            .setAudioEncoding(AudioEncoding.LINEAR16)
            .setSampleRateHertz(16000)
            .build();
    }

    public void push(String streamId, String text) {

        AudioConfig audioConfig = this.audioConfig;
        VoiceSelectionParams voice = this.voice;

        Runnable r = () -> {
            try {
                logger.debug("TTS synthesis: StreamSid: {}, text: {}", streamId, text);
                SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
                SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
                Thread.sleep(100);  // Wait a bit before playback
                ByteString audioContents = response.getAudioContent();

                try {
                    logger.debug("TTS before playback: {} bytes", audioContents.toByteArray().length);
                    audioSink.play(streamId, audioContents.toByteArray());                    
                } catch (Exception ee) {
                    logger.error("During STT playback: " + ee.getMessage());
                }
    
            } catch (Exception e) {
                logger.error("During STT synthesis: " + e.getMessage());
            }


        };
        Thread thread = new Thread(r);
        thread.start();
    }
}

