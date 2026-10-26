package model.produto;

import enums.Tamanho;
import enums.Versao;
import model.atores.Fornecedor;

public abstract class Camisa {

    private final String time;
    private final String temporada;
    private final Tamanho tamanho;
    private final Versao versao;
    private final double custoUsd;
    private final double precoVendaReais;
    private final Fornecedor fornecedor;

    public Camisa(String time, String temporada, Tamanho tamanho, Versao versao, double custoUsd,double precoVendaReais ,Fornecedor fornecedor) {
        this.time = time;
        this.temporada = temporada;
        this.tamanho = tamanho;
        this.versao = versao;
        this.custoUsd = custoUsd;
        this.precoVendaReais = precoVendaReais;
        this.fornecedor = fornecedor;
    }

    public abstract double calcularCustoUsd();

    public String getTime() {
        return time;
    }
    public String getTemporada() {
        return temporada;
    }
    public Tamanho getTamanho() {
        return tamanho;
    }
    public Versao getVersao() {
        return versao;
    }
    public double getCustoUsd() {
        return custoUsd;
    }
    public Fornecedor getFornecedor() {
        return fornecedor;
    }
    public double getPrecoVendaReais() {
        return precoVendaReais;
    }
}
