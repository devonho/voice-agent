package com.symplesys.voicebot.audio;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.protobuf.ByteString;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class STT
 {
    private static final Logger logger = LogManager.getLogger(STT.class);
    //private ClientStream<StreamingRecognizeRequest> clientStream;
    public String streamId;

    @Autowired
    private AudioSource audioSource;

    public STT(AudioSource audioSource) {
      this.audioSource = audioSource;
    }

    private void sendStreamingConfigRequest(ClientStream<StreamingRecognizeRequest> clientStream) {
        RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
            .setLanguageCode("en-US")
            .setSampleRateHertz(16000)        
            .build(); 
            
        StreamingRecognitionConfig streamingRecognitionConfig = StreamingRecognitionConfig.newBuilder()
            .setConfig(recognitionConfig).build();
          
        StreamingRecognizeRequest configRequest = StreamingRecognizeRequest.newBuilder()
            .setStreamingConfig(streamingRecognitionConfig)
            .build(); // The first request in a streaming call has to be a config

        clientStream.send(configRequest); // SampleRate:16000Hz, SampleSizeInBits: 16, Number of channels: 1, Signed: true, bigEndian: false
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {}
    }

    private void sendStreamingRecognizeRequest(byte[] data, ClientStream<StreamingRecognizeRequest> clientStream) {
      StreamingRecognizeRequest request = StreamingRecognizeRequest.newBuilder()
      .setAudioContent(ByteString.copyFrom(data))
      .build();
      clientStream.send(request);              
    }

    // TODO: implement onComplete observer callback
    //responseObserver.onComplete();

    
    public void setup(STTHandler sttHandler) {
      try {
        STT sttInstance = this;

        ResponseObserver<StreamingRecognizeResponse> responseObserver = new ResponseObserver<StreamingRecognizeResponse>() {
            @Override
            public void onStart(StreamController controller) {
              sttHandler.onStart();
            }

            @Override
            public void onResponse(StreamingRecognizeResponse response) {

                StreamingRecognitionResult result = response.getResultsList().get(0);
                if(result.getIsFinal()) {
                    SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                    String transcript = alternative.getTranscript();
                    logger.debug("STT : {}", transcript );
                    sttHandler.onResponse(sttInstance.streamId, transcript);
                }
            }

            @Override
            public void onComplete() {}

            @Override
            public void onError(Throwable t) {
                logger.error("STT Recognize Error {}", t.getMessage());
            }
        };

        SpeechClient client = SpeechClient.create();
        //ClientStream<StreamingRecognizeRequest> clientStream = client.streamingRecognizeCallable().splitCall(responseObserver);

        AudioChunkEventHandler eventHandler = new AudioChunkEventHandler() {

          ClientStream<StreamingRecognizeRequest> clientStream; 

          @Override
          public void onStart(String streamId) {
            logger.debug("Starting STT - sending config request");
            sttInstance.streamId = streamId;
            clientStream = client.streamingRecognizeCallable().splitCall(responseObserver);
            sendStreamingConfigRequest(clientStream);
          }

          @Override
          public void onStop(String streamId) {
            logger.debug("Stopping STT");
            clientStream.closeSend();
            //client.close();
          }

          @Override
          public void onAudioChunk(byte[] audioChunk, String streamId) {
            logger.trace("Sending chunk {}", audioChunk.length);
            sendStreamingRecognizeRequest(audioChunk, clientStream);
          }
        };
        audioSource.registerEventHandler(eventHandler);

      } catch (Exception e) {
        logger.error("Exception: {}", e.getMessage());
      }
    }
}
