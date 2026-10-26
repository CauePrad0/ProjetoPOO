package pagamento;

import enums.TipoPagamento;
import exception.PagamentoRecusadoException;

public interface Pagamento {
    void pagar(double valor) throws PagamentoRecusadoException;
    TipoPagamento getTipo();
}
