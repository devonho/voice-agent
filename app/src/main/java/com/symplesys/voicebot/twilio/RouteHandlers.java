package com.symplesys.voicebot.twilio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteHandlers {
	private static final Logger logger = LogManager.getLogger(RouteHandlers.class);

	@GetMapping("/call")
	public static void call(String phoneNumber, String name, String greeting) {
		try {
			Telephony.call(phoneNumber, name, greeting);
		} catch (Exception e) {
			logger.error("Error calling: {}", phoneNumber, e);
		}
	}

	@PostMapping(path = "/status")
	public static void status(
		Integer CallDuration, 
		String RecordingUrl, 
		String RecordingSid, 
		String RecordingDuration,
		String CallbackSource,
		String CallStatus
	) {
		logger.debug("Status: {} Source: {}", CallStatus, CallbackSource);
	}
	/*
	@PostMapping("/handlerecording")
	public static void handleRecording(String RecordingUrl, Integer RecordingDuration, String Digits) {
		logger.debug("RecordingUrl: {}", RecordingUrl);
	}	

	@PostMapping("/transcription") 
	public static void transcription(
		String TranscriptionSid,
		String TranscriptionEvent,
		String TranscriptionData,
		String TranscriptionStatus) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			HashMap<String, String> hashMap = mapper.readValue(TranscriptionData, HashMap.class);

			logger.debug("TranscriptionEvent: {} Text: {}", TranscriptionEvent, hashMap.get("transcript"));
			//Telephony.handleCallback(status);
		} catch (Exception e) {
			logger.error("Error processing transcription: {}", e.getMessage());
		}
	}
	*/
}
