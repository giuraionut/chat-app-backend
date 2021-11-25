package com.chatapp.socket.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.user.UserDestinationResolver;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@Import({KeycloakSecurityConfig.class})
@EnableWebSocketMessageBroker
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Value("${rabbitmq.relay-host}")
    private String relayHost;
    @Value("${rabbitmq.relay-port}")
    private int relayPort;
    @Value("${rabbitmq.virtual-host}")
    private String virtualHost;
    @Value("${rabbitmq.client-login}")
    private String clientLogin;
    @Value("${rabbitmq.client-passcode}")
    private String clientPasscode;
    @Value("${rabbitmq.system-login}")
    private String systemLogin;
    @Value("${rabbitmq.system-passcode}")
    private String systemPasscode;

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .nullDestMatcher().authenticated()
                .simpSubscribeDestMatchers("/user/queue/errors").authenticated()
                .simpDestMatchers("/app/**").hasRole("USER")
                .anyMessage().authenticated();
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableStompBrokerRelay("/topic", "/fanout", "/queue")
                .setRelayHost(relayHost)
                .setRelayPort(relayPort)
                .setVirtualHost(virtualHost)
                .setClientLogin(clientLogin)
                .setClientPasscode(clientPasscode)
                .setSystemLogin(systemLogin)
                .setSystemPasscode(systemPasscode);
    }
    @Bean
    public UserDestinationResolver userDestinationResolver() {
        return new RememberDestination();
    }


}