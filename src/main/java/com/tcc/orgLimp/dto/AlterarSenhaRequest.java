package com.tcc.orgLimp.dto;

import com.tcc.orgLimp.validation.SenhaValida;
import jakarta.validation.constraints.NotBlank;

public class AlterarSenhaRequest {

    @NotBlank(message = "Informe sua senha atual.")
    private String senhaAtual;

    @SenhaValida
    private String novaSenha;

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}