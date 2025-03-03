package com.stelliocode.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks
    }

    @Test
    public void testSendSimpleMessage_Success() {
        String to = "destinatario@example.com";
        String subject = "Assunto do Email";
        String text = "Conteúdo do Email";

        emailService.sendSimpleMessage(to, subject, text);

        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendSimpleMessage_EmptyTo() {
        String to = "";
        String subject = "Assunto do Email";
        String text = "Conteúdo do Email";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendSimpleMessage(to, subject, text);
        });

        assertEquals("O endereço de email do destinatário não pode estar vazio", exception.getMessage());
        verify(emailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendSimpleMessage_EmptySubject() {
        String to = "destinatario@example.com";
        String subject = "";
        String text = "Conteúdo do Email";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendSimpleMessage(to, subject, text);
        });

        assertEquals("O assunto do email não pode estar vazio", exception.getMessage());
        verify(emailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendSimpleMessage_EmptyText() {
        String to = "destinatario@example.com";
        String subject = "Assunto do Email";
        String text = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendSimpleMessage(to, subject, text);
        });

        assertEquals("O conteúdo do email não pode estar vazio", exception.getMessage());
        verify(emailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendSimpleMessage_Exception() {
        String to = "destinatario@example.com";
        String subject = "Assunto do Email";
        String text = "Conteúdo do Email";

        doThrow(new RuntimeException("Erro ao enviar email")).when(emailSender).send(any(SimpleMailMessage.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            emailService.sendSimpleMessage(to, subject, text);
        });

        assertEquals("Falha ao enviar email", exception.getMessage());
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}