package com.example.contentpub.reqhandler.application.controller;

import com.example.contentpub.reqhandler.application.dto.request.NotifyRequest;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The NotifyController is the endpoint to accept notification related API requests. Data received through this endpoint
 * is published to WebSocket topics.
 */
@RestController
public class NotifyController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Value("${notification.web-socket.topic}")
    private String notificationTopic;

    /**
     * Webhook to publish notification data to a WebSocket. Not exposed to the public.
     * @param requestBody the request body.
     * @return An empty response with 200 HTTP status code.
     */
    @PostMapping("/notification")
    public ResponseEntity<Void> broadCastNotification(@RequestBody NotifyRequest requestBody) {

        JSONObject data = requestBody.getData();
        simpMessagingTemplate.convertAndSend(notificationTopic, data.toJSONString()); // Publish to WebSocket.

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
