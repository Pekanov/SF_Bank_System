package org.skillfactory.sf_bank_system.controller;

import lombok.RequiredArgsConstructor;
import org.skillfactory.sf_bank_system.model.entity.Notification;
import org.skillfactory.sf_bank_system.security.JwtTokenProvider;
import org.skillfactory.sf_bank_system.service.NotificationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping(value = "/userNotifications",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Notification>> getUserNotifications(@RequestHeader("Authorization")String authorizationHeader) {
        return ResponseEntity.ok().body(
                notificationService.getAllNotifications(
                        jwtTokenProvider.getIdFromAuthorizationHeader(authorizationHeader)
                )
        );
    }

}
