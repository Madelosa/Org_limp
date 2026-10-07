package com.tcc.orgLimp.dto;

import com.tcc.orgLimp.entity.Usuario;

public class UsuarioSessao {

    private Long id;
    private String nome;
    private String email;
    private Usuario.Perfil perfil;
    private Boolean ativo;

    public UsuarioSessao() {
    }

    public UsuarioSessao(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.perfil = usuario.getPerfil();
        this.ativo = usuario.getAtivo();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Usuario.Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Usuario.Perfil perfil) {
        this.perfil = perfil;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}