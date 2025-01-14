package org.skillfactory.sf_bank_system.service;

import lombok.RequiredArgsConstructor;
import org.skillfactory.sf_bank_system.model.entity.Notification;
import org.skillfactory.sf_bank_system.repository.NotificationRepository;
import org.skillfactory.sf_bank_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<Notification> getAllNotifications(Long userId) {
        return notificationRepository.findAllByUserId(userId);
    }

}
