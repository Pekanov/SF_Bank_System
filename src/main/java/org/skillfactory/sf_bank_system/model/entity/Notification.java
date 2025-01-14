package org.skillfactory.sf_bank_system.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "notification")
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь с пользователем
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Дата уведомления
    @Column(nullable = false)
    private LocalDate date;

    // Текст сообщения
    @Column(nullable = false, length = 1000)
    private String message;

    @PostPersist
    private void setDefaultUsername() {
        this.date = LocalDate.now();
    }
}
