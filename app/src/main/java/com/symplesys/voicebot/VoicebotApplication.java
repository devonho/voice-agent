package com.symplesys.voicebot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.symplesys.voicebot.audio.Caller;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@SpringBootApplication
public class VoicebotApplication {

	static Logger logger = LogManager.getLogger(VoicebotApplication.class);
	
	@Autowired
	Caller caller;

	public VoicebotApplication(Caller caller) {
		this.caller = caller;
	}

	public static void main(String[] args) {
		SpringApplication.run(VoicebotApplication.class, args);
	}

}
