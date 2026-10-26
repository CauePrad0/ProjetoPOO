package enums;

public enum Versao {
    TORCEDOR(1.0),
    JOGADOR(1.8);

    private final double fatorPreco;

    Versao(double fatorPreco){
         this.fatorPreco = fatorPreco;
    }

    public double getFatorPreco() {
        return fatorPreco;
    }
}
