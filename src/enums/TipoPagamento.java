package enums;

public enum TipoPagamento {
    PIX(0.5),
    CARTAO(0.0);

    private final double percentualDesconto;

    TipoPagamento(double percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    public double getPercentualDesconto() {
        return percentualDesconto;
    }
}
