package service;

import enums.StatusPedido;
import exception.EstoqueInsuficienteException;
import exception.PagamentoRecusadoException;
import model.estoque.Estoque;
import model.produto.Camisa;
import model.venda.ItemPedido;
import model.venda.Pedido;
import pagamento.Pagamento;

public class PedidoService {
    private final Estoque estoque;

    public PedidoService(Estoque estoque) {
        this.estoque = estoque;
    }

    public void finalizar(Pedido pedido, Pagamento pagamento) throws EstoqueInsuficienteException, PagamentoRecusadoException {
        if (pedido.getStatus() != StatusPedido.CRIADO) {
            throw new PagamentoRecusadoException("Pagamento Recusado, Pedido já finalizado. Status atual: " + pedido.getStatus());
        }
        for (ItemPedido item : pedido.getItens()) {
            Camisa camisa = item.getCamisa();
            int qtd = item.getQuantidade();
            if (estoque.consultar(camisa) < qtd) {
                throw new EstoqueInsuficienteException("A camisa do " + camisa.getTime() + " do tamanho: " + camisa.getTamanho() + " está sem estoque!" + " Estoque disponivel: " + estoque.consultar(camisa));
            }
        }

        double subtotal = pedido.getSubtotalReais();
        double desconto = subtotal * pagamento.getTipo().getPercentualDesconto();
        double total = subtotal - desconto;

        pagamento.pagar(total);

        for (ItemPedido item : pedido.getItens()) {
            Camisa camisa = item.getCamisa();
            int qtd = item.getQuantidade();
            estoque.remover(camisa, qtd);
        }

        pedido.marcarComoPago();
    }
}
