package com.symplesys.voicebot.twilio;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Connect;
import com.twilio.twiml.voice.Pause;
import com.twilio.twiml.voice.Say;
import com.twilio.twiml.voice.Stream;
import com.twilio.type.Twiml;

public class Telephony {
    
    // Set your Twilio account SID and auth token
    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    public static final String TWILIO_PHONE_NUMBER = System.getenv("TWILIO_PHONE_NUMBER");


    static Logger logger = LogManager.getLogger(Telephony.class);

    public static void call(String phoneNumber, String name, String greeting) throws Exception {

        // Create a new CallCreator instance
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Twiml twiml = makeBidirectionalStream("wss://deehobbt.ngrok.dev/call-stream-websocket", name, greeting);
        Call call = Call.creator(new com.twilio.type.PhoneNumber(phoneNumber),
                            new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER),
                            twiml)
                        .create();

        logger.debug("call sid: {}", call.getSid());
    };

    public static Twiml makeBidirectionalStream(String wssUrl, String name, String greeting) throws TwiMLException {

        Stream wsStream = new Stream.Builder().url(wssUrl).build();
        Connect connect = new Connect.Builder().stream(wsStream).build();
        Say say = new Say.Builder().addText("Hello " + name + "!" + greeting).build();

        VoiceResponse response = new VoiceResponse.Builder()
            .pause(new Pause.Builder().length(1).build())
            .say(say)
            .connect(connect)
            .build();
        return new Twiml(response.toXml());
    }

}