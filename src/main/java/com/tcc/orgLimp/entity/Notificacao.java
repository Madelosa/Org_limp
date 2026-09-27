package com.tcc.orgLimp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacoes")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long destinatarioId;

    @NotBlank
    private String titulo;

    @NotBlank
    private String mensagem;

    @NotBlank
    private String tipo = "info";

    @NotNull
    private Boolean lida = false;

    @NotNull
    private LocalDateTime data;

    public Notificacao() {}

    public Notificacao(Long destinatarioId, String titulo, String mensagem, String tipo) {
        this.destinatarioId = destinatarioId;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.tipo = tipo;
        this.lida = false;
        this.data = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDestinatarioId() { return destinatarioId; }
    public void setDestinatarioId(Long destinatarioId) { this.destinatarioId = destinatarioId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Boolean getLida() { return lida; }
    public void setLida(Boolean lida) { this.lida = lida; }
    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }
}
