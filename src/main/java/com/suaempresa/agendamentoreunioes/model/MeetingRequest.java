package com.suaempresa.agendamentoreunioes.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity // Indica que esta classe é uma entidade JPA mapeada para uma tabela no banco de dados
public class MeetingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gera o ID automaticamente
    private Long id;

    @NotBlank // Valida que o campo não pode estar em branco
    private String clientId;

    @NotBlank
    private String clientName;

    @Email // Valida que o campo deve conter um email válido
    @NotBlank
    private String clientEmail;

    @NotBlank
    private String clientPhone;

    @NotNull // Valida que o campo não pode ser nulo
    private LocalDateTime meetingDateTime;

    @Enumerated(EnumType.STRING) // Armazena o enum como uma string no banco de dados
    private Status status;

    private String notes;

    public enum Status {
        PENDENTE, ACEITA, RECUSADA, REALIZADA // Possíveis status de uma reunião
    }

    // Getters e Setters
}

