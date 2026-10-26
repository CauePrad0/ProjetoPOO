package service;

import enums.Tributo;
import exception.ProdutoInvalidoException;
import model.produto.Camisa;

public class PrecificacaoService {
    private final double cotacaoDolar;

    public PrecificacaoService(double cotacaoDolar) {
        this.cotacaoDolar = cotacaoDolar;
    }

    public double calcularCustoReais(Camisa camisa){
        return camisa.calcularCustoUsd() * cotacaoDolar;
    }

    public double calcularCustoComTributos(Camisa camisa){
        double valor = calcularCustoReais(camisa);
        for (Tributo t : Tributo.values()){
            valor += valor* t.getAliquota();
        }
        return valor;
    }

    public double calcularLucro(Camisa camisa){
        return camisa.getPrecoVendaReais() - calcularCustoComTributos(camisa);
    }

    public double calcularMargem(Camisa camisa){
        double precoVenda = camisa.getPrecoVendaReais();
        if (precoVenda <= 0){
            throw new ProdutoInvalidoException("Preço de venda invalido para calcular margem de lucro");
        }
        return calcularLucro(camisa)/precoVenda;

    }



}
