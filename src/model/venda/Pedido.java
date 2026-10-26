package model.venda;

import enums.StatusPedido;
import model.atores.Cliente;
import model.produto.Camisa;

import java.util.ArrayList;

public class Pedido {
    private final Cliente cliente;
    private StatusPedido status;
    private final ArrayList<ItemPedido> itens = new ArrayList<>();

    public Pedido(Cliente cliente, Camisa camisa, int qtd) {
        this.cliente = cliente;
        itens.add(new ItemPedido(camisa, qtd));
        this.status = StatusPedido.CRIADO;

    }

    public ArrayList<ItemPedido> getItens() {
        return itens;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void adicionarItem(Camisa camisa, int qtd) {
        for (ItemPedido item : itens) {
            if (item.getCamisa().equals(camisa)) {
                item.adicionarQuantidade(qtd);
                return;
            }
        }
        itens.add(new ItemPedido(camisa, qtd));
    }
    public void adicionarItem(Camisa camisa){
        adicionarItem(camisa,1);
    }

    public double getSubtotalReais(){
        double subtotal = 0;
        for(ItemPedido i : itens){
            subtotal += i.getSubtotalReais();
        }
        return subtotal;
    }
    public void marcarComoPago(){
        status = StatusPedido.PAGO;
    }

}
