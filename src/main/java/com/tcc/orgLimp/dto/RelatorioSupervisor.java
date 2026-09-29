package com.tcc.orgLimp.dto;

public class RelatorioSupervisor {

    private Long id;
    private String nome;
    private long total;
    private long concluidas;
    private long emAberto;

    public RelatorioSupervisor(
            Long id,
            String nome,
            long total,
            long concluidas,
            long emAberto) {

        this.id = id;
        this.nome = nome;
        this.total = total;
        this.concluidas = concluidas;
        this.emAberto = emAberto;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public long getTotal() {
        return total;
    }

    public long getConcluidas() {
        return concluidas;
    }

    public long getEmAberto() {
        return emAberto;
    }
}