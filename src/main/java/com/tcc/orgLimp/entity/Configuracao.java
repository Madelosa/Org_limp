package com.tcc.orgLimp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "configuracoes")
public class Configuracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String empresa;

    private String email;

    private String whatsapp;

    private Boolean notificarEmail = true;

    private Boolean notificarWhatsApp = true;

    public Configuracao() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }
    public Boolean getNotificarEmail() { return notificarEmail; }
    public void setNotificarEmail(Boolean notificarEmail) { this.notificarEmail = notificarEmail; }
    public Boolean getNotificarWhatsApp() { return notificarWhatsApp; }
    public void setNotificarWhatsApp(Boolean notificarWhatsApp) { this.notificarWhatsApp = notificarWhatsApp; }
}
