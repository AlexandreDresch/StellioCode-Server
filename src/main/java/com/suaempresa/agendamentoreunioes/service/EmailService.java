package com.suaempresa.agendamentoreunioes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service // Marca a classe como um serviço Spring
public class EmailService {

    @Autowired // Injeta o serviço de email automaticamente
    private JavaMailSender emailSender;

    // Método para enviar emails simples
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to); // Define o destinatário do email
        message.setSubject(subject); // Define o assunto do email
        message.setText(text); // Define o conteúdo do email
        emailSender.send(message); // Envia o email
    }
}
