package com.symplesys.voicebot.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.symplesys.voicebot.twilio.TwilioWebSocketHandler;

@Configuration
@EnableWebSocket
@Component
public class WebSocketConfiguration implements WebSocketConfigurer {

	TwilioWebSocketHandler handler;
	public WebSocketConfiguration(TwilioWebSocketHandler handler) {
		this.handler = handler;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(this.handler, "/call-stream-websocket");
			//.setAllowedOrigins("https://deehobbt.ngrok.dev");;
	}
	/*
	@Bean
	public WebSocketHandler myHandler() {
		return new StreamHandler();
	}
	*/
}