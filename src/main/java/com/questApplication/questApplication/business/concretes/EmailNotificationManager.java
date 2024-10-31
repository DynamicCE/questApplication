package com.questApplication.questApplication.service.concretes;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.questApplication.questApplication.configuration.RabbitMQConfig;
import com.questApplication.questApplication.entity.EmailMessage;

@Service
public class EmailNotificationManager {
    private final RabbitTemplate rabbitTemplate;

    public EmailNotificationManager(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEmailNotification(String to, String subject, String content) {
        EmailMessage message = new EmailMessage(to, subject, content);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_QUEUE, message);
    }
}