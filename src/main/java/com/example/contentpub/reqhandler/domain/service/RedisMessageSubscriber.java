package com.example.contentpub.reqhandler.domain.service;

import com.example.contentpub.reqhandler.application.config.websocket.ServerWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisMessageSubscriber implements MessageListener {

    @Autowired
    private ServerWebSocketHandler serverWebSocketHandler;

    public void onMessage(final Message message, final byte[] pattern) {
        try {
            log.info("Message received from Pub Sub msg: {}", message);
            serverWebSocketHandler.sendMessage(message.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}