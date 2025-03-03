package com.stelliocode.backend.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); // Servidor SMTP do Gmail
        mailSender.setPort(587); // Porta para TLS

        // Credenciais do e-mail
        mailSender.setUsername("seu-email@gmail.com"); // Substitua pelo seu e-mail
        mailSender.setPassword("sua-senha"); // Substitua pela sua senha

        // Propriedades adicionais
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true"); // Autenticação SMTP
        props.put("mail.smtp.starttls.enable", "true"); // Habilita STARTTLS

        return mailSender;
    }
}