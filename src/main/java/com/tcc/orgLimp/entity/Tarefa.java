package com.tcc.orgLimp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String local;

    @NotNull
    private LocalDate data;

    @NotNull
    private LocalTime hora;

    @NotNull
    private LocalDate prazo;

    @NotNull
    private Long supervisorId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status = Status.pendente;

    private String observacao;

    @NotNull
    private LocalDateTime criadoEm;

    public enum Status {
        pendente, iniciada, em_andamento, concluida
    }

    public Tarefa() {}

    public Tarefa(String titulo, String local, LocalDate data, LocalTime hora, LocalDate prazo, Long supervisorId, Status status, String observacao) {
        this.titulo = titulo;
        this.local = local;
        this.data = data;
        this.hora = hora;
        this.prazo = prazo;
        this.supervisorId = supervisorId;
        this.status = status;
        this.observacao = observacao;
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    public LocalDate getPrazo() { return prazo; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }
    public Long getSupervisorId() { return supervisorId; }
    public void setSupervisorId(Long supervisorId) { this.supervisorId = supervisorId; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
