package pagamento;

import enums.TipoPagamento;
import exception.PagamentoRecusadoException;

public class PagamentoCartao implements Pagamento{
    private final String numero;
    private final int parcelas;

    public PagamentoCartao(String numero, int parcelas) {
        this.numero = numero;
        this.parcelas = parcelas;
    }



    @Override
    public void pagar(double valor) throws PagamentoRecusadoException {
        if (valor <= 0){
            throw new PagamentoRecusadoException("Pagamento Recusado, Valor Invalido");
        }
        if (parcelas > 12 || parcelas <= 0){
            throw new PagamentoRecusadoException("Pagamento Recusado, Quantidade de Parcelas Invalido");
        }
        if (numero.length() != 16){
            throw new PagamentoRecusadoException("Pagamento Recusado, Numero do Cartão Invalido");
        }
        System.out.println("PAGO!");
    }

    @Override
    public TipoPagamento getTipo() {
        return TipoPagamento.CARTAO;
    }
}
