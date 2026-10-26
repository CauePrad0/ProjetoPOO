package model.produto;

import enums.Tamanho;
import enums.Versao;
import model.atores.Fornecedor;

public class CamisaRetro extends Camisa{
    private static final double FATOR_RETRO = 1.8;

    public CamisaRetro(String time, String temporada, Tamanho tamanho, Versao versao, double custoUsd, double precoVendaReais ,Fornecedor fornecedor) {
        super(time, temporada, tamanho, versao, custoUsd, precoVendaReais ,fornecedor);
    }


    @Override
    public double calcularCustoUsd() {
        return getCustoUsd() * getVersao().getFatorPreco() * FATOR_RETRO;
    }
}
