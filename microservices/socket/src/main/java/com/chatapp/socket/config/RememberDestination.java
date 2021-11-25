package com.chatapp.socket.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.user.UserDestinationResolver;
import org.springframework.messaging.simp.user.UserDestinationResult;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RememberDestination implements UserDestinationResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(RememberDestination.class);

    private static final Pattern DESTINATION_PREFIXING_PATTERN = Pattern.compile("/user/(?<name>.+?)/(?<routing>.+)/(?<dest>.+)");

    private static final Pattern USER_AUTHENTICATED_PATTERN = Pattern.compile("/user/(?<routing>.*)/(?<dest>.+)");
    private static final Pattern USER_CHANNEL_PATTERN = Pattern.compile("/topic/(?<dest>.*)");

    @Override
    public UserDestinationResult resolveDestination(Message<?> message) {

        SimpMessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, SimpMessageHeaderAccessor.class);
        final String destination = accessor.getDestination();

        if (destination == null) {
            return null;
        }

        final String authUser = accessor.getUser() != null ? accessor.getUser().getName() : null;

        LOGGER.trace("Resolving user destination {} for authUser={}, messageType={}",
                destination, authUser, accessor.getMessageType());

        if (SimpMessageType.SUBSCRIBE.equals(accessor.getMessageType()) || SimpMessageType.UNSUBSCRIBE.equals(accessor.getMessageType())) {
            if (authUser != null) {
                final Matcher authMatcher = USER_AUTHENTICATED_PATTERN.matcher(destination);
                final Matcher channelMatcher = USER_CHANNEL_PATTERN.matcher(destination);
                if (authMatcher.matches()) {
                    String result = String.format("/%s/user(%s)-%s",
                            authMatcher.group("routing"),
                            authUser,
                            authMatcher.group("dest"));
                    UserDestinationResult userDestinationResult =
                            new UserDestinationResult(destination,
                                    Collections.singleton(result),
                                    result,
                                    authUser);
                    LOGGER.debug("Resolved {} for {} into {}", destination, authUser, userDestinationResult);
                    return userDestinationResult;
                }
                if(channelMatcher.matches()){
                    LOGGER.info("CHANNEL MATCH: {}",destination);
                    String result = String.format("/%s/user(%s)-channel-test",
                            channelMatcher.group("dest"),
                            authUser,
                            channelMatcher.group("dest"));
                    UserDestinationResult userDestinationResult =
                            new UserDestinationResult(destination,Collections.singleton(result),result,authUser);
                    return userDestinationResult;
                }
            }
        } else if (SimpMessageType.MESSAGE.equals(accessor.getMessageType())) {
            final Matcher prefixMatcher = DESTINATION_PREFIXING_PATTERN.matcher(destination);
            if (prefixMatcher.matches()) {
                String user = prefixMatcher.group("name");
                String result = String.format("/%s/user(%s)-%s",
                        prefixMatcher.group("routing"),
                        user,
                        prefixMatcher.group("dest"));
                UserDestinationResult userDestinationResult =
                        new UserDestinationResult(destination,
                                Collections.singleton(result),
                                result,
                                user);
                LOGGER.debug("Resolved {} into {}", destination, userDestinationResult);
                return userDestinationResult;
            }
        }

        LOGGER.trace("Destination {} is not user-based", destination);
        return null;
    }
}