package pagamento;


import enums.TipoPagamento;
import exception.PagamentoRecusadoException;

public class PagamentoPix implements Pagamento{
    private final String chave;

    public PagamentoPix(String chave) {
        this.chave = chave;
    }

    @Override
    public void pagar(double valor) throws PagamentoRecusadoException {
        if (valor <= 0){
            throw new PagamentoRecusadoException("Pagamento Recusado, Valor Invalido");
        }
        if (chave == null || chave.isBlank()){
            throw new PagamentoRecusadoException("Pagamento Recusado, Chave Invalida");
        }
        System.out.println("PAGO");
    }

    @Override
    public TipoPagamento getTipo() {
        return TipoPagamento.PIX;
    }
}
