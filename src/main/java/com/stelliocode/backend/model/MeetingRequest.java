package com.stelliocode.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa uma solicitação de reunião feita por um cliente.
 */
@Entity
@Table(name = "meeting_requests")
public class MeetingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String clientId;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String clientName;

    @Email
    @NotBlank
    @Column(nullable = false, length = 100)
    private String clientEmail;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String clientPhone;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String title; // Adicionado o campo title

    @NotNull
    @Column(nullable = false)
    private LocalDateTime meetingDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Size(max = 500, message = "As notas não podem ter mais de 500 caracteres")
    @Column(length = 500)
    private String notes;

    public MeetingRequest() {
        this.status = Status.PENDENTE; // Define o status padrão como "PENDENTE"
    }

    public MeetingRequest(String clientId, String clientName, String clientEmail, String clientPhone, String title, LocalDateTime meetingDateTime, String notes) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhone = clientPhone;
        this.title = title; // Inicializa o campo title
        this.meetingDateTime = meetingDateTime;
        this.notes = notes;
        this.status = Status.PENDENTE; // Define o status padrão como "PENDENTE"
    }

    public enum Status {
        PENDENTE("Pendente"),
        ACEITA("Aceita"),
        RECUSADA("Recusada"),
        REALIZADA("Realizada");

        private final String descricao;

        Status(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getMeetingDateTime() {
        return meetingDateTime;
    }

    public void setMeetingDateTime(LocalDateTime meetingDateTime) {
        this.meetingDateTime = meetingDateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "MeetingRequest{" +
                "id=" + id +
                ", clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientEmail='" + clientEmail + '\'' +
                ", clientPhone='" + clientPhone + '\'' +
                ", title='" + title + '\'' +
                ", meetingDateTime=" + meetingDateTime +
                ", status=" + status +
                ", notes='" + notes + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingRequest that = (MeetingRequest) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(clientId, that.clientId) &&
                Objects.equals(meetingDateTime, that.meetingDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, meetingDateTime);
    }
}