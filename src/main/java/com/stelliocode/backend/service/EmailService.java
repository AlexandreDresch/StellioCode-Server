package com.stelliocode.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        // Validação dos parâmetros
        if (!StringUtils.hasText(to)) {
            throw new IllegalArgumentException("O endereço de email do destinatário não pode estar vazio");
        }
        if (!StringUtils.hasText(subject)) {
            throw new IllegalArgumentException("O assunto do email não pode estar vazio");
        }
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException("O conteúdo do email não pode estar vazio");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            emailSender.send(message);
            logger.info("Email enviado com sucesso para: {}", to);
        } catch (Exception e) {
            logger.error("Erro ao enviar email para: {} - Erro: {}", to, e.getMessage());
            throw new RuntimeException("Falha ao enviar email", e);
        }
    }
}

//package com.stelliocode.backend.service;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//
//
//@Service
//public class EmailService {
//
//    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
//
//    @Autowired
//    private JavaMailSender emailSender;
//
//    public void sendSimpleMessage(String to, String subject, String text) {
//        try {
//            // Validação dos parâmetros
//            if (!StringUtils.hasText(to)) {
//                throw new IllegalArgumentException("O endereço de email do destinatário não pode estar vazio");
//            }
//            if (!StringUtils.hasText(subject)) {
//                throw new IllegalArgumentException("O assunto do email não pode estar vazio");
//            }
//            if (!StringUtils.hasText(text)) {
//                throw new IllegalArgumentException("O conteúdo do email não pode estar vazio");
//            }
//
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(to);
//            message.setSubject(subject);
//            message.setText(text);
//
//            emailSender.send(message);
//            logger.info("Email enviado com sucesso para: {}", to);
//        } catch (Exception e) {
//            logger.error("Erro ao enviar email para: {} - Erro: {}", to, e.getMessage());
//            throw new RuntimeException("Falha ao enviar email", e);
//        }
//    }
//}
//
