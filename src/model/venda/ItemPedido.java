package model.venda;

import model.produto.Camisa;

public class ItemPedido {
    private final Camisa camisa;
    private int quantidade;

    public ItemPedido(Camisa camisa, int quantidade) {
        this.camisa = camisa;
        this.quantidade = quantidade;
    }

    public Camisa getCamisa() {
        return camisa;
    }
    public int getQuantidade() {
        return quantidade;
    }

    public void adicionarQuantidade(int qtd) {
        this.quantidade += qtd;
    }

    public double getSubtotalReais(){
        return (camisa.getPrecoVendaReais() * quantidade);
    }
}
