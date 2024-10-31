package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.configuration.RabbitMQConfig;
import com.questApplication.questApplication.entity.EmailMessage;
import com.questApplication.questApplication.core.utilities.exception.ResourceNotFoundException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailListener {

    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailListener.class);

    public EmailListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void handleEmailMessage(EmailMessage message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(message.getTo());
            mailMessage.setSubject(message.getSubject());
            mailMessage.setText(message.getContent());

            mailSender.send(mailMessage);
            logger.info("E-posta başarıyla gönderildi: {}", message);
        } catch (Exception e) {
            logger.error("E-posta gönderilirken bir hata oluştu: {}", e.getMessage());
            throw new ResourceNotFoundException("E-posta gönderimi sırasında bir hata oluştu");
        }
    }
}