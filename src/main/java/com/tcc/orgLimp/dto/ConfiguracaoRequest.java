package com.tcc.orgLimp.dto;

public class ConfiguracaoRequest {

    private String empresa;
    private String email;
    private String whatsapp;
    private Boolean notificarEmail;
    private Boolean notificarWhatsApp;

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
